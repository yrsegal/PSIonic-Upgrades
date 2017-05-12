package wiresegal.psionup.common.block.tile

import com.teamwizardry.librarianlib.features.base.block.TileMod
import com.teamwizardry.librarianlib.features.saving.Save
import com.teamwizardry.librarianlib.features.structure.InWorldRender.pos
import io.netty.buffer.ByteBuf
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.ITickable
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.api.internal.Vector3
import vazkii.psi.common.Psi
import java.awt.Color

/**
 * @author WireSegal
 * Created at 1:46 PM on 5/4/16.
 */
class TileCracklingStar : TileMod(), ITickable {
    private val TAG_RAYS = "rays"

    @Save
    var colorizer: ItemStack = ItemStack.EMPTY

    @Save
    val rays = mutableSetOf<Vec3d>()

    @Save
    var time = -1

    override fun update() {
        if (world.isRemote) {
            for (ray in rays)
                makeLine(ray)
            val color = getColor()
            Psi.proxy.wispFX(world, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, color.red / 255f, color.green / 255f, color.blue / 255f, 0.25f)
        } else {
            if (time > 0) time--
            else if (time == 0) world.setBlockToAir(pos)
        }
    }

    fun getColor(): Color {
        return if (colorizer.isEmpty) Color(ICADColorizer.DEFAULT_SPELL_COLOR) else Psi.proxy.getColorizerColor(colorizer)
    }

    @SideOnly(Side.CLIENT)
    private fun makeLine(vec: Vec3d) {
        val start = Vector3.fromBlockPos(pos).add(0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05)
        val stepsPer = Math.random() * 6.0

        val len = vec.lengthVector()
        val ray = vec.scale(1 / len)
        val steps = (len * stepsPer).toInt()

        for (i in 0..steps - 1) {
            val extended = ray.scale(i / stepsPer)
            val x = start.x + extended.xCoord
            val y = start.y + extended.yCoord
            val z = start.z + extended.zCoord

            val c = getColor()

            val r = c.red.toFloat() / 255.0f
            val g = c.green.toFloat() / 255.0f
            val b = c.blue.toFloat() / 255.0f

            Psi.proxy.wispFX(world, x, y, z, r, g, b, 0.125f)
        }
    }

    override val useFastSync: Boolean
        get() = false

    override fun writeCustomNBT(cmp: NBTTagCompound, sync: Boolean) {
        val list = NBTTagList()
        for (ray in rays) {
            val rayList = NBTTagList()
            rayList.appendTag(NBTTagInt(ray.xCoord.toInt()))
            rayList.appendTag(NBTTagInt(ray.yCoord.toInt()))
            rayList.appendTag(NBTTagInt(ray.zCoord.toInt()))
            list.appendTag(rayList)
        }
        cmp.setTag(TAG_RAYS, list)
    }

    override fun readCustomNBT(cmp: NBTTagCompound) {
        val list = cmp.getTagList(TAG_RAYS, 9)
        (0..list.tagCount() - 1)
                .map { list.get(it) }
                .filterIsInstance<NBTTagList>()
                .mapTo(rays) { Vec3d(it.getIntAt(0).toDouble(), it.getIntAt(1).toDouble(), it.getIntAt(2).toDouble()) }
    }
}
