package wiresegal.psionup.client.core

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.psionup.common.core.CommonProxy

/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
        ModelHandler.preInit()
    }
}
