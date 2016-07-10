package wiresegal.psionup.common.network

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import wiresegal.psionup.common.core.helper.FlowColors

/**
 * @author WireSegal
 * Created at 5:46 PM on 7/10/16.
 */
class MessageFlowColorUpdate(var color: Int = 0) : IMessage {

    class MessageFlowColorHandler : IMessageHandler<MessageFlowColorUpdate, IMessage> {
        override fun onMessage(message: MessageFlowColorUpdate?, ctx: MessageContext?): IMessage? {
            if (message != null && ctx != null && ctx.side.isServer) {
                FlowColors.applyColor(ctx.serverHandler.playerEntity, message.color)
            }
            return null
        }
    }

    override fun toBytes(buf: ByteBuf?) {
        buf?.let {
            it.writeInt(color)
        }
    }

    override fun fromBytes(buf: ByteBuf?) {
        buf?.let {
            color = it.readInt()
        }
    }
}
