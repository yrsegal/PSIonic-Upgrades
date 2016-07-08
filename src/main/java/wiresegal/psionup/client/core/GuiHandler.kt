package wiresegal.psionup.client.core

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import wiresegal.psionup.common.block.BlockCADCase
import wiresegal.psionup.common.gui.cadcase.ContainerCADCase
import wiresegal.psionup.common.gui.cadcase.GuiCADCase
import wiresegal.psionup.common.gui.magazine.ContainerCADMagazine
import wiresegal.psionup.common.gui.magazine.GuiCADMagazine
import wiresegal.psionup.common.items.spell.ItemCADMagazine

/**
 * @author WireSegal
 * Created at 8:30 PM on 7/5/16.
 */
object GuiHandler : IGuiHandler {
    val GUI_CASE = 0
    val GUI_MAGAZINE = 1

    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int): Any? {
        if (ID == GUI_CASE) {
            val stack = getStack(player, BlockCADCase::class.java)
            if (stack != null) return ContainerCADCase(player, stack)
        } else if (ID == GUI_MAGAZINE) {
            val stack = getStack(player, ItemCADMagazine::class.java)
            if (stack != null) return ContainerCADMagazine(player, stack)
        }
        return null
    }

    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int): Any? {
        if (ID == GUI_CASE) {
            val stack = getStack(player, BlockCADCase::class.java)
            if (stack != null) return GuiCADCase(player, stack)
        } else if (ID == GUI_MAGAZINE) {
            val stack = getStack(player, ItemCADMagazine::class.java)
            if (stack != null) return GuiCADMagazine(player, stack)
        }
        return null
    }

    private fun getStack(p: EntityPlayer, itemClass: Class<*>): ItemStack? {
        var item = p.heldItemMainhand?.item
        if (item != null && itemClass.isInstance(item) || (item is ItemBlock && itemClass.isInstance(item.block)))
            return p.heldItemMainhand;

        item = p.heldItemOffhand?.item
        if (item != null && itemClass.isInstance(item) || (item is ItemBlock && itemClass.isInstance(item.block)))
            return p.heldItemOffhand;

        return null
    }
}
