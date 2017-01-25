package wiresegal.psionup.common.block.tile

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagInt
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.ITickable
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.arl.block.tile.TileMod
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
    private val TAG_COLORIZER = "colorizer"

    var colorizer: ItemStack? = null

    val rays = mutableSetOf<Vector3>()

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
        return if (colorizer == null) Color(ICADColorizer.DEFAULT_SPELL_COLOR) else Psi.proxy.getColorizerColor(colorizer)
    }

    @SideOnly(Side.CLIENT)
    private fun makeLine(vec: Vector3) {
        val start = Vector3.fromBlockPos(pos).add(0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05)
        val stepsPer = Math.random() * 6.0

        val len = vec.mag()
        val ray = vec.copy().multiply(1 / len)
        val steps = (len * stepsPer).toInt()

        for (i in 0..steps - 1) {
            val extended = ray.copy().multiply(i / stepsPer)
            val x = start.x + extended.x
            val y = start.y + extended.y
            val z = start.z + extended.z

            val c = getColor()

            val r = c.red.toFloat() / 255.0f
            val g = c.green.toFloat() / 255.0f
            val b = c.blue.toFloat() / 255.0f

            Psi.proxy.wispFX(world, x, y, z, r, g, b, 0.125f)
        }
    }

    override fun writeSharedNBT(cmp: NBTTagCompound) {
        val list = NBTTagList()
        for (ray in rays) {
            val rayList = NBTTagList()
            rayList.appendTag(NBTTagInt(ray.x.toInt()))
            rayList.appendTag(NBTTagInt(ray.y.toInt()))
            rayList.appendTag(NBTTagInt(ray.z.toInt()))
            list.appendTag(rayList)
        }
        cmp.setTag(TAG_RAYS, list)
        if (colorizer != null)
            cmp.setTag(TAG_COLORIZER, colorizer!!.writeToNBT(NBTTagCompound()))
    }

    override fun readSharedNBT(cmp: NBTTagCompound) {
        val list = cmp.getTagList(TAG_RAYS, 9)
        for (i in 0..list.tagCount() - 1) {
            val ray = list.get(i) as NBTTagList
            val vec = Vector3(ray.getIntAt(0).toDouble(), ray.getIntAt(1).toDouble(), ray.getIntAt(2).toDouble())
            rays.add(vec)
        }
        if (cmp.hasKey(TAG_COLORIZER))
            colorizer = ItemStack(cmp.getCompoundTag(TAG_COLORIZER))
    }
}
