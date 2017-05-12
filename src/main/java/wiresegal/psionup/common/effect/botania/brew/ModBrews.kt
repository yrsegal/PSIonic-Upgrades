package wiresegal.psionup.common.effect.botania.brew

import net.minecraft.potion.PotionEffect
import vazkii.botania.api.brew.Brew
import vazkii.botania.common.brew.BrewMod
import wiresegal.psionup.common.core.ConfigHandler
import wiresegal.psionup.common.effect.ModPotions
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 6:10 PM on 7/15/16.
 */
object ModBrews {
    val psishock: Brew = BrewMod(LibNames.Potions.PSISHOCK, 16000, PotionEffect(ModPotions.psishock, 300)).setNotBloodPendantInfusable()
    lateinit var psipulse: Brew

    init {
        if (ConfigHandler.enablePsionicPulse)
            psipulse = BrewMod(LibNames.Potions.PSIPULSE, 32000, PotionEffect(ModPotions.psipulse, 600)).setNotBloodPendantInfusable().setNotIncenseInfusable()
    }
}
