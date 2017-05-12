package wiresegal.psionup.common.effect

import wiresegal.psionup.common.effect.base.PotionPsiChange
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 10:01 PM on 7/13/16.
 */
class PotionPsishock : PotionPsiChange(LibNames.Potions.PSISHOCK, true, 0xFF4D12) {
    override val ampAmount: Int
        get() = -15
    override val baseAmount: Int
        get() = -30
}
