package wiresegal.psionup.common.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable

class ContainerCADCase(player: EntityPlayer, val stack: ItemStack) : Container() {
    val inventory = InventoryCADCase(stack)

    init {
        val playerInventory = player.inventory

        addSlotToContainer(object : Slot(inventory, 0, 132, 7) {

            override fun getItemStackLimit(stack: ItemStack?): Int {
                return 1
            }

            override fun isItemValid(stack: ItemStack?): Boolean {
                return stack?.item is ICAD
            }
        })


        addSlotToContainer(object : Slot(inventory, 1, 79, 7) {

            override fun getItemStackLimit(stack: ItemStack?): Int {
                return 1
            }

            override fun isItemValid(stack: ItemStack?): Boolean {
                return stack?.item is ISocketable && stack?.item !is ICAD
            }
        })

        val xs = 34
        val ys = 48

        for (i in 0..2)
            for (j in 0..8)
                addSlotToContainer(Slot(playerInventory, j + i * 9 + 9, xs + j * 18, ys + i * 18))

        for (k in 0..8)
            addSlotToContainer(object : Slot(playerInventory, k, xs + k * 18, ys + 58) {

                override fun canTakeStack(playerIn: EntityPlayer?): Boolean {
                    return !ItemStack.areItemStacksEqual(stack, this@ContainerCADCase.stack)
                }

                override fun canBeHovered(): Boolean {
                    return !ItemStack.areItemStacksEqual(stack, this@ContainerCADCase.stack)
                }
            })

        for (k in 0..3) {
            val slot = equipmentSlots[k]

            addSlotToContainer(object : Slot(playerInventory, playerInventory.sizeInventory - 2 - k, xs - 27, ys + 18 * k) {

                override fun getSlotStackLimit(): Int {
                    return 1
                }

                override fun isItemValid(stack: ItemStack?): Boolean {
                    return stack != null && stack.item.isValidArmor(stack, slot, player)
                }

                @SideOnly(Side.CLIENT)
                override fun getSlotTexture(): String? {
                    return ItemArmor.EMPTY_SLOT_NAMES[slot.index]
                }
            })
        }

        addSlotToContainer(object : Slot(playerInventory, playerInventory.sizeInventory - 1, 205, 48) {

            @SideOnly(Side.CLIENT)
            override fun getSlotTexture(): String? {
                return "minecraft:items/empty_armor_slot_shield"
            }

            override fun canTakeStack(playerIn: EntityPlayer?): Boolean {
                return !ItemStack.areItemStacksEqual(stack, this@ContainerCADCase.stack)
            }

            override fun canBeHovered(): Boolean {
                return !ItemStack.areItemStacksEqual(stack, this@ContainerCADCase.stack)
            }
        })
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

            val invStart = 1
            val hotbarStart = invStart + 28
            val invEnd = hotbarStart + 9

            if (index > invStart) {
                if (itemstack1.item is ICAD) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        return null // Inventory -> CAD slot
                } else if (itemstack1.item is ISocketable) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                        return null // Inventory -> Socket Slot
                }
            } else if (itemstack1.item is ItemArmor) {
                val armor = itemstack1.item as ItemArmor
                val armorSlot = 3 - armor.armorType.index
                if (!mergeItemStack(itemstack1, invEnd + armorSlot, invEnd + armorSlot + 1, true) && !mergeItemStack(itemstack1, invStart, invEnd, true))
                    return null // Assembler -> Armor+Inv+Hotbar

            } else if (!mergeItemStack(itemstack1, invStart, invEnd, true))
                return null // Assembler -> Inv+hotbar

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

    companion object {

        private val equipmentSlots = arrayOf(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET)
    }

}
