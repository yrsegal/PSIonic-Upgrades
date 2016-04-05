package wiresegal.psionup.common.items.component.botania

import net.minecraft.client.gui.GuiScreen
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler
import vazkii.botania.common.item.ItemManaGun
import vazkii.botania.common.item.ModItems
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICAD
import wiresegal.psionup.common.items.CompatItems
import java.util.*
import java.util.regex.Pattern

/**
 * @author WireSegal
 * Created at 10:26 PM on 3/31/16.
 */
class BlasterEventHandler {
    companion object {
        val INSTANCE = BlasterEventHandler()

        fun register() {
            MinecraftForge.EVENT_BUS.register(INSTANCE)
        }
    }

    val advancedMatcher = Pattern.compile("\\s+(?=\\(#\\d+\\))")

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun applyTooltip(e: ItemTooltipEvent) {
        val newTooltip = ArrayList<String>()
        var name = e.toolTip[0]
        if (e.itemStack.item is ICAD) {
            val item = e.itemStack.item as ICAD

            val component = item.getComponentInSlot(e.itemStack, EnumCADComponent.ASSEMBLY)

            if (component != null && component.item == CompatItems.blaster) {
                val lens = ItemManaGun.getLens(e.itemStack)
                if (lens != null) {
                    if (e.isShowAdvancedItemTooltips) {
                        val match = advancedMatcher.split(name)
                        if (match.size > 1) {
                            var joined = match.slice(1..match.size - 1).joinToString(" ")
                            if (joined.length > 0) joined = " $joined"

                            name = match[0].trimEnd() + " ${TextFormatting.RESET}(${TextFormatting.GREEN}${lens.displayName}${TextFormatting.RESET})" + joined
                        }
                    }
                }
                if (GuiScreen.isShiftKeyDown())
                    ModItems.manaGun.addInformation(e.itemStack, e.entityPlayer, newTooltip, e.isShowAdvancedItemTooltips)
                e.toolTip.addAll(2, newTooltip)
                e.toolTip[0] = name
            }
        }
    }

    @SubscribeEvent
    fun onInteract(e: PlayerInteractEvent.RightClickItem) {
        if (e.entityPlayer.isSneaking) {
            val heldItem = e.itemStack
            val hand = e.hand
            if (heldItem != null && heldItem.item is ICAD) {
                val item = heldItem.item as ICAD
                if (item.getComponentInSlot(heldItem, EnumCADComponent.ASSEMBLY).item == CompatItems.blaster && ItemManaGun.hasClip(heldItem)) {
                    ItemManaGun.rotatePos(heldItem)
                    e.world.playSound(null, e.entityPlayer.posX, e.entityPlayer.posY, e.entityPlayer.posZ, SoundEvents.block_stone_button_click_on, SoundCategory.PLAYERS, 0.6F, (1.0F + (e.world.rand.nextFloat() - e.world.rand.nextFloat()) * 0.2F) * 0.7F)
                    if (e.world.isRemote)
                        e.entityPlayer.swingArm(hand)
                    val lens = ItemManaGun.getLens(heldItem)
                    ItemsRemainingRenderHandler.set(lens, -2)

                    e.isCanceled = true
                }
            }
        }
    }
}
