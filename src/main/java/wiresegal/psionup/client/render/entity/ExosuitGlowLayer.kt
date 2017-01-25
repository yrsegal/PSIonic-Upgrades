package wiresegal.psionup.client.render.entity

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderLivingBase
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.util.ResourceLocation
import vazkii.psi.client.core.handler.ShaderHandler
import wiresegal.psionup.common.items.spell.ItemFlowExosuit
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 5:21 PM on 7/9/16.
 */
class ExosuitGlowLayer(val renderer: RenderLivingBase<*>) : LayerBipedArmor(renderer) {
    override fun doRenderLayer(entitylivingbaseIn: EntityLivingBase, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST)
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS)
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET)
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD)
    }

    val RESOURCE = ResourceLocation(LibMisc.MOD_ID, "textures/model/exosuitOverlay.png")

    fun renderArmorLayer(entityLivingBaseIn: EntityLivingBase, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float, slotIn: EntityEquipmentSlot) {
        val itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn)

        if (itemstack != null && itemstack.item is ItemFlowExosuit) {
            val itemarmor = itemstack.item as ItemFlowExosuit

            if (itemarmor.equipmentSlot == slotIn) {
                var t = this.getModelFromSlot(slotIn)
                t = getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t)
                t.setModelAttributes(this.renderer.mainModel)
                t.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks)
                this.setModelSlotVisible(t, slotIn)

                this.renderer.bindTexture(RESOURCE)
                val i = if (entityLivingBaseIn is EntityPlayer) itemarmor.getColorFromPlayer(entityLivingBaseIn) else return
                if (i == 0) return
                val f = (i shr 16 and 255).toFloat() / 255.0f
                val f1 = (i shr 8 and 255).toFloat() / 255.0f
                val f2 = (i and 255).toFloat() / 255.0f
                GlStateManager.disableLighting()
                ShaderHandler.useShader(ShaderHandler.rawColor)
                GlStateManager.color(f, f1, f2, 1f)
                t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale)
                GlStateManager.enableLighting()
                ShaderHandler.releaseShader()
                GlStateManager.color(1f, 1f, 1f, 1f)
            }
        }
    }
}
