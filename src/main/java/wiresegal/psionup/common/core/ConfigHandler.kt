package wiresegal.psionup.common.core

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import vazkii.botania.common.lib.LibMisc
import java.io.File

/**
 * @author WireSegal
 * Created at 1:09 AM on 7/10/16.
 */
object ConfigHandler {
    class ChangeListener {
        @SubscribeEvent
        fun onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent) {
            if (eventArgs.modID == LibMisc.MOD_ID)
                load()
        }
    }

    var enableInline = true
    var enableMagazine = true
    var enableCase = true
    var enableRing = true

    lateinit var config: Configuration

    fun loadConfig(configFile: File) {
        config = Configuration(configFile)

        config.load()
        load()

        MinecraftForge.EVENT_BUS.register(ChangeListener())
    }

    fun load() {
        var desc = "Set this to false to prevent the Inline Caster from being crafted."
        enableInline = loadPropBool("inline.enabled", desc, enableInline)

        desc = "Set this to false to prevent the Spell Magazine from being crafted."
        enableMagazine = loadPropBool("magazine.enabled", desc, enableMagazine)

        desc = "Set this to false to prevent the CAD Case from being crafted."
        enableCase = loadPropBool("case.enabled", desc, enableCase)

        desc = "Set this to false to prevent the Flash Ring from being crafted."
        enableRing = loadPropBool("ring.enabled", desc, enableRing)

        if (config.hasChanged())
            config.save()
    }

    fun loadPropBool(propName: String, desc: String, default_: Boolean): Boolean {
        val prop = config.get(Configuration.CATEGORY_GENERAL, propName, default_)
        prop.comment = desc

        return prop.getBoolean(default_)
    }

}
