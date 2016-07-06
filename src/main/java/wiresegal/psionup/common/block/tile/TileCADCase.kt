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
        itemHandler.deserializeNBT(cmp)
    }

    val itemHandler: ItemStackHandler by lazy {
        BlockCADCase.CaseStackHandler()
    }

    fun onClick(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand?, heldItem: ItemStack?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        return false //todo
    }

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == null) || super.hasCapability(capability, facing)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T? {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == null)
            return itemHandler as T
        return super.getCapability(capability, facing)
    }
}
