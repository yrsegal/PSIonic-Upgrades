package wiresegal.psionup.common.effect.botania.brew

import net.minecraft.potion.PotionEffect
import vazkii.botania.api.brew.Brew
import wiresegal.psionup.common.effect.ModPotions
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 6:10 PM on 7/15/16.
 */
object ModBrews {
    val psishock: Brew

    init {
        psishock = BrewMod(LibNames.Potions.PSISHOCK, 16000, PotionEffect(ModPotions.psishock, 300)).setNotBloodPendantInfusable()
    }
}
