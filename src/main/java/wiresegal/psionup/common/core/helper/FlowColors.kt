package wiresegal.psionup.common.core.helper

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.item.ItemEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import vazkii.psi.api.PsiAPI
import vazkii.psi.common.Psi
import vazkii.psi.common.core.helper.ItemNBTHelper
import wiresegal.psionup.common.network.MessageFlowColorUpdate
import wiresegal.psionup.common.network.NetworkHandler

/**
 * @author WireSegal
 * Created at 5:12 PM on 7/10/16.
 */
object FlowColors {

    interface IAcceptor

    object EventHandler {
        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun onPlayerUpdate(e: LivingEvent.LivingUpdateEvent) {
            if (e.entityLiving is EntityPlayer) {
                updateColor(e.entityLiving as EntityPlayer)
            }
        }
    }

    fun removeEntry(stack: ItemStack, tag: String) {
        ItemNBTHelper.getNBT(stack).removeTag(tag)
    }

    val TAG_FLOW_COLOR = "FlowColor"

    fun purgeColor(stack: ItemStack) {
        removeEntry(stack, TAG_FLOW_COLOR)
    }

    fun purgeColor(player: EntityPlayer) {
        for (i in 0..player.inventory.sizeInventory-1) {
            val stack = player.inventory.getStackInSlot(i)
            if (stack != null && stack.item is IAcceptor)
                purgeColor(stack)
        }
    }

    fun applyColor(player: EntityPlayer, color: Int) {
        for (i in 0..player.inventory.sizeInventory-1) {
            val stack = player.inventory.getStackInSlot(i)
            if (stack != null && stack.item is IAcceptor)
                setColor(stack, color)
        }
    }

    fun setColor(stack: ItemStack, color: Int) {
        ItemNBTHelper.setInt(stack, TAG_FLOW_COLOR, color)
    }

    fun getColor(stack: ItemStack): Int {
        return ItemNBTHelper.getInt(stack.copy(), TAG_FLOW_COLOR, 0)
    }

    fun updateColor(player: EntityPlayer) {
        val cad = PsiAPI.getPlayerCAD(player)
        if (player.worldObj.isRemote && cad != null)
            NetworkHandler.INSTANCE.sendToServer(MessageFlowColorUpdate(Psi.proxy.getCADColor(cad).rgb))
        else if (cad == null)
            purgeColor(player)
    }
}
