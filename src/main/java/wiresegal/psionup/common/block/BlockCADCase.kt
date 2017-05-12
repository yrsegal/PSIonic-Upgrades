package wiresegal.psionup.common.block

import com.teamwizardry.librarianlib.features.base.block.BlockModContainer
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.local
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.gui.GuiScreen
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.ISpellContainer
import wiresegal.psionup.common.block.tile.TileCADCase
import wiresegal.psionup.common.items.ItemCADCase
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.lib.LibNames
import java.awt.Color

/**
 * @author WireSegal
 * Created at 2:52 PM on 7/5/16.
 */
class BlockCADCase(name: String) : BlockModContainer(name, Material.CLOTH, *makeVariants(name)), IBlockColorProvider {
    companion object {
        fun makeVariants(name: String): Array<String> {
            return Array(16) {
                name + LibNames.Colors[it]
            }
        }

        val WEST_AABB = AxisAlignedBB(3.5 / 16.0, 0.0, 0.5 / 16.0, 12.5 / 16.0, 4.5 / 16.0, 15.5 / 16.0)
        val NORTH_AABB = AxisAlignedBB(0.5 / 16.0, 0.0, 3.5 / 16.0, 15.5 / 16.0, 4.5 / 16.0, 12.5 / 16.0)
        val EAST_AABB = AxisAlignedBB(3.5 / 16.0, 0.0, 0.5 / 16.0, 12.5 / 16.0, 4.5 / 16.0, 15.5 / 16.0)
        val SOUTH_AABB = AxisAlignedBB(0.5 / 16.0, 0.0, 3.5 / 16.0, 15.5 / 16.0, 4.5 / 16.0, 12.5 / 16.0)

        val OPEN: PropertyBool = PropertyBool.create("open")
        val FACING: PropertyDirection = PropertyDirection.create("facing", EnumFacing.HORIZONTALS.toList())
        val COLOR: PropertyEnum<EnumDyeColor> = PropertyEnum.create("color", EnumDyeColor::class.java)
    }

    init {
        setHardness(0.5f)
        soundType = SoundType.METAL
    }

    override fun removedByPlayer(state: IBlockState, world: World, pos: BlockPos, player: EntityPlayer, willHarvest: Boolean): Boolean {
        if (willHarvest) {
            onBlockHarvested(world, pos, state, player)
            return true
        } else {
            return super.removedByPlayer(state, world, pos, player, willHarvest)
        }
    }

    override fun harvestBlock(worldIn: World, player: EntityPlayer?, pos: BlockPos, state: IBlockState?, te: TileEntity?, stack: ItemStack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack)
        worldIn.setBlockToAir(pos)
    }

    override fun isFullBlock(state: IBlockState?) = false
    override fun isBlockSolid(worldIn: IBlockAccess?, pos: BlockPos?, side: EnumFacing?) = false
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false

    override fun createItemForm(): ItemBlock? {
        return ItemCADCase(this)
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileCADCase()
    }

    override fun getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): MutableList<ItemStack>? {
        val baseStack = ItemStack(this, 1, getActualState(state, world, pos).getValue(COLOR).metadata)
        val te = (world.getTileEntity(pos) ?: return mutableListOf(baseStack)) as TileCADCase

        val handler = baseStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) ?: return mutableListOf(baseStack)
        for (slot in 0..te.itemHandler.slots - 1)
            handler.insertItem(slot, te.itemHandler.getStackInSlot(slot)?.copy(), false)
        if (te.name != null)
            baseStack.setStackDisplayName(te.name)

        return mutableListOf(baseStack)
    }

    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP) || worldIn.getBlockState(pos.down()).block === Blocks.GLOWSTONE
    }

    override fun addInformation(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        val handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
        val ifShift = "|  " + local("psimisc.shiftForInfo")
        var flag = false
        if (handler != null) {
            for (slot in 0..handler.slots - 1) {
                val inSlot = handler.extractItem(slot, 1, true)
                if (inSlot.isEmpty) {
                    if (flag)
                        tooltip.add("")
                    addToTooltip(tooltip, "| ${TextFormatting.WHITE}${inSlot.displayName}")
                    val slotTooltip = inSlot.getTooltip(player, false)
                    if (slotTooltip.size > 1) {
                        if (GuiScreen.isShiftKeyDown()) {
                            for (line in slotTooltip.subList(1, slotTooltip.size))
                                addToTooltip(tooltip, "|   $line")
                            flag = true
                        } else
                            addToTooltip(tooltip, ifShift)
                    }
                }
            }
        }
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos?, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack) {
        val tile = worldIn.getTileEntity(pos) as TileCADCase
        tile.woolColor = stack.itemDamage
        val handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) ?: return
        for (slot in 0..handler.slots - 1)
            tile.itemHandler.insertItem(slot, handler.getStackInSlot(slot).copy(), false)
        if (stack.hasDisplayName())
            tile.name = stack.displayName
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) {
        if (!worldIn.isRemote) {
            if (this.canPlaceBlockAt(worldIn, pos)) {
                val redstone = worldIn.isBlockPowered(pos)

                if (redstone || blockIn.defaultState.canProvidePower()) {
                    val open = state.getValue(OPEN)

                    if (open != redstone) {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, redstone), 2)
                        playSound(worldIn, pos, redstone)
                    }
                }
            } else {
                this.dropBlockAsItem(worldIn, pos, state, 0)
                worldIn.setBlockToAir(pos)
            }
        }
    }

    override fun hasComparatorInputOverride(state: IBlockState?): Boolean {
        return true
    }

    override fun getComparatorInputOverride(blockState: IBlockState?, worldIn: World?, pos: BlockPos?): Int {
        val tile = (worldIn?.getTileEntity(pos) ?: return 0) as TileCADCase
        val handler = tile.itemHandler
        val i = (0..handler.slots - 1)
                .map { handler.getStackInSlot(it) }
                .count { !it.isEmpty }

        return Math.ceil(15.0 / handler.slots * i).toInt()
    }

    fun playSound(worldIn: World, pos: BlockPos, closing: Boolean) {
        worldIn.playSound(null, pos,
                if (closing) SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE else SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN,
                SoundCategory.BLOCKS, 1f, 1f)
    }

    override fun getPickBlock(state: IBlockState, target: RayTraceResult?, world: World, pos: BlockPos, player: EntityPlayer?): ItemStack {
        return ItemStack(this, 1, getActualState(state, world, pos).getValue(COLOR).metadata)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (playerIn.isSneaking) {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, state.cycleProperty(OPEN), 2)
                playSound(worldIn, pos, state.getValue(OPEN))
            }

            return true
        }

        if (!state.getValue(OPEN)) return false

        val tile = (worldIn.getTileEntity(pos) ?: return false) as TileCADCase
        return tile.onClick(state, playerIn, hand, playerIn.getHeldItem(hand), hitX, hitZ)
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

    override fun getStateForPlacement(world: World?, pos: BlockPos?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase?, hand: EnumHand?): IBlockState {
        return defaultState.withProperty(FACING, placer?.horizontalFacing?.opposite).withProperty(OPEN, false)
    }

    override val blockColorFunction: ((state: IBlockState, world: IBlockAccess?, pos: BlockPos?, tintIndex: Int) -> Int)?
        get() = { iBlockState, iBlockAccess, blockPos, i ->
            if (i == 1 && blockPos != null) {
                val colorArr = EntitySheep.getDyeRgb(getActualState(iBlockState, iBlockAccess!!, blockPos).getValue(COLOR))
                Color(colorArr[0], colorArr[1], colorArr[2]).rgb
            } else 0xFFFFFF
        }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, i ->
            if (i == 1) {
                val colorArr = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(itemStack.itemDamage))
                Color(colorArr[0], colorArr[1], colorArr[2]).rgb
            } else 0xFFFFFF
        }

    open class CaseStackHandler : ItemStackHandler(2) {

        fun canInsertIntoSlot(slot: Int, stack: ItemStack): Boolean {
            if (slot == 0) return stack.item is ICAD || stack.item == ModItems.gaussRifle
            if (slot == 1) return (stack.item is ISocketable && stack.item !is ICAD) || stack.item is ISpellContainer
            return false
        }

        override fun getStackLimit(slot: Int, stack: ItemStack) = if (!canInsertIntoSlot(slot, stack)) 0 else 1
    }

}
