package wiresegal.psionup.common.network

import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 12:12 AM on 7/10/16.
 */
object NetworkHandler {
    val INSTANCE: SimpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(LibMisc.MOD_ID)
    var i = 0

    init {
        INSTANCE.registerMessage(MessageFlashSync.MessageFlashSyncHandler::class.java, MessageFlashSync::class.java, i++, Side.SERVER)
        INSTANCE.registerMessage(MessageFlowColorUpdate.MessageFlowColorHandler::class.java, MessageFlowColorUpdate::class.java, i++, Side.SERVER)
    }
}
