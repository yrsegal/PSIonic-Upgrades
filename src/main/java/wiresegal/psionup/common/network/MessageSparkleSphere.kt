package wiresegal.psionup.common.network

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister
import com.teamwizardry.librarianlib.features.network.PacketBase
import com.teamwizardry.librarianlib.features.saving.Save
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.api.internal.Vector3
import vazkii.psi.common.Psi
import wiresegal.psionup.common.entity.EntityGaussPulse
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:17 AM on 7/10/16.
 */
@PacketRegister(Side.CLIENT)
class MessageSparkleSphere(@Save var position: Vec3d = Vec3d.ZERO,
                           @Save var status: EntityGaussPulse.AmmoStatus = EntityGaussPulse.AmmoStatus.DEPLETED) : PacketBase() {

    override fun handle(ctx: MessageContext) {
        val world = LibrarianLib.PROXY.getClientPlayer().world
        val rand = world.rand
        val shift = rand.nextInt(6)

        for (theta in shift until 360 step 10) for (azimuth in -90 + shift until 90 step 10) {
            val dist = rand.nextDouble() * 2 + 3
            val x = MathHelper.cos(theta * Math.PI.toFloat() / 180) * MathHelper.cos(azimuth * Math.PI.toFloat() / 180) * dist + position.xCoord
            val y = MathHelper.sin(azimuth * Math.PI.toFloat() / 180) * dist + position.yCoord
            val z = MathHelper.sin(theta * Math.PI.toFloat() / 180) * MathHelper.cos(azimuth * Math.PI.toFloat() / 180) * dist + position.zCoord

            val color = Color(if (status == EntityGaussPulse.AmmoStatus.NOTAMMO) ICADColorizer.DEFAULT_SPELL_COLOR else 0xB87333)
            val r = color.red.toFloat() / 255.0f
            val g = color.green.toFloat() / 255.0f
            val b = color.blue.toFloat() / 255.0f

            val direction = Vector3(position).add(-x, -y, -z).normalize().multiply(-0.325)

            Psi.proxy.sparkleFX(world, position.xCoord, position.yCoord, position.zCoord, r, g, b, direction.x.toFloat(), direction.y.toFloat(), direction.z.toFloat(), 1.2f, 12)
        }
    }
}
