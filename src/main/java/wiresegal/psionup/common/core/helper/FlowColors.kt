package wiresegal.psionup.common.core.helper

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICAD
import vazkii.psi.common.core.helper.ItemNBTHelper
import wiresegal.psionup.common.items.ModItems

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
            val player = e.entityLiving
            if (player is EntityPlayer && !player.worldObj.isRemote) {
                val cad = PsiAPI.getPlayerCAD(player)
                if (cad != null) {
                    val colorizer = (cad.item as ICAD).getComponentInSlot(cad, EnumCADComponent.DYE) ?: ItemStack(ModItems.liquidColorizer)
                    applyColor(player, colorizer)
                } else {
                    purgeColor(player)
                }
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
        for (i in 0..player.inventory.sizeInventory - 1) {
            val stack = player.inventory.getStackInSlot(i)
            if (stack != null && stack.item is IAcceptor)
                purgeColor(stack)
        }
    }

    fun applyColor(player: EntityPlayer, color: ItemStack) {
        for (i in 0..player.inventory.sizeInventory - 1) {
            val stack = player.inventory.getStackInSlot(i)
            if (stack != null && stack.item is IAcceptor)
                setColor(stack, color)
        }
    }

    fun setColor(stack: ItemStack, colorizer: ItemStack) {
        ItemNBTHelper.setCompound(stack, TAG_FLOW_COLOR, colorizer.writeToNBT(NBTTagCompound()))
    }

    fun getColor(stack: ItemStack): ItemStack? {
        return ItemStack.loadItemStackFromNBT(ItemNBTHelper.getCompound(stack.copy(), TAG_FLOW_COLOR, true) ?: return null)
    }
}
