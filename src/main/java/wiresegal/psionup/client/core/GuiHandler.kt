package wiresegal.psionup.client.core

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import wiresegal.psionup.common.block.BlockCADCase
import wiresegal.psionup.common.gui.ContainerCADCase
import wiresegal.psionup.common.gui.GuiCADCase

/**
 * @author WireSegal
 * Created at 8:30 PM on 7/5/16.
 */
object GuiHandler : IGuiHandler {
    val GUI_CASE = 0

    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int): Any? {
        if (ID == GUI_CASE) {
            val stack = getStack(player, BlockCADCase::class.java)
            if (stack != null) return ContainerCADCase(player, stack)
        }
        return null
    }

    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World?, x: Int, y: Int, z: Int): Any? {
        if (ID == GUI_CASE) {
            val stack = getStack(player, BlockCADCase::class.java)
            if (stack != null) return GuiCADCase(player, stack)
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
