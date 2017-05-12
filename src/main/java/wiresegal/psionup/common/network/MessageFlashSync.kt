package wiresegal.psionup.common.network

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import io.netty.buffer.ByteBuf
import jdk.nashorn.internal.objects.NativeError.getStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import vazkii.psi.api.spell.ISpellContainer
import vazkii.psi.api.spell.Spell
import wiresegal.psionup.common.items.spell.ItemFlashRing

/**
 * @author WireSegal
 * Created at 12:17 AM on 7/10/16.
 */
@PacketRegister(Side.SERVER)
class MessageFlashSync(var spell: Spell? = null) : PacketBase() {

    private fun getStack(p: EntityPlayer, itemClass: Class<*>): ItemStack {
        var item = p.heldItemMainhand?.item
        if (item != null && itemClass.isInstance(item) || (item is ItemBlock && itemClass.isInstance(item.block)))
            return p.heldItemMainhand

        item = p.heldItemOffhand?.item
        if (item != null && itemClass.isInstance(item) || (item is ItemBlock && itemClass.isInstance(item.block)))
            return p.heldItemOffhand

        return ItemStack.EMPTY
    }

    override fun handle(ctx: MessageContext) {
        val player = ctx.serverHandler.player

        val stack = getStack(player, ItemFlashRing::class.java)
        if (stack.isEmpty) return
        (stack.item as ISpellContainer).setSpell(player, stack, spell)
    }

    override fun readCustomBytes(buf: ByteBuf) {
        spell = buf.readSpell()
    }

    override fun writeCustomBytes(buf: ByteBuf) {
        buf.writeSpell(spell)
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
