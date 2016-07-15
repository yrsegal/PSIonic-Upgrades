package wiresegal.psionup.common.effect

import wiresegal.psionup.common.effect.base.PotionMod

/**
 * @author WireSegal
 * Created at 10:06 PM on 7/13/16.
 */
object ModPotions {
    private var index = 0

    val psishock: PotionMod

    init {
        psishock = PotionPsishock(index++)
    }
}
