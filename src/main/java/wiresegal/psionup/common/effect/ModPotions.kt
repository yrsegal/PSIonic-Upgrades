package wiresegal.psionup.common.effect

import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionType
import wiresegal.psionup.common.core.ConfigHandler
import wiresegal.psionup.common.effect.base.PotionMod
import wiresegal.psionup.common.effect.base.PotionTypeMod
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 10:06 PM on 7/13/16.
 */
object ModPotions {
    private var index = 0

    val psishock: PotionMod
    val psipulse: PotionMod

    val psishockType: PotionType
    val strongPsishockType: PotionType
    val longPsishockType: PotionType

    lateinit var psipulseType: PotionType
    lateinit var strongPsipulseType: PotionType
    lateinit var longPsipulseType: PotionType

    init {
        psishock = PotionPsishock(index++)
        psipulse = PotionPsipulse(index++)

        psishockType = PotionTypeMod(LibNames.Potions.PSISHOCK, PotionEffect(psishock, 160))
        strongPsishockType = PotionTypeMod(LibNames.Potions.PSISHOCK_STRONG, PotionEffect(psishock, 125, 1))
        longPsishockType = PotionTypeMod(LibNames.Potions.PSISHOCK_LONG, PotionEffect(psishock, 240))

        if (ConfigHandler.enablePsionicPulse) {
            psipulseType = PotionTypeMod(LibNames.Potions.PSIPULSE, PotionEffect(psipulse, 400))
            strongPsipulseType = PotionTypeMod(LibNames.Potions.PSIPULSE_STRONG, PotionEffect(psipulse, 250, 1))
            longPsipulseType = PotionTypeMod(LibNames.Potions.PSIPULSE_LONG, PotionEffect(psipulse, 600))
        }
    }
}
