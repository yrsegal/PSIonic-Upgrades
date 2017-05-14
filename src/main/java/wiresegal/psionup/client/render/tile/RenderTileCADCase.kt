package wiresegal.psionup.client.render.tile

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.util.EnumFacing
import wiresegal.psionup.common.block.BlockCADCase
import wiresegal.psionup.common.block.ModBlocks
import wiresegal.psionup.common.block.tile.TileCADCase

/**
 * @author WireSegal
 * Created at 1:44 PM on 5/4/16.
 */
class RenderTileCADCase : TileEntitySpecialRenderer<TileCADCase>() {
    override fun renderTileEntityAt(te: TileCADCase, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int) {

        val state = te.world.getBlockState(te.pos)
        if (state.block != ModBlocks.cadCase || !state.getValue(BlockCADCase.OPEN)) return

        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.enableRescaleNormal()
        GlStateManager.translate(x, y, z)

        val facing = state.getValue(BlockCADCase.FACING)
        GlStateManager.rotate(-facing.horizontalAngle, 0F, 1F, 0F)
        if (facing == EnumFacing.NORTH)
            GlStateManager.translate(-1F, 0F, -1F)
        else if (facing == EnumFacing.WEST)
            GlStateManager.translate(0F, 0F, -1F)
        else if (facing == EnumFacing.EAST)
            GlStateManager.translate(-1F, 0F, 0F)

        GlStateManager.pushMatrix()
        GlStateManager.rotate(-90F, 1F, 0F, 0F)
        GlStateManager.scale(0.35, 0.35, 0.35)
        GlStateManager.translate(2.0F, -1.45F, 0.3F)
        Minecraft.getMinecraft().renderItem.renderItem(te.itemHandler.getStackInSlot(0), ItemCameraTransforms.TransformType.FIXED)
        GlStateManager.popMatrix()

        GlStateManager.rotate(-90F, 1F, 0F, 0F)
        GlStateManager.scale(0.35, 0.35, 0.35)
        GlStateManager.translate(0.75F, -1.45F, 0.3F)
        Minecraft.getMinecraft().renderItem.renderItem(te.itemHandler.getStackInSlot(1), ItemCameraTransforms.TransformType.FIXED)

        GlStateManager.color(1f, 1f, 1f)
        GlStateManager.disableBlend()
        GlStateManager.enableRescaleNormal()
        GlStateManager.popMatrix()
    }
}
