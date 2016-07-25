package wiresegal.psionup.common.effect

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.network.NetworkHandler
import vazkii.psi.common.network.message.MessageDataSync
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.effect.base.PotionMod
import wiresegal.psionup.common.effect.base.PotionModColorized
import wiresegal.psionup.common.effect.base.PotionPsiChange
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 10:01 PM on 7/13/16.
 */
class PotionPsipulse(iconIndex: Int) : PotionPsiChange(LibNames.Potions.PSIPULSE, false, ICADColorizer.DEFAULT_SPELL_COLOR, iconIndex) {
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
