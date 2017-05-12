package wiresegal.psionup.common.core

import com.teamwizardry.librarianlib.features.config.ConfigPropertyBoolean
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import wiresegal.psionup.common.lib.LibMisc
import java.io.File

/**
 * @author WireSegal
 * Created at 1:09 AM on 7/10/16.
 */
object ConfigHandler {

    @ConfigPropertyBoolean(LibMisc.MOD_ID, "general", "inline.enabled",
            "Set this to false to prevent the Inline Caster from being crafted.", true)
    var enableInline = true

    @ConfigPropertyBoolean(LibMisc.MOD_ID, "general", "magazine.enabled",
            "Set this to false to prevent the Spell Magazine from being crafted.", true)
    var enableMagazine = true

    @ConfigPropertyBoolean(LibMisc.MOD_ID, "general", "case.enabled",
            "Set this to false to prevent the CAD Case from being crafted.", true)
    var enableCase = true

    @ConfigPropertyBoolean(LibMisc.MOD_ID, "general", "ring.enabled",
            "Set this to false to prevent the Flash Ring from being crafted.", true)
    var enableRing = true

    @ConfigPropertyBoolean(LibMisc.MOD_ID, "general", "psiRegenPotion.enabled",
            "Set this to false to remove the Psionic Pulse potion. The effect will still remain for /effect.", true)
    var enablePsionicPulse = true

}
