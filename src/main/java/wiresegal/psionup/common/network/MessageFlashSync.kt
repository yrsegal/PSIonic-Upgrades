package wiresegal.psionup.common.network

import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import vazkii.psi.api.spell.ISpellContainer
import vazkii.psi.api.spell.Spell
import wiresegal.psionup.common.items.spell.ItemFlashRing

/**
 * @author WireSegal
 * Created at 12:17 AM on 7/10/16.
 */
class MessageFlashSync(var spell: Spell? = null) : IMessage {

    class MessageFlashSyncHandler() : IMessageHandler<MessageFlashSync, IMessage> {

        override fun onMessage(message: MessageFlashSync?, ctx: MessageContext?): IMessage? {
            if (ctx != null && message != null && ctx.side.isServer) {
                val player = ctx.serverHandler.playerEntity

                val stack = getStack(player, ItemFlashRing::class.java) ?: return null
                (stack.item as ISpellContainer).setSpell(player, stack, message.spell)
            }
            return null
        }

        private fun getStack(p: EntityPlayer, itemClass: Class<*>): ItemStack? {
            var item = p.heldItemMainhand?.item
            if (item != null && itemClass.isInstance(item) || (item is ItemBlock && itemClass.isInstance(item.block)))
                return p.heldItemMainhand

            item = p.heldItemOffhand?.item
            if (item != null && itemClass.isInstance(item) || (item is ItemBlock && itemClass.isInstance(item.block)))
                return p.heldItemOffhand

            return null
        }
    }

    override fun fromBytes(buf: ByteBuf?) {
        buf?.let {
            spell = it.readSpell()
        }
    }

    override fun toBytes(buf: ByteBuf?) {
        buf?.let {
            it.writeSpell(spell)
        }
    }

    private fun ByteBuf.readSpell(): Spell? {
        val cmp = ByteBufUtils.readTag(this)
        return Spell.createFromNBT(cmp)
    }

    private fun ByteBuf.writeSpell(spell: Spell?) {
        val cmp = NBTTagCompound()
        spell?.writeToNBT(cmp)
        ByteBufUtils.writeTag(this, cmp)
    }
}
