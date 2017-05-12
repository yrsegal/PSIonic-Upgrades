package wiresegal.psionup.common.block.spell

import com.teamwizardry.librarianlib.features.base.block.BlockModContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import wiresegal.psionup.common.block.tile.TileCracklingStar

/**
 * @author WireSegal
 * Created at 4:30 PM on 3/20/16.
 */
class BlockConjuredStar(name: String) : BlockModContainer(name, Material.GLASS, name) {

    override fun createItemForm() = null

    private val AABB = AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)

    init {
        setLightLevel(1f)
        soundType = SoundType.CLOTH
    }

    override fun getRenderType(state: IBlockState?): EnumBlockRenderType? {
        return EnumBlockRenderType.INVISIBLE
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileCracklingStar()
    }

    override fun isFullBlock(state: IBlockState?) = false
    override fun isBlockSolid(worldIn: IBlockAccess?, pos: BlockPos?, side: EnumFacing?) = false
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false
    override fun isPassable(worldIn: IBlockAccess?, pos: BlockPos?) = true
    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = AABB
    override fun getCollisionBoundingBox(blockState: IBlockState?, worldIn: IBlockAccess?, pos: BlockPos?) = NULL_AABB
    override fun canSpawnInBlock(): Boolean = true
    override fun isReplaceable(worldIn: IBlockAccess?, pos: BlockPos?) = false
}
