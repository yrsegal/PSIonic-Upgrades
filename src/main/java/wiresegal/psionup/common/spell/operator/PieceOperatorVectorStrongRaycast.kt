package wiresegal.psionup.common.spell.operator

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.api.spell.SpellParam
import vazkii.psi.api.spell.SpellRuntimeException
import vazkii.psi.api.spell.param.ParamNumber
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceOperator

/**
 * @author WireSegal
 * Created at 7:06 PM on 3/24/16.
 */

class PieceOperatorVectorStrongRaycast(spell: Spell) : PieceOperator(spell) {
    internal lateinit var origin: SpellParam
    internal lateinit var ray: SpellParam
    internal lateinit var max: SpellParam

    override fun initParams() {
        this.origin = ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false)
        this.ray = ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false)
        this.max = ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.PURPLE, true, false)
        this.addParam(origin)
        this.addParam(ray)
        this.addParam(max)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val originVal = this.getParamValue<Vector3>(context, this.origin)
        val rayVal = this.getParamValue<Vector3>(context, this.ray)
        if (originVal != null && rayVal != null) {
            var maxLen = 32.0
            val numberVal = this.getParamValue<Double>(context, this.max)
            if (numberVal != null) {
                maxLen = numberVal.toDouble()
            }

            maxLen = Math.min(32.0, maxLen)
            val pos = raycast(context.caster.worldObj, originVal, rayVal, maxLen)
            if (pos != null && pos.blockPos != null) {
                return Vector3(pos.blockPos.x.toDouble(), pos.blockPos.y.toDouble(), pos.blockPos.z.toDouble())
            } else {
                return null
            }
        } else {
            return null
        }
    }

    override fun getEvaluationType(): Class<*> {
        return Vector3::class.java
    }

    companion object {
        @Throws(SpellRuntimeException::class)
        fun raycast(e: Entity, len: Double): RayTraceResult? {
            val vec = Vector3.fromEntity(e)
            if (e is EntityPlayer) {
                vec.add(0.0, e.getEyeHeight().toDouble(), 0.0)
            }

            val look = e.lookVec
            if (look == null) {
                throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)
            } else {
                return raycast(e.worldObj, vec, Vector3(look), len)
            }
        }

        fun raycast(world: World, origin: Vector3, ray: Vector3, len: Double): RayTraceResult? {
            val end = origin.copy().add(ray.copy().normalize().multiply(len))
            val pos = world.rayTraceBlocks(origin.toVec3D(), end.toVec3D(), false, true, false)
            return pos
        }
    }
}
