package wiresegal.psionup.common.items.component

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.exosuit.IExosuitSensor
import vazkii.psi.api.exosuit.PsiArmorEvent
import vazkii.psi.api.internal.MathHelper
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.common.core.handler.PlayerDataHandler
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.common.items.base.ItemMod
import wiresegal.psionup.common.lib.LibMisc
import java.awt.Color

/**
 * @author WireSegal
 * Created at 5:44 PM on 7/13/16.
 */
class ItemBioticSensor(name: String) : ItemMod(name), IExosuitSensor, ModelHandler.IItemColorProvider {

    companion object {

        val EVENT_BIOTIC = "${LibMisc.MOD_ID}.event.nearbyEntities"

        val triggeredBioticsNonremote = hashMapOf<EntityPlayer, MutableList<EntityLivingBase>>()
        val triggeredBioticsRemote = hashMapOf<EntityPlayer, MutableList<EntityLivingBase>>()

        fun biotics(remote: Boolean) = if (remote) triggeredBioticsRemote else triggeredBioticsNonremote

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun onPlayerTick(e: LivingEvent.LivingUpdateEvent) {
            val range = 10.0

            val player = e.entityLiving
            if (player is EntityPlayer) {
                val triggeredBiotics = biotics(player.worldObj.isRemote)
                var triggered = triggeredBiotics[player]
                if (triggered == null) {
                    triggered = mutableListOf()
                    triggeredBiotics.put(player, triggered)
                }

                val found = mutableListOf<EntityLivingBase>()
                val entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(-range, -range, -range, range, range, range).offset(player.posX, player.posY, player.posZ))
                for (entity in entities) if (entity != player && MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, entity.posX, entity.posY, entity.posZ) <= range) {
                    if (!triggered.contains(entity))
                        PsiArmorEvent.post(PsiArmorEvent(player, EVENT_BIOTIC, 0.0, entity))
                    found.add(entity)
                }

                triggered.clear()
                for (i in found) triggered.add(i)
            }

        }
    }

    override fun getColor(p0: ItemStack?): Int {
        val add = Math.max((Math.sin(ClientTickHandler.ticksInGame * 0.1) * 96).toInt(), 0)
        val newColor =
                (add shl 16) or
                (add shl 8) or
                (add shl 0)
        return newColor
    }

    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i -> if (i == 1) getColor(itemStack) else 0xFFFFFF }
    }

    override fun getEventType(p0: ItemStack?): String {
        return EVENT_BIOTIC
    }
}
