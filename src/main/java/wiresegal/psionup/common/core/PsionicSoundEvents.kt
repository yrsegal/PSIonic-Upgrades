package wiresegal.psionup.common.core

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 8:12 PM on 10/3/16.
 */
object PsionicSoundEvents {

    val THWACK: SoundEvent

    init {
        val loc = ResourceLocation(LibMisc.MOD_ID, "thwack")
        THWACK = SoundEvent(loc)
        GameRegistry.register(THWACK, loc)
    }
}
