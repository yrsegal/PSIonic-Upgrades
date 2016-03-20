package wiresegal.psionup.common.block

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable
import vazkii.psi.common.Psi
import vazkii.psi.common.block.BlockConjured
import vazkii.psi.common.block.tile.base.TileMod
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 5:24 PM on 3/20/16.
 */

class TileConjuredPulsar : TileMod(), ITickable {
    var time = -1
    var colorizer: ItemStack? = null

    override fun update() {
        if (this.worldObj.isRemote) {
            var color = Color(1295871)
            if (this.colorizer != null) {
                color = Psi.proxy.getColorizerColor(this.colorizer)
            }

            val r = color.red.toFloat() / 255.0f
            val g = color.green.toFloat() / 255.0f
            val b = color.blue.toFloat() / 255.0f
            var state = this.worldObj.getBlockState(this.getPos())
            state = state.block.getActualState(state, this.worldObj, this.getPos())
            if (state.block === ModBlocks.conjured && state.getValue(BlockConjured.SOLID)) {
                val var16 = BooleanArray(12)
                Arrays.fill(var16, true)
                if (state.getValue(BlockConjured.BLOCK_DOWN)) {
                    this.removeEdges(var16, 0, 1, 2, 3)
                }

                if (state.getValue(BlockConjured.BLOCK_UP)) {
                    this.removeEdges(var16, 4, 5, 6, 7)
                }

                if (state.getValue(BlockConjured.BLOCK_NORTH)) {
                    this.removeEdges(var16, 3, 7, 8, 11)
                }

                if (state.getValue(BlockConjured.BLOCK_SOUTH)) {
                    this.removeEdges(var16, 1, 5, 9, 10)
                }

                if (state.getValue(BlockConjured.BLOCK_EAST)) {
                    this.removeEdges(var16, 2, 6, 10, 11)
                }

                if (state.getValue(BlockConjured.BLOCK_WEST)) {
                    this.removeEdges(var16, 0, 4, 8, 9)
                }

                val var17 = this.getPos().x.toDouble()
                val y = this.getPos().y.toDouble()
                val z = this.getPos().z.toDouble()
                this.makeParticle(var16[0], r, g, b, var17 + 0.0, y + 0.0, z + 0.0, 0.0, 0.0, 1.0)
                this.makeParticle(var16[1], r, g, b, var17 + 0.0, y + 0.0, z + 1.0, 1.0, 0.0, 0.0)
                this.makeParticle(var16[2], r, g, b, var17 + 1.0, y + 0.0, z + 0.0, 0.0, 0.0, 1.0)
                this.makeParticle(var16[3], r, g, b, var17 + 0.0, y + 0.0, z + 0.0, 1.0, 0.0, 0.0)
                this.makeParticle(var16[4], r, g, b, var17 + 0.0, y + 1.0, z + 0.0, 0.0, 0.0, 1.0)
                this.makeParticle(var16[5], r, g, b, var17 + 0.0, y + 1.0, z + 1.0, 1.0, 0.0, 0.0)
                this.makeParticle(var16[6], r, g, b, var17 + 1.0, y + 1.0, z + 0.0, 0.0, 0.0, 1.0)
                this.makeParticle(var16[7], r, g, b, var17 + 0.0, y + 1.0, z + 0.0, 1.0, 0.0, 0.0)
                this.makeParticle(var16[8], r, g, b, var17 + 0.0, y + 0.0, z + 0.0, 0.0, 1.0, 0.0)
                this.makeParticle(var16[9], r, g, b, var17 + 0.0, y + 0.0, z + 1.0, 0.0, 1.0, 0.0)
                this.makeParticle(var16[10], r, g, b, var17 + 1.0, y + 0.0, z + 1.0, 0.0, 1.0, 0.0)
                this.makeParticle(var16[11], r, g, b, var17 + 1.0, y + 0.0, z + 0.0, 0.0, 1.0, 0.0)
            } else if (Math.random() < 0.5) {
                val w = 0.15f
                val h = 0.05f
                val x = this.getPos().x.toDouble() + 0.5 + (Math.random() - 0.5) * w.toDouble()
                val y1 = this.getPos().y.toDouble() + 0.25 + (Math.random() - 0.5) * h.toDouble()
                val z1 = this.getPos().z.toDouble() + 0.5 + (Math.random() - 0.5) * w.toDouble()
                val s = 0.2f + Math.random().toFloat() * 0.1f
                val m = 0.01f + Math.random().toFloat() * 0.015f
                Psi.proxy.wispFX(this.worldObj, x, y1, z1, r, g, b, s, -m)
            }
        }

        if (this.time >= 0) {
            if (this.time == 0) {
                this.worldObj.setBlockToAir(this.getPos())
            } else {
                --this.time
            }

        }
    }

    fun makeParticle(doit: Boolean, r: Float, g: Float, b: Float, xp: Double, yp: Double, zp: Double, xv: Double, yv: Double, zv: Double) {
        var xvn = xv
        var yvn = yv
        var zvn = zv
        if (doit && Math.random() < 0.3) {
            val m = 0.1f
            xvn *= m.toDouble()
            yvn *= m.toDouble()
            zvn *= m.toDouble()
            Psi.proxy.sparkleFX(this.worldObj, xp, yp, zp, r, g, b, xvn.toFloat(), yvn.toFloat(), zvn.toFloat(), 1.25f, 20)
        }

    }

    fun removeEdges(edges: BooleanArray, vararg posArray: Int) {
        val var3 = posArray
        val var4 = posArray.size

        for (var5 in 0..var4 - 1) {
            val i = var3[var5]
            edges[i] = false
        }

    }

    override fun writeSharedNBT(cmp: NBTTagCompound?) {
        cmp!!.setInteger("time", this.time)
        val stackCmp = NBTTagCompound()
        if (this.colorizer != null) {
            this.colorizer!!.writeToNBT(stackCmp)
        }

        cmp.setTag("colorizer", stackCmp)
    }

    override fun readSharedNBT(cmp: NBTTagCompound?) {
        this.time = cmp!!.getInteger("time")
        val stackCmp = cmp.getCompoundTag("colorizer")
        this.colorizer = ItemStack.loadItemStackFromNBT(stackCmp)
    }
}
