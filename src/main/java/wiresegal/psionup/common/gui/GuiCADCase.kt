package wiresegal.psionup.common.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import wiresegal.psionup.common.lib.LibMisc

class GuiCADCase(player: EntityPlayer, internal var stack: ItemStack) : GuiContainer(ContainerCADCase(player, stack)) {
    val inventory = InventoryCADCase(stack)

    val xOffset = 72
    val yOffset = 5 + 29

    override fun initGui() {
        xSize = 227
        ySize = 130
        super.initGui()
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f)
        mc.textureManager.bindTexture(textureMain)
        val x = (width - xSize) / 2
        val y = (height - ySize) / 2
        drawTexturedModalRect(x, y + yOffset, 0, 0, xSize, 96)

        mc.textureManager.bindTexture(textureCase)
        val caseX = (stack.itemDamage % 3) * 83
        val caseY = (stack.itemDamage / 3) * 29
        drawTexturedModalRect(x + xOffset, y, caseX, caseY, 83, 29)
    }

    companion object {
        private val textureMain = ResourceLocation(LibMisc.MOD_ID, "textures/gui/caseBase.png") // 227 x 96
        private val textureCase = ResourceLocation(LibMisc.MOD_ID, "textures/gui/cases.png") // 83 x 29 per
    }

}
