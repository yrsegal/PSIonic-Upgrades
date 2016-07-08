package wiresegal.psionup.common.spell.operator

import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.api.spell.SpellParam
import vazkii.psi.api.spell.SpellRuntimeException
import vazkii.psi.api.spell.param.ParamNumber
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceOperator

class PieceOperatorVectorStrongRaycastAxis(spell: Spell) : PieceOperator(spell) {
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
            val pos = PieceOperatorVectorStrongRaycast.raycast(context.caster.worldObj, originVal, rayVal, maxLen)
            if (pos != null && pos.blockPos != null) {
                val facing = pos.sideHit
                return Vector3(facing.frontOffsetX.toDouble(), facing.frontOffsetY.toDouble(), facing.frontOffsetZ.toDouble())
            } else {
                throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)
            }
        } else {
            throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)
        }
    }

    override fun getEvaluationType(): Class<*> {
        return Vector3::class.java
    }
}

