package wiresegal.psionup.common.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable

/**
 * @author WireSegal
 * Created at 8:59 PM on 7/5/16.
 */
class InventoryCADCase(stack: ItemStack) : IInventory {

    val handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) as IItemHandlerModifiable

    override fun clear() {
        for (i in 0..handler.slots-1)
            handler.setStackInSlot(i, null)
    }

    override fun closeInventory(player: EntityPlayer?) {
        //NO-OP
    }

    override fun decrStackSize(index: Int, count: Int): ItemStack? {
        return handler.extractItem(index, 1, false)
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
        return handler.getStackInSlot(index)
    }

    override fun openInventory(player: EntityPlayer?) {
        //NO-OP
    }

    override fun setField(id: Int, value: Int) {
        //NO-OP
    }

    override fun removeStackFromSlot(index: Int): ItemStack? {
        return handler.extractItem(index, 64, true)
    }

    override fun markDirty() {
        //NO-OP
    }

    override fun getSizeInventory(): Int {
        return handler.slots
    }

    override fun isItemValidForSlot(index: Int, stack: ItemStack?): Boolean {
        if (index == 0) return stack?.item is ICAD
        if (index == 1) return stack?.item is ISocketable && stack?.item !is ICAD
        return false
    }

    override fun isUseableByPlayer(player: EntityPlayer?): Boolean {
        return true
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack?) {
        handler.setStackInSlot(index, stack)
    }

    override fun getName(): String? {
        return "psionup.container.case"
    }

    override fun hasCustomName(): Boolean {
        return false
    }
}
