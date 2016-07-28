package wiresegal.psionup.client.render.entity

import com.google.common.base.Objects
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType
import net.minecraft.client.renderer.entity.RenderLivingBase
import net.minecraft.client.renderer.entity.layers.LayerHeldItem
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.client.core.handler.ShaderHandler
import vazkii.psi.common.core.helper.ItemNBTHelper
import wiresegal.psionup.client.core.PsionicClientMethodHandles

/**
 * @author WireSegal
 * Created at 8:55 PM on 7/27/16.
 */
class GlowingItemHandler {

    interface IOverlayable {
        companion object {
            val TAG_OVERLAY = "overlay"

            fun overlayStackOf(itemStack: ItemStack): ItemStack {
                val ret = itemStack.copy()
                ItemNBTHelper.setBoolean(ret, TAG_OVERLAY, true)
                EnchantmentHelper.setEnchantments(mapOf(), ret)
                return ret
            }
        }
    }

    @SideOnly(Side.CLIENT)
    class EventHandler {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent(receiveCanceled = true)
        fun onRenderHand(e: RenderHandEvent) {
            val entity = Minecraft.getMinecraft().renderViewEntity
            val flag = entity is EntityLivingBase && entity.isPlayerSleeping


            val render = Minecraft.getMinecraft().entityRenderer

            val stackMain = PsionicClientMethodHandles.getStackMainHand(render.itemRenderer)
            val stackOff = PsionicClientMethodHandles.getStackOffHand(render.itemRenderer)
            if (stackMain?.item !is IOverlayable && stackOff?.item !is IOverlayable) return
            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && !flag && !Minecraft.getMinecraft().gameSettings.hideGUI && !Minecraft.getMinecraft().playerController!!.isSpectator) {

                GlStateManager.pushMatrix()
                render.enableLightmap()
                renderOverlayItemsInFirstPerson(e.partialTicks, false)
                render.disableLightmap()
                GlStateManager.popMatrix()

                renderOverlayItemsInFirstPerson(e.partialTicks, true)
            }

            e.isCanceled = true
        }

        fun renderOverlayItemsInFirstPerson(partialTicks: Float, overlay: Boolean) {
            val render = Minecraft.getMinecraft().itemRenderer

            val abstractclientplayer = Minecraft.getMinecraft().thePlayer
            val f = abstractclientplayer.getSwingProgress(partialTicks)
            val f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks
            val f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks
            var flag = true
            var flag1 = true

            if (abstractclientplayer.isHandActive) {
                val itemstack = abstractclientplayer.activeItemStack

                if (itemstack != null && itemstack.item === Items.BOW) {
                    val enumhand1 = abstractclientplayer.activeHand
                    flag = enumhand1 == EnumHand.MAIN_HAND
                    flag1 = !flag
                }
            }

            rotateArroundXAndY(f1, f2)
            setLightmap()
            rotateArm(partialTicks)
            GlStateManager.enableRescaleNormal()

            val prevProgMain = PsionicClientMethodHandles.getPrevEquipMainHand(render)
            val prevProgOff = PsionicClientMethodHandles.getPrevEquipOffHand(render)
            val progMain = PsionicClientMethodHandles.getEquipMainHand(render)
            val progOff = PsionicClientMethodHandles.getEquipOffHand(render)
            val stackMain = PsionicClientMethodHandles.getStackMainHand(render)
            val stackOff = PsionicClientMethodHandles.getStackOffHand(render)

            if (overlay) {
                GlStateManager.disableLighting()
                ShaderHandler.useShader(ShaderHandler.rawColor)
            }
            if (flag && (stackMain?.item is IOverlayable || !overlay)) {
                val f3 = if (Objects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND) == EnumHand.MAIN_HAND) f else 0F
                val f5 = 1F - (prevProgMain + (progMain - prevProgMain) * partialTicks)
                render.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, if (overlay) IOverlayable.overlayStackOf(stackMain!!) else stackMain, f5)
            }

            if (flag1 && (stackOff?.item is IOverlayable || !overlay)) {
                val f4 = if (Objects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND) == EnumHand.OFF_HAND) f else 0F
                val f6 = 1F - (prevProgOff + (progOff - prevProgOff) * partialTicks)
                render.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, if (overlay) IOverlayable.overlayStackOf(stackOff!!) else stackOff, f6)
            }
            if (overlay) {
                ShaderHandler.releaseShader()
                GlStateManager.enableLighting()
            }
            
            GlStateManager.disableRescaleNormal()
            RenderHelper.disableStandardItemLighting()
        }

        private fun rotateArroundXAndY(angle: Float, angleY: Float) {
            GlStateManager.pushMatrix()
            GlStateManager.rotate(angle, 1F, 0F, 0F)
            GlStateManager.rotate(angleY, 0F, 1F, 0F)
            RenderHelper.enableStandardItemLighting()
            GlStateManager.popMatrix()
        }

        private fun setLightmap() {
            val abstractclientplayer = Minecraft.getMinecraft().thePlayer
            val i = Minecraft.getMinecraft().theWorld.getCombinedLight(BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + abstractclientplayer.getEyeHeight().toDouble(), abstractclientplayer.posZ), 0)
            val f = (i and 65535).toFloat()
            val f1 = (i shr 16).toFloat()
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1)
        }

        private fun rotateArm(partTicks: Float) {
            val entityplayersp = Minecraft.getMinecraft().thePlayer
            val f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * partTicks
            val f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * partTicks
            GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1F, 1F, 0F, 0F)
            GlStateManager.rotate((entityplayersp.rotationYaw - f1) * 0.1F, 0F, 1F, 0F)
        }
    }

    @SideOnly(Side.CLIENT)
    class GlowingItemLayer(val render: RenderLivingBase<*>) : LayerRenderer<EntityLivingBase> {

        override fun doRenderLayer(entitylivingbaseIn: EntityLivingBase?, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
            val flag = (entitylivingbaseIn ?: return).primaryHand == EnumHandSide.RIGHT
            val itemstack = if (flag) entitylivingbaseIn.heldItemOffhand else entitylivingbaseIn.heldItemMainhand
            val itemstack1 = if (flag) entitylivingbaseIn.heldItemMainhand else entitylivingbaseIn.heldItemOffhand

            if (itemstack != null || itemstack1 != null) {
                GlStateManager.pushMatrix()

                if (this.render.mainModel.isChild) {
                    GlStateManager.translate(0F, 0.625F, 0F)
                    GlStateManager.rotate(-20F, -1F, 0F, 0F)
                    GlStateManager.scale(0.5F, 0.5F, 0.5F)
                }

                GlStateManager.disableLighting()
                ShaderHandler.useShader(ShaderHandler.rawColor)
                this.renderHeldItem(entitylivingbaseIn, itemstack1, TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT)
                this.renderHeldItem(entitylivingbaseIn, itemstack, TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT)
                ShaderHandler.releaseShader()
                GlStateManager.enableLighting()
                GlStateManager.popMatrix()
            }
        }

        private fun renderHeldItem(entity: EntityLivingBase, stack: ItemStack?, transform: TransformType, handSide: EnumHandSide) {
            if (stack == null || stack.item !is IOverlayable) return
            GlStateManager.pushMatrix()

            if (entity.isSneaking)
                GlStateManager.translate(0F, 0.2F, 0F)
            (this.render.mainModel as ModelBiped).postRenderArm(0.0625F, handSide)
            GlStateManager.rotate(-90F, 1F, 0F, 0F)
            GlStateManager.rotate(180F, 0F, 1F, 0F)
            val flag = handSide == EnumHandSide.LEFT
            GlStateManager.translate((if (flag) -1 else 1) / 16F, 0.125F, -0.625F)
            Minecraft.getMinecraft().itemRenderer.renderItemSide(entity, IOverlayable.overlayStackOf(stack), transform, flag)
            GlStateManager.popMatrix()
        }


        override fun shouldCombineTextures() = false
    }
}
