package wiresegal.psionup.common.network

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.common.Psi
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:17 AM on 7/10/16.
 */
@PacketRegister(Side.CLIENT)
class MessageParticleTrail(@Save var position: Vec3d? = null,
                           @Save var ray: Vec3d? = null,
                           @Save var length: Double = 0.0,
                           @Save var time: Int = 0,
                           @Save var cad: ItemStack = ItemStack.EMPTY) : PacketBase() {

    fun makeParticle(world: World, r: Float, g: Float, b: Float, xp: Double, yp: Double, zp: Double, xv: Double, yv: Double, zv: Double, time: Int) {
        val xvn = xv * 0.1
        val yvn = yv * 0.1
        val zvn = zv * 0.1
        Psi.proxy.sparkleFX(world, xp, yp, zp, r, g, b, xvn.toFloat(), yvn.toFloat(), zvn.toFloat(), 0.25f, time)
    }

    override fun handle(ctx: MessageContext) {
        val dir = ray ?: return
        val pos = position ?: return
        val len = length
        val time = time
        val cad = cad

        val ray = dir.normalize()
        val steps = (len * 4).toInt()

        var color = Color(ICADColorizer.DEFAULT_SPELL_COLOR)
        if (!cad.isEmpty) color = Psi.proxy.getCADColor(cad)

        val r = color.red.toFloat() / 255.0f
        val g = color.green.toFloat() / 255.0f
        val b = color.blue.toFloat() / 255.0f

        for (i in 0..steps - 1) {
            val extended = ray.scale(i / 4.0)
            val x = pos.xCoord + extended.xCoord
            val y = pos.yCoord + extended.yCoord
            val z = pos.zCoord + extended.zCoord

            makeParticle(Minecraft.getMinecraft().world, r, g, b, x, y, z, 0.0, 0.0, 0.0, time)
        }
    }
}
