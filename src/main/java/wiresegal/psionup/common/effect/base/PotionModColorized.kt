package wiresegal.psionup.common.effect.base

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.common.Psi
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:27 AM on 4/15/16.
 */
open class PotionModColorized(name: String, badEffect: Boolean, color: Int, iconIndex: Int) : PotionMod(name, badEffect, color, iconIndex) {

    @SideOnly(Side.CLIENT)
    override fun renderHUDEffect(x: Int, y: Int, effect: PotionEffect?, mc: Minecraft, alpha: Float) {
        var color = ICADColorizer.DEFAULT_SPELL_COLOR
        val cad = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().thePlayer)
        if (cad != null) color = Psi.proxy.getCADColor(cad).rgb
        val pulse = pulseColor(color)
        GlStateManager.color(pulse.first / 255f, pulse.second / 255f, pulse.third / 255f, alpha)
        super.renderHUDEffect(x, y, effect, mc, alpha)
        GlStateManager.color(1f, 1f, 1f)
    }

    @SideOnly(Side.CLIENT)
    override fun renderInventoryEffect(x: Int, y: Int, effect: PotionEffect, mc: Minecraft) {
        var color = ICADColorizer.DEFAULT_SPELL_COLOR
        val cad = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().thePlayer)
        if (cad != null) color = Psi.proxy.getCADColor(cad).rgb
        val pulse = pulseColor(color)
        GlStateManager.color(pulse.first / 255f, pulse.second / 255f, pulse.third / 255f)
        super.renderInventoryEffect(x, y, effect, mc)
        GlStateManager.color(1f, 1f, 1f)
    }

    fun pulseColor(rgb: Int): Triple<Int, Int, Int> {
        val add = (MathHelper.sin(ClientTickHandler.ticksInGame * 0.2f) * 32).toInt()
        val r = (rgb and (0xFF shl 16)) shr 16
        val b = (rgb and (0xFF shl 8)) shr 8
        val g = (rgb and (0xFF shl 0)) shr 0
        return Triple(Math.max(Math.min(r + add, 255), 0),
                Math.max(Math.min(b + add, 255), 0),
                Math.max(Math.min(g + add, 255), 0))
    }
}
