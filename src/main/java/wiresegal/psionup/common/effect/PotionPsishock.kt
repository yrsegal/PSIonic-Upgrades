package wiresegal.psionup.common.effect

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Vector3d
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.network.Message
import vazkii.psi.common.network.NetworkHandler
import vazkii.psi.common.network.message.MessageDataSync
import wiresegal.psionup.common.effect.base.PotionMod
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 10:01 PM on 7/13/16.
 */
class PotionPsishock(iconIndex: Int) : PotionMod(LibNames.Potions.PSISHOCK, true, ICADColorizer.DEFAULT_SPELL_COLOR, iconIndex, true) {
    override fun isReady(duration: Int, amplifier: Int): Boolean {
        return true
    }

    override fun performEffect(entity: EntityLivingBase?, amplifier: Int) {
        if (entity is EntityPlayer) {
            val amp = amplifier.toByte()
            val amount = 30 + amp * 15
            val data = PlayerDataHandler.get(entity)
            if (amount >= 0)
                data.deductPsi(amount, 40, true, true)
            else {
                val cad = PsiAPI.getPlayerCAD(entity) ?: return
                val cadItem = cad.item as ICAD
                var icad = true

                val overflow = cadItem.getStatValue(cad, EnumCADStat.OVERFLOW)
                val stored = cadItem.getStoredPsi(cad)
                if (stored < overflow) {
                    cadItem.regenPsi(cad, Math.max(1, -amount / 2))
                    icad = false
                }

                if (icad && data.availablePsi < data.totalPsi) {
                    data.availablePsi = Math.min(data.totalPsi, data.availablePsi - amount)
                    data.save()
                    if (entity.worldObj.isRemote && entity is EntityPlayerMP)
                        NetworkHandler.INSTANCE.sendTo(MessageDataSync(), entity)
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun renderHUDEffect(x: Int, y: Int, effect: PotionEffect?, mc: Minecraft, alpha: Float) {
        val pulse = pulseColor(0xE8E8E8)
        GlStateManager.color(pulse.first / 255f, pulse.second / 255f, pulse.third / 255f)
        super.renderHUDEffect(x, y, effect, mc, alpha)
        GlStateManager.color(1f, 1f, 1f)
    }

    @SideOnly(Side.CLIENT)
    override fun renderInventoryEffect(x: Int, y: Int, effect: PotionEffect, mc: Minecraft) {
        val pulse = pulseColor(0xE8E8E8)
        GlStateManager.color(pulse.first / 255f, pulse.second / 255f, pulse.third / 255f)
        super.renderInventoryEffect(x, y, effect, mc)
        GlStateManager.color(1f, 1f, 1f)
    }

    fun pulseColor(rgb: Int): Triple<Int, Int, Int> {
        val add = (MathHelper.sin(ClientTickHandler.ticksInGame * 0.2f) * 24).toInt()
        val r = (rgb and (0xFF shl 16)) shr 16
        val b = (rgb and (0xFF shl 8)) shr 8
        val g = (rgb and (0xFF shl 0)) shr 0
        return Triple(Math.max(Math.min(r + add, 255), 0),
               Math.max(Math.min(b + add, 255), 0),
               Math.max(Math.min(g + add, 255), 0))
    }
}
