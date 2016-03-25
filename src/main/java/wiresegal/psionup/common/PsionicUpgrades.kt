package wiresegal.psionup.common

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import wiresegal.psionup.common.command.CommandPsiLearn
import wiresegal.psionup.common.command.CommandPsiUnlearn
import wiresegal.psionup.common.core.CommonProxy

/**
 * @author WireSegal
 * Created at 8:29 AM on 3/20/16.
 */
@Mod(modid = "PSIonicUpgrades", name = "PSIonic Upgrades", version = "r1-1", dependencies = "required-after:Forge@[12.16.0.1809,);required-after:Psi[beta-29,);")
class PsionicUpgrades {
    companion object {
        @Mod.Instance("PSIonicUpgrades")
        lateinit var instance: PsionicUpgrades

        @SidedProxy(serverSide = "wiresegal.psionup.common.core.CommonProxy",
                clientSide = "wiresegal.psionup.client.core.ClientProxy")
        lateinit var proxy: CommonProxy
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        proxy.pre(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @Mod.EventHandler
    fun post(event: FMLPostInitializationEvent) {
        proxy.post(event)
    }

    @Mod.EventHandler
    fun serverStartingEvent(e: FMLServerStartingEvent) {
        e.registerServerCommand(CommandPsiLearn());
        e.registerServerCommand(CommandPsiUnlearn());
    }
}
