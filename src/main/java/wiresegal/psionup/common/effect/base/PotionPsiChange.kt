package wiresegal.psionup.common.effect.base

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import vazkii.arl.network.NetworkHandler
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICAD
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.network.message.MessageDataSync

/**
 * @author WireSegal
 * Created at 10:01 PM on 7/13/16.
 */
abstract class PotionPsiChange(name: String, badEffect: Boolean, color: Int, iconIndex: Int) : PotionModColorized(name, badEffect, color, iconIndex) {
    override fun isReady(duration: Int, amplifier: Int): Boolean {
        return true
    }

    abstract val baseAmount: Int
    abstract val ampAmount: Int

    override fun performEffect(entity: EntityLivingBase, amplifier: Int) {
        if (entity is EntityPlayer) {
            val amp = amplifier.toByte()
            addPsiToPlayer(entity, baseAmount + amp * ampAmount)
        }
    }

    fun addPsiToPlayer(player: EntityPlayer, psi: Int) {
        val data = PlayerDataHandler.get(player)
        if (psi < 0)
            data.deductPsi(-psi, 40, true, true)
        else {
            val cad = PsiAPI.getPlayerCAD(player) ?: return
            val cadItem = cad.item as ICAD
            var icad = true

            val overflow = cadItem.getStatValue(cad, EnumCADStat.OVERFLOW)
            val stored = cadItem.getStoredPsi(cad)
            if (stored < overflow) {
                cadItem.regenPsi(cad, Math.max(1, psi / 2))
                icad = false
            }

            if (icad && data.availablePsi < data.totalPsi) {
                data.availablePsi = Math.min(data.totalPsi, data.availablePsi + psi)
                data.save()
                if (player.world.isRemote && player is EntityPlayerMP)
                    NetworkHandler.INSTANCE.sendTo(MessageDataSync(), player)
            }
        }
    }

}
