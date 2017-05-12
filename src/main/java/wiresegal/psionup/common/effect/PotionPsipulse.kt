package wiresegal.psionup.common.effect

import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect
import vazkii.psi.api.cad.ICADColorizer
import wiresegal.psionup.common.effect.base.PotionPsiChange
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 10:01 PM on 7/13/16.
 */
class PotionPsipulse : PotionPsiChange(LibNames.Potions.PSIPULSE, false, ICADColorizer.DEFAULT_SPELL_COLOR) {
    override val ampAmount: Int
        get() = 10
    override val baseAmount: Int
        get() = 20

    override fun performEffect(entity: EntityLivingBase, amplifier: Int) {
        val shockEffect = ModPotions.psishock.getEffect(entity)
        if (shockEffect != null) {
            val thisEffect = getEffect(entity)!!
            val newEffect = PotionEffect(ModPotions.psishock, shockEffect.duration + thisEffect.duration, Math.min(shockEffect.amplifier + thisEffect.amplifier + 1, 127))
            shockEffect.combine(newEffect)
            thisEffect.combine(PotionEffect(this, 0, amplifier + 1))
        } else
            super.performEffect(entity, amplifier)
    }
}
