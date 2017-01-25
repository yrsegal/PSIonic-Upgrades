package wiresegal.psionup.common.block.spell

import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.common.block.BlockConjured.*
import wiresegal.psionup.common.block.base.BlockModContainer
import wiresegal.psionup.common.block.tile.TileConjuredPulsar
import java.util.*

/**
 * @author WireSegal
 * Created at 4:30 PM on 3/20/16.
 */
class BlockConjuredPulsar(name: String) : BlockModContainer(name, Material.GLASS, name) {

    override val item: ItemBlock?
        get() = null

    init {
        this.defaultState = this.makeDefaultState()
        this.setLightOpacity(0)
        this.disableStats()
        this.translucent = true
    }

    override fun canProvidePower(state: IBlockState?): Boolean {
        return true
    }

    override fun getWeakPower(blockState: IBlockState?, blockAccess: IBlockAccess?, pos: BlockPos?, side: EnumFacing?): Int {
        return 15
    }

    fun makeDefaultState(): IBlockState {
        return this.getStateFromMeta(0)
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, *this.allProperties)
    }

    override val ignoredProperties: Array<IProperty<*>>?
        get() = allProperties

    val allProperties: Array<IProperty<*>>
        get() = arrayOf(SOLID, LIGHT, BLOCK_UP, BLOCK_DOWN, BLOCK_NORTH, BLOCK_SOUTH, BLOCK_WEST, BLOCK_EAST)

    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isFullBlock(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    @SideOnly(Side.CLIENT)
    override fun getAmbientOcclusionLightValue(state: IBlockState): Float {
        return 1.0f
    }

    override fun quantityDropped(random: Random?): Int {
        return 0
    }

    override fun canSilkHarvest(world: World?, pos: BlockPos?, state: IBlockState, player: EntityPlayer?): Boolean {
        return false
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        val state = this.defaultState
        return state.withProperty(SOLID, meta and 1 > 0).withProperty(LIGHT, meta and 2 > 0)
    }

    override fun getMetaFromState(state: IBlockState?): Int {
        return if (state!!.getValue(SOLID)) 1 else 0 + if (state.getValue(LIGHT)) 2 else 0
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType {
        return EnumBlockRenderType.INVISIBLE
    }

    override fun getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos): IBlockState {
        var newState = state
        newState = newState.withProperty(BLOCK_UP, checkBlock(worldIn, pos.up(), state))
        newState = newState.withProperty(BLOCK_DOWN, checkBlock(worldIn, pos.down(), state))
        newState = newState.withProperty(BLOCK_NORTH, checkBlock(worldIn, pos.north(), state))
        newState = newState.withProperty(BLOCK_SOUTH, checkBlock(worldIn, pos.south(), state))
        newState = newState.withProperty(BLOCK_WEST, checkBlock(worldIn, pos.west(), state))
        newState = newState.withProperty(BLOCK_EAST, checkBlock(worldIn, pos.east(), state))
        return newState
    }

    fun checkBlock(w: IBlockAccess, p: BlockPos, state: IBlockState): Boolean {
        return w.getBlockState(p).block == state.block && w.getBlockState(p).getValue(SOLID)
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos): Int {
        return if (state.getValue(LIGHT)) 15 else 0
    }


    override fun addCollisionBoxToList(state: IBlockState, worldIn: World, pos: BlockPos, aabb: AxisAlignedBB, list: List<AxisAlignedBB>, entity: Entity?, something: Boolean) {
        if (state.getValue(SOLID)) {
            addCollisionBoxToList(pos, aabb, list, AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0))
        }

    }

    override fun getSelectedBoundingBox(state: IBlockState, world: World, pos: BlockPos): AxisAlignedBB {
        val solid = state.getValue(SOLID)
        val f = if (solid) 0.0f else 0.25f
        val minX = f.toDouble()
        val minY = f.toDouble()
        val minZ = f.toDouble()
        val maxX = (1.0f - f).toDouble()
        val maxY = (1.0f - f).toDouble()
        val maxZ = (1.0f - f).toDouble()
        return AxisAlignedBB(pos.x.toDouble() + minX, pos.y.toDouble() + minY, pos.z.toDouble() + minZ, pos.x.toDouble() + maxX, pos.y.toDouble() + maxY, pos.z.toDouble() + maxZ)
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity {
        return TileConjuredPulsar()
    }
}
