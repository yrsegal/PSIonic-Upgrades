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
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import vazkii.psi.api.PsiAPI
import vazkii.psi.client.core.handler.ShaderHandler
import vazkii.psi.common.Psi
import wiresegal.psionup.client.core.PsionicClientMethodHandles
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.lib.LibMisc
import java.util.*

/**
 * @author WireSegal
 * Created at 4:46 PM on 7/28/16.
 */
@SideOnly(Side.CLIENT)
class LayerGlowingWire(val renderPlayer: RenderPlayer) : LayerRenderer<AbstractClientPlayer> {
    val modelElytra = ModelElytra()

    fun getTexture(player: AbstractClientPlayer): ResourceLocation {
        val itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS)
        if (itemstack == null || itemstack.item == ModItems.ebonyExosuitLegs)
            return TEXTURE_EBONY
        return TEXTURE_IVORY
    }

    override fun doRenderLayer(player: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        if (player.uniqueID != WIRE_UUID) return

        val cad = PsiAPI.getPlayerCAD(player)
        var r = 0f
        var g = 0f
        var b = 0f
        var hasCad = false
        if (cad != null) {
            val i = Psi.proxy.getCADColor(cad).rgb
            r = (i shr 16 and 255).toFloat() / 255.0f
            g = (i shr 8 and 255).toFloat() / 255.0f
            b = (i and 255).toFloat() / 255.0f
            hasCad = true
        }

        doCapeRender(player, partialTicks, r, g, b, hasCad)
        doElytraRender(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, r, g, b, hasCad)
        doSkinOverlayRender(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, r, b, g, hasCad)
    }

    fun doCapeRender(player: AbstractClientPlayer, partialTicks: Float, r: Float, g: Float, b: Float, hasCad: Boolean) {
        if (player.hasPlayerInfo() && !player.isInvisible && player.isWearing(EnumPlayerModelParts.CAPE) && player.locationCape != null) {
            val itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)

            if (itemstack == null || itemstack.item !== Items.ELYTRA) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
                this.renderPlayer.bindTexture(getTexture(player))
                GlStateManager.pushMatrix()
                GlStateManager.translate(0.0f, 0.0f, 0.125f)
                val d0 = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * partialTicks.toDouble() - (player.prevPosX + (player.posX - player.prevPosX) * partialTicks.toDouble())
                val d1 = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * partialTicks.toDouble() - (player.prevPosY + (player.posY - player.prevPosY) * partialTicks.toDouble())
                val d2 = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * partialTicks.toDouble() - (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks.toDouble())
                val f = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks
                val d3 = MathHelper.sin(f * 0.017453292f).toDouble()
                val d4 = (-MathHelper.cos(f * 0.017453292f)).toDouble()
                var f1 = d1.toFloat() * 10.0f
                f1 = MathHelper.clamp(f1, -6.0f, 32.0f)
                var f2 = (d0 * d3 + d2 * d4).toFloat() * 100.0f
                val f3 = (d0 * d4 - d2 * d3).toFloat() * 100.0f

                if (f2 < 0.0f) {
                    f2 = 0.0f
                }

                val f4 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks
                f1 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f4

                if (player.isSneaking) {
                    f1 += 25.0f
                }

                GlStateManager.rotate(6.0f + f2 / 2.0f + f1, 1.0f, 0.0f, 0.0f)
                GlStateManager.rotate(f3 / 2.0f, 0.0f, 0.0f, 1.0f)
                GlStateManager.rotate(-f3 / 2.0f, 0.0f, 1.0f, 0.0f)
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f)
                this.renderPlayer.mainModel.renderCape(0.0625f)

                if (hasCad) {
                    this.renderPlayer.bindTexture(TEXTURE_OVERLAY)
                    GlStateManager.disableLighting()
                    ShaderHandler.useShader(ShaderHandler.rawColor)
                    GlStateManager.color(r, g, b)
                    this.renderPlayer.mainModel.renderCape(0.0625f)
                    GlStateManager.color(1f, 1f, 1f)
                    GlStateManager.enableLighting()
                    ShaderHandler.releaseShader()
                }

                GlStateManager.popMatrix()
            }
        }
    }

    fun doElytraRender(player: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float, r: Float, g: Float, b: Float, hasCad: Boolean) {
        val itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)
        if (player.isWearing(EnumPlayerModelParts.CAPE) && itemstack != null && itemstack.item == Items.ELYTRA) {
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

            if (hasCad) {
                this.renderPlayer.bindTexture(TEXTURE_OVERLAY)
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

    fun doSkinOverlayRender(player: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float, r: Float, b: Float, g: Float, hasCad: Boolean) {
        if (hasCad) {
            GlStateManager.pushMatrix()
            GlStateManager.disableLighting()
            ShaderHandler.useShader(ShaderHandler.rawColor)
            GlStateManager.color(r, g, b)
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN)

            this.renderPlayer.bindTexture(TEXTURE_SKIN_OVERLAY)
            PsionicClientMethodHandles.setModelVisibilities(renderPlayer, player)

            renderPlayer.mainModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)

            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN)
            GlStateManager.color(1F, 1F, 1F)
            ShaderHandler.releaseShader()
            GlStateManager.enableLighting()
            GlStateManager.popMatrix()
        }
    }

    override fun shouldCombineTextures(): Boolean {
        return false
    }

    companion object {
        private val WIRE_UUID = UUID.fromString("458391f5-6303-4649-b416-e4c0d18f837a")

        private val TEXTURE_EBONY = ResourceLocation(LibMisc.MOD_ID, "textures/model/wire/ebonyCape2015.png")
        private val TEXTURE_IVORY = ResourceLocation(LibMisc.MOD_ID, "textures/model/wire/ivoryCape2015.png")
        private val TEXTURE_OVERLAY = ResourceLocation(LibMisc.MOD_ID, "textures/model/wire/cape2015Overlay.png")
        private val TEXTURE_SKIN_OVERLAY = ResourceLocation(LibMisc.MOD_ID, "textures/model/wire/skinOverlay.png")
    }
}
