package wiresegal.psionup.common.gui.magazine

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.EnumSpellStat
import vazkii.psi.api.spell.ISpellContainer
import vazkii.psi.common.spell.SpellCompiler
import wiresegal.psionup.common.items.spell.ItemCADMagazine

/**
 * @author WireSegal
 * Created at 8:59 PM on 7/5/16.
 */
class InventorySocketable(val stack: ItemStack, val maxBandwidth: Int = -1) : IInventory {

    val item = stack.item as ISocketable

    private inner class IteratorSocketable : Iterator<Pair<Int, ItemStack?>> {

        var current = 0

        override fun hasNext(): Boolean {
            return item.isSocketSlotAvailable(stack, current + 1)
        }

        override fun next(): Pair<Int, ItemStack?> {
            current++
            return current to item.getBulletInSocket(stack, current)
        }
    }

    val slots: Iterator<Pair<Int, ItemStack?>>
        get() = IteratorSocketable()

    val totalSlots: Int
        get() {
            var ret = 0
            for ((i) in slots) ret++
            return ret
        }


    override fun clear() {
        for (i in slots) {
            item.setBulletInSocket(stack, i.first, null)
        }
    }

    override fun closeInventory(player: EntityPlayer?) {
        //NO-OP
    }

    override fun decrStackSize(index: Int, count: Int): ItemStack? {
        val bullet = item.getBulletInSocket(stack, index)
        if (bullet != null) item.setBulletInSocket(stack, index, null)
        return bullet
    }

    override fun getDisplayName(): ITextComponent? {
        return TextComponentTranslation(name)
    }

    override fun getField(id: Int): Int {
        return 0
    }

    override fun getFieldCount(): Int {
        return 0
    }

    override fun getInventoryStackLimit(): Int {
        return 1
    }

    override fun getStackInSlot(index: Int): ItemStack? {
        val stack = item.getBulletInSocket(stack, index)
        return stack
    }

    override fun openInventory(player: EntityPlayer?) {
        //NO-OP
    }

    override fun setField(id: Int, value: Int) {
        //NO-OP
    }

    override fun removeStackFromSlot(index: Int): ItemStack? {
        return decrStackSize(index, 1)
    }

    override fun markDirty() {
        //NO-OP
    }

    override fun getSizeInventory(): Int {
        return totalSlots
    }

    override fun isItemValidForSlot(index: Int, stack: ItemStack?): Boolean {
        val item = stack?.item ?: return false
        if (item !is ISpellContainer) return false
        if (maxBandwidth == -1) return true
        val spell = item.getSpell(stack)
        val compiled = SpellCompiler(spell)
        return (compiled.compiledSpell.metadata.stats[EnumSpellStat.BANDWIDTH] ?: Integer.MAX_VALUE) <= maxBandwidth
    }

    override fun isUseableByPlayer(player: EntityPlayer?): Boolean {
        return true
    }

    override fun setInventorySlotContents(index: Int, bullet: ItemStack?) {
        item.setBulletInSocket(stack, index, bullet)
    }

    override fun getName(): String? {
        return "psionup.container.socketable"
    }

    override fun hasCustomName(): Boolean {
        return false
    }
}
