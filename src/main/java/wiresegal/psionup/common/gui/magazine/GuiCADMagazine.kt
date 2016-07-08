package wiresegal.psionup.common.gui.magazine

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import wiresegal.psionup.common.lib.LibMisc

class GuiCADMagazine(player: EntityPlayer, var stack: ItemStack) : GuiContainer(ContainerCADMagazine(player, stack)) {

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

    companion object {
        private val texture = ResourceLocation(LibMisc.MOD_ID, "textures/gui/magazine.png")
    }

}
