package wiresegal.psionup.common.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.api.internal.Vector3
import vazkii.psi.common.Psi
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:17 AM on 7/10/16.
 */
class MessageParticleTrail(var position: Vector3? = null, var ray: Vector3? = null, var length: Double = 0.0, var time: Int = 0, var cad: ItemStack? = null) : IMessage {

    class MessageParticleHandler() : IMessageHandler<MessageParticleTrail, IMessage> {

        override fun onMessage(message: MessageParticleTrail?, ctx: MessageContext?): IMessage? {
            if (ctx != null && message != null && ctx.side.isClient) {
                val dir = message.ray ?: return null
                val pos = message.position ?: return null
                val len = message.length
                val time = message.time
                val cad = message.cad

                val ray = dir.copy().normalize()
                val steps = (len * 4).toInt()

                var color = Color(ICADColorizer.DEFAULT_SPELL_COLOR)
                if (cad != null) color = Psi.proxy.getCADColor(cad)

                val r = color.red.toFloat() / 255.0f
                val g = color.green.toFloat() / 255.0f
                val b = color.blue.toFloat() / 255.0f

                for (i in 0..steps - 1) {
                    val extended = ray.copy().multiply(i / 4.0)
                    val x = pos.x + extended.x
                    val y = pos.y + extended.y
                    val z = pos.z + extended.z

                    makeParticle(Minecraft.getMinecraft().theWorld, r, g, b, x, y, z, 0.0, 0.0, 0.0, time)
                }
            }
            return null
        }

        fun makeParticle(world: World, r: Float, g: Float, b: Float, xp: Double, yp: Double, zp: Double, xv: Double, yv: Double, zv: Double, time: Int) {
            val xvn = xv * 0.1
            val yvn = yv * 0.1
            val zvn = zv * 0.1
            Psi.proxy.sparkleFX(world, xp, yp, zp, r, g, b, xvn.toFloat(), yvn.toFloat(), zvn.toFloat(), 0.25f, time)
        }
    }

    override fun fromBytes(buf: ByteBuf?) {
        buf?.let {
            position = it.readVector()
            ray = it.readVector()
            length = it.readDouble()
            time = it.readInt()
            cad = ByteBufUtils.readItemStack(it)
        }
    }

    override fun toBytes(buf: ByteBuf?) {
        buf?.let {
            it.writeVector(position)
            it.writeVector(ray)
            it.writeDouble(length)
            it.writeInt(time)
            ByteBufUtils.writeItemStack(it, cad)
        }
    }

    private fun ByteBuf.readVector(): Vector3 {
        val x = readDouble()
        val y = readDouble()
        val z = readDouble()
        return Vector3(x, y, z)
    }

    private fun ByteBuf.writeVector(vec: Vector3?) {
        if (vec == null)
            for (i in 0..2)
                writeDouble(0.0)
        else {
            writeDouble(vec.x)
            writeDouble(vec.y)
            writeDouble(vec.z)
        }
    }
}
