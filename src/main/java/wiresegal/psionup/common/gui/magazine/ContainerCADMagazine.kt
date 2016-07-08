package wiresegal.psionup.common.gui.magazine

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.ISpellContainer
import vazkii.psi.common.block.tile.TileCADAssembler
import wiresegal.psionup.common.gui.cadcase.InventoryCADCase
import wiresegal.psionup.common.items.spell.ItemCADMagazine
import wiresegal.psionup.common.lib.LibMisc

class ContainerCADMagazine(val player: EntityPlayer, val stack: ItemStack) : Container() {
    val inventory = InventorySocketable(stack, ItemCADMagazine.getBandwidth(stack))
    val cadInventory: InventorySocketable

    val innerSlots = arrayOf(
            135 to 88,
            129 to 109,
            110 to 128,
            89 to 134,
            68 to 128,
            49 to 109,
            43 to 88,
            49 to 67,
            68 to 48,
            89 to 42,
            110 to 48,
            129 to 67
    )

    val outerSlots = arrayOf(
            173 to 88,
            159 to 131,
            130 to 160,
            89 to 171,
            48 to 160,
            19 to 131,
            5 to 88,
            19 to 45,
            48 to 16,
            89 to 5,
            130 to 16,
            159 to 45
    )

    init {
        val cad = PsiAPI.getPlayerCAD(player)
        cadInventory = InventorySocketable(cad)

        var i = 0

        for (slot in innerSlots) {
            addSlotToContainer(SlotBullet(cadInventory, i, slot.first, slot.second, i, true))
            i++
        }

        i = 0

        for (slot in outerSlots) {
            addSlotToContainer(SlotBullet(inventory, i, slot.first, slot.second, i, false))
            i++
        }
    }

    var dontNotify = false
    var notifyOnce = false
    var tooltipTime = 0
    var tooltipText = ""

    inner class SlotBullet(val socketable: InventorySocketable, index: Int, xPosition: Int, yPosition: Int, var socketSlot: Int, val dark: Boolean) : Slot(socketable, index, xPosition, yPosition) {

        override fun isItemValid(stack: ItemStack?): Boolean {
            if (stack!!.item is ISpellContainer) {
                val container = stack.item as ISpellContainer
                if (container.containsSpell(stack) && isSlotEnabled()) {
                    val ret = socketable.isItemValidForSlot(socketSlot, stack)
                    if (!ret && (!dontNotify || notifyOnce) && player.worldObj.isRemote) {
                        tooltipTime = 80
                        tooltipText = "${LibMisc.MOD_ID}.misc.tooComplex"
                        if (notifyOnce) notifyOnce = false
                    } else if (ret)
                        tooltipTime = 0
                    return ret
                }
            }

            return false
        }

        override fun canTakeStack(playerIn: EntityPlayer?): Boolean {
            val ret = socketable.isItemValidForSlot(socketSlot, stack)
            if (!ret && (!dontNotify || notifyOnce) && player.worldObj.isRemote) {
                tooltipTime = 80
                tooltipText = "${LibMisc.MOD_ID}.misc.tooComplexBullet"
                if (notifyOnce) notifyOnce = false
            } else if (ret)
                tooltipTime = 0
            return ret
        }

        override fun canBeHovered(): Boolean {
            return isSlotEnabled()
        }

        fun isSlotEnabled() = socketSlot <= socketable.sizeInventory
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return inventory.isUseableByPlayer(playerIn)
    }

    override fun transferStackInSlot(playerIn: EntityPlayer?, index: Int): ItemStack? {
        var itemstack: ItemStack? = null
        val slot = inventorySlots[index]

        if (slot != null && slot.hasStack) {
            val itemstack1 = slot.stack
            itemstack = itemstack1!!.copy()


            val magazineStart = 0
            val magazineEnd = innerSlots.size - 1
            val cadStart = magazineEnd + 1
            val cadEnd = cadStart + outerSlots.size - 1

            dontNotify = true
            notifyOnce = true
            if (index > magazineEnd) {
                if (!this.mergeItemStack(itemstack1, magazineStart, magazineEnd, false))
                    return null // CAD -> Magazine
            } else if (!this.mergeItemStack(itemstack1, cadStart, cadEnd, false))
                return null // Magazine -> CAD
            dontNotify = false
            notifyOnce = false

            if (itemstack1.stackSize == 0)
                slot.putStack(null)
            else
                slot.onSlotChanged()

            if (itemstack1.stackSize == itemstack!!.stackSize)
                return null

            slot.onPickupFromSlot(playerIn, itemstack1)
        }

        return itemstack
    }

}
