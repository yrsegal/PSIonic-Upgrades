package wiresegal.psionup.common.block.tile

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import vazkii.botania.api.internal.VanillaPacketDispatcher
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.common.block.tile.base.TileMod
import wiresegal.psionup.common.block.BlockCADCase

/**
 * @author WireSegal
 * Created at 3:00 PM on 7/5/16.
 */
class TileCADCase : TileMod() {
    var woolColor = 0

    override fun writeSharedNBT(cmp: NBTTagCompound) {
        cmp.setByte("color", woolColor.toByte())
        cmp.setTag("inv", itemHandler.serializeNBT())
    }

    override fun readSharedNBT(cmp: NBTTagCompound) {
        woolColor = cmp.getByte("color").toInt()
        itemHandler.deserializeNBT(cmp.getCompoundTag("inv"))
    }

    val itemHandler: BlockCADCase.CaseStackHandler by lazy {
        object : BlockCADCase.CaseStackHandler() {
            override fun onContentsChanged(slot: Int) {
                markDirty()
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this@TileCADCase)
            }
        }
    }

    fun onClick(state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, heldItem: ItemStack?, hitX: Float, hitZ: Float): Boolean {
        val slot = getSlot(state.getValue(BlockCADCase.FACING), hitX, hitZ)
        if (heldItem == null) {
            if (itemHandler.getStackInSlot(slot) != null) {
                if (!world.isRemote)
                    playerIn.setHeldItem(hand, itemHandler.extractItem(slot, 1, false))
                return true
            }
        } else {
            if (itemHandler.getStackInSlot(slot) == null && itemHandler.canInsertIntoSlot(slot, heldItem)) {
                if (!world.isRemote) {
                    val heldCopy = heldItem.copy()
                    playerIn.setHeldItem(hand, itemHandler.insertItem(slot, heldCopy, false))
                }
                return true
            } else if (itemHandler.getStackInSlot(slot) != null) {
                if (!world.isRemote) {
                    val toAdd = itemHandler.extractItem(slot, 1, false)
                    if (!playerIn.inventory.addItemStackToInventory(toAdd))
                        playerIn.dropItem(toAdd, false)
                }
                return true
            }
        }
        return false
    }

    private fun getSlot(facing: EnumFacing, hitX: Float, hitZ: Float): Int {
        var x = hitX
        if (facing == EnumFacing.NORTH) {
            x = 1 - x
        } else if (facing == EnumFacing.EAST) {
            x = 1 - hitZ
        } else if (facing == EnumFacing.WEST) {
            x = hitZ
        }

        return if (x < 0.5) return 1 else 0
    }

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY/* && facing == null*/) || super.hasCapability(capability, facing)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T? {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY/* && facing == null*/)
            return itemHandler as T
        return super.getCapability(capability, facing)
    }
}
