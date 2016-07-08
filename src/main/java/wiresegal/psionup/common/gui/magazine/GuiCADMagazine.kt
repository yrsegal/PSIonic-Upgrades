package wiresegal.psionup.common.gui.magazine

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.fml.client.config.GuiUtils
import vazkii.psi.client.core.handler.ClientTickHandler
import wiresegal.psionup.common.lib.LibMisc

class GuiCADMagazine(player: EntityPlayer, var stack: ItemStack) : GuiContainer(ContainerCADMagazine(player, stack)) {

    var tooltipTime: Int
        get() = (inventorySlots as ContainerCADMagazine).tooltipTime
        set(value) {
            (inventorySlots as ContainerCADMagazine).tooltipTime = value
        }

    val tooltipText: String
        get() = (inventorySlots as ContainerCADMagazine).tooltipText

    var lastTick = 0

    override fun initGui() {
        xSize = 194
        ySize = 192
        super.initGui()
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f)
        mc.textureManager.bindTexture(texture)
        val x = (width - xSize) / 2
        val y = (height - ySize) / 2
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize)

        for (i in inventorySlots.inventorySlots) if (i is ContainerCADMagazine.SlotBullet && !i.isSlotEnabled())
            drawTexturedModalRect(i.xDisplayPosition + x, i.yDisplayPosition + y, 16, 224 + if (i.dark) 16 else 0, 16, 16)
    }


    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        if (tooltipTime != 0) {
            GuiUtils.drawHoveringText(listOf(TextFormatting.RED.toString() + I18n.format(tooltipText)), -10, ySize / 2, width, height, xSize, Minecraft.getMinecraft().fontRendererObj)
            tooltipTime -= ClientTickHandler.ticksInGame - lastTick
        }
        lastTick = ClientTickHandler.ticksInGame

    }

    companion object {
        private val texture = ResourceLocation(LibMisc.MOD_ID, "textures/gui/magazine.png")
    }

}
