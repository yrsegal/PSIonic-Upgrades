package wiresegal.psionup.client.core

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.common.Psi
import wiresegal.psionup.client.core.handler.HUDHandler
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.client.render.entity.ExosuitGlowLayer
import wiresegal.psionup.client.render.entity.GlowingItemHandler
import wiresegal.psionup.client.render.tile.RenderTileCADCase
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.block.tile.TileCADCase
import wiresegal.psionup.common.core.CommonProxy
import wiresegal.psionup.common.lib.LibMisc

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
        render?.addLayer(ExosuitGlowLayer(render))
        render?.addLayer(GlowingItemHandler.GlowingItemLayer(render))

        render = skinMap["slim"]
        render?.addLayer(ExosuitGlowLayer(render))
        render?.addLayer(GlowingItemHandler.GlowingItemLayer(render))
    }
}
