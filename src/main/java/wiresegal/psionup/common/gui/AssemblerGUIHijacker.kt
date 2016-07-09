package wiresegal.psionup.common.gui

import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerContainerEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.spell.ISpellContainer
import vazkii.psi.client.gui.GuiCADAssembler
import vazkii.psi.common.block.tile.TileCADAssembler
import vazkii.psi.common.block.tile.container.ContainerCADAssembler
import vazkii.psi.common.block.tile.container.slot.SlotBullet
import wiresegal.psionup.common.core.PsionicMethodHandles
import wiresegal.psionup.common.items.ModItems

/**
 * @author WireSegal
 * Created at 10:32 PM on 7/8/16.
 */
object AssemblerGUIHijacker {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onContainerInitialized(e: PlayerContainerEvent.Open) {
        val container = e.container
        if (container is ContainerCADAssembler) for (i in container.inventorySlots.indices) {
            val slot = container.inventorySlots[i]
            if (slot is SlotBullet) {
                container.inventorySlots[i] = SlotBulletReplacement(slot.inventory as TileCADAssembler, slot.slotIndex, slot.xDisplayPosition, slot.yDisplayPosition, PsionicMethodHandles.getSocketSlot(slot))
            }
        }
    }

    class SlotBulletReplacement(var assembler: TileCADAssembler, index: Int, xPosition: Int, yPosition: Int, var socketSlot: Int) : Slot(assembler, index, xPosition, yPosition) {

        override fun isItemValid(stack: ItemStack?): Boolean {
            if (stack!!.item is ISpellContainer) {
                val container = stack.item as ISpellContainer
                if (container.containsSpell(stack) && this.assembler.isBulletSlotEnabled(this.socketSlot)) {
                    val socketableStack = this.assembler.getStackInSlot(6)
                    if (container.isCADOnlyContainer(stack)) {
                        return socketableStack!!.item is ICAD || socketableStack.item == ModItems.fakeCAD || socketableStack.item == ModItems.magazine
                    }

                    return true
                }
            }

            return false
        }
    }
}
