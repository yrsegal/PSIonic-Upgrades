package wiresegal.psionup.client.core

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.entity.layers.LayerElytra
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.psionup.client.core.handler.HUDHandler
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.client.render.entity.ExosuitGlowLayer
import wiresegal.psionup.client.render.entity.GlowingItemHandler
import wiresegal.psionup.client.render.entity.LayerGlowingWire
import wiresegal.psionup.client.render.tile.RenderTileCADCase
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.block.tile.TileCADCase
import wiresegal.psionup.common.core.CommonProxy
import wiresegal.psionup.common.lib.LibMisc
import java.util.*

/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        HUDHandler
        ModelHandler.preInit(LibMisc.MOD_ID, PsionicUpgrades.DEV_ENVIRONMENT, PsionicUpgrades.LOGGER)

        GlowingItemHandler.EventHandler()
    }

    override fun init(e: FMLInitializationEvent) {
        super.init(e)
        ModelHandler.init()
        ClientRegistry.bindTileEntitySpecialRenderer(TileCADCase::class.java, RenderTileCADCase())

        val skinMap = Minecraft.getMinecraft().renderManager.skinMap
        var render = skinMap["default"]
        render?.let {
            val renders = PsionicClientMethodHandles.getRenderLayers(it)
            it.addLayer(ExosuitGlowLayer(it))
            it.addLayer(GlowingItemHandler.GlowingItemLayer(it))
            it.addLayer(LayerGlowingWire(it))
            for ((index, layer) in renders.withIndex())
                if (layer is LayerElytra)
                    renders[index] = LayerWireOccludeElytra(layer)

        }

        render = skinMap["slim"]
        render?.let {
            val renders = PsionicClientMethodHandles.getRenderLayers(it)
            it.addLayer(ExosuitGlowLayer(it))
            it.addLayer(GlowingItemHandler.GlowingItemLayer(it))
            it.addLayer(LayerGlowingWire(it))
            for ((index, layer) in renders.withIndex())
                if (layer is LayerElytra)
                    renders[index] = LayerWireOccludeElytra(layer)

        }
    }

    class LayerWireOccludeElytra(val elytra: LayerElytra) : LayerRenderer<AbstractClientPlayer> {

        val WIRE_UUID: UUID = UUID.fromString("458391f5-6303-4649-b416-e4c0d18f837a")

        override fun shouldCombineTextures() = false
        override fun doRenderLayer(player: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
            if (player.uniqueID != WIRE_UUID || !player.isWearing(EnumPlayerModelParts.CAPE))
                elytra.doRenderLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale)
        }
    }
}
