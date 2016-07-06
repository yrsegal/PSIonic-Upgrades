package wiresegal.psionup.common.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextFormatting
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICAD
import vazkii.psi.common.block.base.ModBlocks
import vazkii.psi.common.block.tile.TileCADAssembler
import vazkii.psi.common.block.tile.container.ContainerCADAssembler
import vazkii.psi.common.lib.LibResources

class GuiCADCase(player: EntityPlayer, internal var stack: ItemStack) : GuiContainer(ContainerCADCase(player, stack)) {
    val inventory = InventoryCADCase(stack)

    override fun initGui() {
        xSize = 256
        ySize = 225
        super.initGui()
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f)
        mc.textureManager.bindTexture(texture)
        val x = (width - xSize) / 2
        val y = (height - ySize) / 2
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize)
    }

    companion object {
        private val texture = ResourceLocation(LibResources.GUI_CAD_ASSEMBLER) //todo
    }

}
