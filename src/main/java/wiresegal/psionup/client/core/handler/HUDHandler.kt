package wiresegal.psionup.client.core.handler

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.ARBMultitexture
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.GL11
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.client.core.handler.ShaderHandler
import vazkii.psi.common.core.handler.ConfigHandler
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.lib.LibResources
import wiresegal.psionup.common.items.ItemGaussRifle
import wiresegal.psionup.common.items.spell.ItemFlashRing
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:44 AM on 7/10/16.
 */
object HUDHandler {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun onDraw(event: RenderGameOverlayEvent.Post) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            drawPsiBar(event.resolution, event.partialTicks)
        }
    }

    private val psiBar = ResourceLocation(LibResources.GUI_PSI_BAR)
    private val psiBarMask = ResourceLocation(LibResources.GUI_PSI_BAR_MASK)
    private val psiBarShatter = ResourceLocation(LibResources.GUI_PSI_BAR_SHATTER)

    private val secondaryTextureUnit = 7
    private var registeredMask = false

    @SideOnly(Side.CLIENT)
    fun drawPsiBar(res: ScaledResolution, pticks: Float) {
        var resoultion = res
        val mc = Minecraft.getMinecraft()
        val cadStack = PsiAPI.getPlayerCAD(mc.player)
        if (cadStack.isEmpty) return

        val cad = cadStack.item as ICAD
        val data = PlayerDataHandler.get(mc.player)
        if (data.level == 0 && !mc.player.capabilities.isCreativeMode)
            return

        val totalPsi = data.totalPsi
        val currPsi = data.getAvailablePsi()

        val mainHand = mc.player.heldItemMainhand
        val offHand = mc.player.heldItemOffhand

        if ((currPsi == totalPsi &&
                (mainHand.isEmpty || mainHand.item is ISocketable) &&
                (offHand.isEmpty || offHand.item is ISocketable)))
            return

        if ((mainHand.isEmpty || (mainHand.item !is ItemFlashRing && mainHand.item !is ItemGaussRifle)) &&
                (offHand.isEmpty || (offHand.item !is ItemFlashRing && offHand.item !is ItemGaussRifle)))
            return

        GlStateManager.pushMatrix()
        val scaleFactor = resoultion.scaleFactor

        if (scaleFactor > ConfigHandler.maxPsiBarScale) {
            val guiScale = mc.gameSettings.guiScale

            mc.gameSettings.guiScale = ConfigHandler.maxPsiBarScale
            resoultion = ScaledResolution(mc)
            mc.gameSettings.guiScale = guiScale

            val s = ConfigHandler.maxPsiBarScale.toFloat() / scaleFactor.toFloat()
            GlStateManager.scale(s, s, s)
        }

        val right = ConfigHandler.psiBarOnRight

        val pad = 3
        var width = 32
        var height = 140

        var x = -pad
        if (right)
            x = resoultion.scaledWidth + pad - width
        var y = resoultion.scaledHeight / 2 - height / 2

        if (!registeredMask) {
            mc.renderEngine.bindTexture(psiBarMask)
            mc.renderEngine.bindTexture(psiBarShatter)
            registeredMask = true
        }
        mc.renderEngine.bindTexture(psiBar)
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, width, height, 64f, 256f)

        x += 8
        y += 26

        width = 16
        height = 106

        val r = 0.6f
        val g = 0.65f
        val b = 1f

        val origHeight = height
        val origY = y
        var v = 0
        val max = totalPsi

        var texture = 0
        val shaders = ShaderHandler.useShaders()

        if (shaders) {
            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit)
            texture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
        }

        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        for (d in data.deductions) {
            val a = d.getPercentile(pticks)
            GlStateManager.color(r, g, b, a)
            height = Math.ceil(origHeight * d.deduct.toDouble() / max).toInt()
            val effHeight = (origHeight * d.current.toDouble() / max).toInt()
            v = origHeight - effHeight
            y = origY + v

            ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(a, d.shatter))
            Gui.drawModalRectWithCustomSizedTexture(x, y, 32f, v.toFloat(), width, height, 64f, 256f)
        }

        var textY = origY.toFloat()
        if (max > 0) {
            height = (origHeight.toDouble() * data.availablePsi.toDouble() / max).toInt()
            v = origHeight - height
            y = origY + v

            if (data.availablePsi != data.lastAvailablePsi) {
                val textHeight = (origHeight * (data.availablePsi * pticks + data.lastAvailablePsi * (1.0 - pticks)) / max).toFloat()
                textY = origY + (origHeight - textHeight)
            } else
                textY = y.toFloat()
        } else
            height = 0

        GlStateManager.color(r, g, b)
        ShaderHandler.useShader(ShaderHandler.psiBar, generateCallback(1f, false))
        Gui.drawModalRectWithCustomSizedTexture(x, y, 32f, v.toFloat(), width, height, 64f, 256f)
        ShaderHandler.releaseShader()

        if (shaders) {
            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB)
        }

        GlStateManager.color(1f, 1f, 1f)

        GlStateManager.pushMatrix()
        GlStateManager.translate(0f, textY, 0f)
        width = 44
        height = 3

        val s1 = "" + data.availablePsi
        val s2 = "" + cad.getStoredPsi(cadStack)

        var offBar = 22
        var offStr1 = 7 + mc.fontRenderer.getStringWidth(s1)
        var offStr2 = 7 + mc.fontRenderer.getStringWidth(s2)

        if (!right) {
            offBar = 6
            offStr1 = -23
            offStr2 = -23
        }

        val color = Color(cad.getSpellColor(cadStack))
        GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f)

        Gui.drawModalRectWithCustomSizedTexture(x - offBar, -2, 0f, 140f, width, height, 64f, 256f)
        mc.fontRenderer.drawStringWithShadow(s1, x - offStr1.toFloat(), -11f, 0xFFFFFF)
        GlStateManager.popMatrix()

        GlStateManager.pushMatrix()
        GlStateManager.translate(0f, Math.max(textY + 3, (origY + 100).toFloat()), 0f)
        mc.fontRenderer.drawStringWithShadow(s2, x - offStr2.toFloat(), 0f, 0xFFFFFF)
        GlStateManager.popMatrix()
        GlStateManager.popMatrix()
    }

    @SideOnly(Side.CLIENT)
    private fun generateCallback(percentile: Float, shatter: Boolean): (Int) -> Unit {
        val mc = Minecraft.getMinecraft()
        return { shader: Int ->
            val percentileUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "percentile")
            val imageUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "image")
            val maskUniform = ARBShaderObjects.glGetUniformLocationARB(shader, "mask")

            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(psiBar).glTextureId)
            ARBShaderObjects.glUniform1iARB(imageUniform, 0)

            OpenGlHelper.setActiveTexture(ARBMultitexture.GL_TEXTURE0_ARB + secondaryTextureUnit)

            GlStateManager.enableTexture2D()
            GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(if (shatter) psiBarShatter else psiBarMask).glTextureId)
            ARBShaderObjects.glUniform1iARB(maskUniform, secondaryTextureUnit)

            ARBShaderObjects.glUniform1fARB(percentileUniform, percentile)
        }
    }
}
