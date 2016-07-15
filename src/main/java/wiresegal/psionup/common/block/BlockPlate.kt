package wiresegal.psionup.common.block

import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.EnumDyeColor
import net.minecraft.util.BlockRenderLayer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.common.block.base.BlockMod
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 9:56 PM on 7/4/16.
 */
class BlockPlate(name: String) : BlockMod(name, Material.IRON, *makeVariants(name)), ModelHandler.IBlockColorProvider {
    companion object {
        fun makeVariants(name: String): Array<String> {
            return Array(16) {
                name + LibNames.Colors[it]
            }
        }

        val COLOR = PropertyEnum.create("color", EnumDyeColor::class.java)
    }

    override fun getBlockLayer(): BlockRenderLayer? {
        return BlockRenderLayer.CUTOUT_MIPPED
    }

    init {
        setLightLevel(1f)
        this.setHardness(5.0f)
        this.setResistance(10.0f)
        this.soundType = SoundType.METAL
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, COLOR)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(COLOR).metadata
    }

    override fun getStateFromMeta(meta: Int): IBlockState? {
        return defaultState.withProperty(COLOR, EnumDyeColor.byMetadata(meta))
    }

    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(state)
    }

    override fun getMapColor(state: IBlockState): MapColor {
        return state.getValue(COLOR).mapColor
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockColor(): IBlockColor? {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> if (i == 1) iBlockState.getValue(COLOR).mapColor.colorValue else 0xFFFFFF }
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor? {
        return IItemColor { itemStack, i -> EnumDyeColor.byMetadata(itemStack.itemDamage).mapColor.colorValue }
    }

}
