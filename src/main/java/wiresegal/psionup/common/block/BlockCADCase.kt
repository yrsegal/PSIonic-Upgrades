package wiresegal.psionup.common.block

import net.minecraft.block.BlockDirectional
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import shadowfox.botanicaladdons.client.core.ModelHandler
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.internal.Vector3
import wiresegal.psionup.common.block.base.BlockModContainer
import wiresegal.psionup.common.block.base.ItemModBlock
import wiresegal.psionup.common.block.tile.TileCADCase
import wiresegal.psionup.common.items.ItemCADCase
import wiresegal.psionup.common.lib.LibNames
import java.awt.Color

/**
 * @author WireSegal
 * Created at 2:52 PM on 7/5/16.
 */
class BlockCADCase(name: String) : BlockModContainer(name, Material.IRON, *makeVariants(name)), ModelHandler.IBlockColorProvider {
    companion object {
        fun makeVariants(name: String): Array<String> {
            return Array(16) {
                name + LibNames.Colors[it]
            }
        }

        val WEST_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) //todo
        val NORTH_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) //todo
        val EAST_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) //todo
        val SOUTH_AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) //todo

        val OPEN = PropertyBool.create("open")
        val FACING = PropertyDirection.create("facing", EnumFacing.HORIZONTALS.toList())
        val COLOR = PropertyEnum.create("color", EnumDyeColor::class.java)
    }

    override val item: ItemBlock?
        get() = ItemCADCase(this)

    override fun createTileEntity(world: World?, state: IBlockState?): TileEntity? {
        return TileCADCase()
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (playerIn.isSneaking) {
            if (!worldIn.isRemote)
                worldIn.setBlockState(pos, state.withProperty(OPEN, !state.getValue(OPEN)))
            else
                worldIn.playSound(playerIn, pos,
                        if (state.getValue(OPEN)) SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE else SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN,
                        SoundCategory.BLOCKS, 1f, 1f)

            return true
        }

        val tile = (worldIn.getTileEntity(pos) ?: return false) as TileCADCase
        return tile.onClick(worldIn, pos, state, playerIn, hand, heldItem, hitX, hitY, hitZ)
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, OPEN, FACING, COLOR)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        val horFlag = state.getValue(FACING).horizontalIndex
        val openFlag = if (state.getValue(OPEN)) 4 else 0
        return horFlag or openFlag
    }

    override fun getStateFromMeta(meta: Int): IBlockState? {
        val horFlag = meta and 3
        val openFlag = meta and 4
        return defaultState.withProperty(FACING, EnumFacing.HORIZONTALS[horFlag]).withProperty(OPEN, openFlag == 4)
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        val tile = (worldIn.getTileEntity(pos) ?: return state) as TileCADCase
        return state.withProperty(COLOR, EnumDyeColor.byMetadata(tile.woolColor))
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess?, pos: BlockPos?): AxisAlignedBB? {
        return when (state.getValue(FACING)) {
            EnumFacing.WEST -> WEST_AABB
            EnumFacing.NORTH -> NORTH_AABB
            EnumFacing.EAST -> EAST_AABB
            EnumFacing.SOUTH -> SOUTH_AABB
            else -> NULL_AABB // Can't ever happen
        }
    }

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf(COLOR)

    override fun onBlockPlaced(worldIn: World?, pos: BlockPos, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState? {
        return defaultState.withProperty(FACING, placer.horizontalFacing.opposite).withProperty(OPEN, false)
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos?, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack) {
        val tile = worldIn.getTileEntity(pos) as TileCADCase
        tile.woolColor = stack.itemDamage
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockColor(): IBlockColor? {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i ->
            if (i == 1 && blockPos != null) {
                val colorArr = EntitySheep.getDyeRgb(getActualState(iBlockState, iBlockAccess!!, blockPos).getValue(COLOR))
                Color(colorArr[0], colorArr[1], colorArr[2]).rgb
            } else 0xFFFFFF
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor? {
        return IItemColor { itemStack, i ->
            if (i == 1) {
                val colorArr = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(itemStack.itemDamage))
                Color(colorArr[0], colorArr[1], colorArr[2]).rgb
            } else 0xFFFFFF
        }
    }

    class CaseStackHandler() : ItemStackHandler(2) {
        override fun insertItem(slot: Int, stack: ItemStack?, simulate: Boolean): ItemStack? {
            if (stack == null || stack.stackSize == 0)
                return null

            validateSlotIndex(slot)

            val existing = this.stacks[slot]

            var limit = getStackLimit(slot, stack)
            val canInsert = canInsertIntoSlot(slot, stack)

            if (!canInsert) return stack

            if (existing != null) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                    return stack

                limit -= existing.stackSize
            }

            if (limit <= 0)
                return stack

            val reachedLimit = stack.stackSize > limit

            if (!simulate) {
                if (existing == null) {
                    this.stacks[slot] = if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, limit) else stack
                } else {
                    existing.stackSize += if (reachedLimit) limit else stack.stackSize
                }
                onContentsChanged(slot)
            }

            return if (reachedLimit) ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) else null
        }

        fun canInsertIntoSlot(slot: Int, stack: ItemStack): Boolean {
            if (slot == 0) return stack.item is ICAD
            if (slot == 1) return stack.item is ISocketable && stack.item !is ICAD
            return false
        }

        override fun getStackLimit(slot: Int, stack: ItemStack?): Int {
            return 1
        }
    }

}
