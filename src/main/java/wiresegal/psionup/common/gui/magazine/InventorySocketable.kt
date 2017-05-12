package wiresegal.psionup.common.gui.magazine

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.EnumSpellStat
import vazkii.psi.api.spell.ISpellContainer
import vazkii.psi.common.spell.SpellCompiler
import wiresegal.psionup.common.core.helper.IteratorSocketable

/**
 * @author WireSegal
 * Created at 8:59 PM on 7/5/16.
 */
class InventorySocketable(val stack: ItemStack, val maxBandwidth: Int = -1) : IInventory {

    val item = stack.item as ISocketable

    val slots: Iterator<Pair<Int, ItemStack>>
        get() = IteratorSocketable(stack)

    val totalSlots: Int
        get() {
            var ret = 0
            for (i in slots) ret++
            return ret - 1
        }


    override fun clear() {
        for ((first) in slots) {
            item.setBulletInSocket(stack, first, ItemStack.EMPTY)
        }
    }

    override fun closeInventory(player: EntityPlayer?) {
        //NO-OP
    }

    override fun decrStackSize(index: Int, count: Int): ItemStack {
        val bullet = item.getBulletInSocket(stack, index)
        if (!bullet.isEmpty) item.setBulletInSocket(stack, index, ItemStack.EMPTY)
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

    override fun getStackInSlot(index: Int): ItemStack {
        val stack = item.getBulletInSocket(stack, index)
        return stack
    }

    override fun openInventory(player: EntityPlayer?) {
        //NO-OP
    }

    override fun setField(id: Int, value: Int) {
        //NO-OP
    }

    override fun removeStackFromSlot(index: Int): ItemStack {
        return decrStackSize(index, 1)
    }

    override fun markDirty() {
        //NO-OP
    }

    override fun getSizeInventory(): Int {
        return totalSlots
    }

    override fun isItemValidForSlot(index: Int, stack: ItemStack): Boolean {
        val item = (stack.item) as? ISpellContainer ?: return false
        if (maxBandwidth == -1) return true
        val spell = item.getSpell(stack)
        val compiled = SpellCompiler(spell)
        return (compiled.compiledSpell.metadata.stats[EnumSpellStat.BANDWIDTH] ?: Integer.MAX_VALUE) <= maxBandwidth
    }

    override fun isEmpty(): Boolean {
        for (i in slots) {
            if (!i.second.isEmpty) return true
        }
        return false
    }

    override fun isUsableByPlayer(player: EntityPlayer?): Boolean {
        return true
    }

    override fun setInventorySlotContents(index: Int, bullet: ItemStack) {
        item.setBulletInSocket(stack, index, bullet)
    }

    override fun getName(): String? {
        return "psionup.container.socketable"
    }

    override fun hasCustomName(): Boolean {
        return false
    }
}
