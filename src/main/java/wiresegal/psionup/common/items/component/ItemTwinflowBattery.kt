package wiresegal.psionup.common.items.component

import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICAD
import vazkii.psi.common.core.handler.PlayerDataHandler
import wiresegal.psionup.common.effect.base.PotionPsiChange
import wiresegal.psionup.common.items.base.ItemComponent
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 8:43 AM on 3/20/16.
 */
class ItemTwinflowBattery(name: String) : ItemComponent(name) {

    override fun registerStats() {
        addStat(EnumCADStat.OVERFLOW, 0, 500)
    }

    override fun getComponentType(p0: ItemStack) = EnumCADComponent.BATTERY

    override fun addHiddenTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        super.addHiddenTooltip(stack, playerIn, tooltip, advanced)

        addPositiveTag(tooltip, "${LibMisc.MOD_ID}.cadstat.extra", "${LibMisc.MOD_ID}.upsides.boost_regen", 5)
        addPositiveTag(tooltip, "${LibMisc.MOD_ID}.cadstat.extra", "${LibMisc.MOD_ID}.upsides.fills_last")
    }

    companion object {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun onPlayerTick(e: TickEvent.PlayerTickEvent) {
            val player = e.player
            if (e.side.isServer) {
                val cad = PsiAPI.getPlayerCAD(player)
                if (!cad.isEmpty) {
                    val item = cad.item as ICAD
                    val battery = item.getComponentInSlot(cad, EnumCADComponent.BATTERY)

                    if (!battery.isEmpty && battery.item is ItemTwinflowBattery) {
                        val data = PlayerDataHandler.get(player)

                        if (data.regenCooldown == 0)
                            PotionPsiChange.addPsiToPlayer(player, 5 + if (data.availablePsi != data.totalPsi) Math.ceil(data.regenPerTick / 2.0).toInt() else 0, false)

                        val amountToDump = Math.min(data.totalPsi - data.availablePsi, item.getStoredPsi(cad))
                        if (amountToDump > 0) {
                            data.deductPsi(-amountToDump, 0, true)
                            item.consumePsi(cad, amountToDump)
                        }
                    }
                }
            }
        }
    }
}
