package wiresegal.psionup.client.core

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import shadowfox.botanicaladdons.client.core.ModelHandler
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.core.CommonProxy
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        ModelHandler.preInit(LibMisc.MOD_ID, PsionicUpgrades.DEV_ENVIRONMENT, PsionicUpgrades.LOGGER)
    }

    override fun init(e: FMLInitializationEvent) {
        super.init(e)
        ModelHandler.init()
    }
}
