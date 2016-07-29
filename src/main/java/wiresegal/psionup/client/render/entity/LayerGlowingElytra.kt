package wiresegal.psionup.client.render.entity

import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.model.ModelElytra
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.entity.layers.LayerArmorBase
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.init.Items
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import vazkii.psi.api.PsiAPI
import vazkii.psi.client.core.handler.ShaderHandler
import vazkii.psi.common.Psi
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.lib.LibMisc
import java.util.*

/**
 * @author WireSegal
 * Created at 4:38 PM on 7/28/16.
 */
@SideOnly(Side.CLIENT)
class LayerGlowingElytra(val renderPlayer: RenderPlayer) : LayerRenderer<AbstractClientPlayer> {
    val modelElytra = ModelElytra()

    fun getTexture(player: AbstractClientPlayer): ResourceLocation {
        val itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS)
        if (itemstack?.item == ModItems.ebonyExosuitLegs)
            return TEXTURE_EBONY
        return TEXTURE_IVORY
    }

    override fun doRenderLayer(player: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        val itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)

        if (player.uniqueID != WIRE_UUID || !player.isWearing(EnumPlayerModelParts.CAPE)) return

        if (itemstack != null && itemstack.item == Items.ELYTRA) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            GlStateManager.enableBlend()
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            this.renderPlayer.bindTexture(getTexture(player))

            GlStateManager.pushMatrix()
            GlStateManager.translate(0.0f, 0.0f, 0.125f)
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, player)
            this.modelElytra.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)

            if (itemstack.isItemEnchanted)
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, player, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale)

            this.renderPlayer.bindTexture(TEXTURE_OVERLAY)

            val cad = PsiAPI.getPlayerCAD(player)
            if (cad != null) {
                val i = Psi.proxy.getCADColor(cad).rgb
                val r = (i shr 16 and 255).toFloat() / 255.0f
                val g = (i shr 8 and 255).toFloat() / 255.0f
                val b = (i and 255).toFloat() / 255.0f

                GlStateManager.disableLighting()
                ShaderHandler.useShader(ShaderHandler.rawColor)
                GlStateManager.color(r, g, b)
                this.modelElytra.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
                GlStateManager.color(1f, 1f, 1f)
                GlStateManager.enableLighting()
                ShaderHandler.releaseShader()
            }

            GlStateManager.disableBlend()

            GlStateManager.popMatrix()
        }
    }

    override fun shouldCombineTextures(): Boolean {
        return false
    }

    companion object {
        private val WIRE_UUID = UUID.fromString("458391f5-6303-4649-b416-e4c0d18f837a")

        private val TEXTURE_EBONY = ResourceLocation(LibMisc.MOD_ID, "textures/model/ebonyCape2015.png")
        private val TEXTURE_IVORY = ResourceLocation(LibMisc.MOD_ID, "textures/model/ivoryCape2015.png")
        private val TEXTURE_OVERLAY = ResourceLocation(LibMisc.MOD_ID, "textures/model/cape2015Overlay.png")
    }
}
