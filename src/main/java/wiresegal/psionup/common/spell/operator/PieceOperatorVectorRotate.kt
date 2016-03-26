package wiresegal.psionup.common.spell.operator

import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.api.spell.SpellParam
import vazkii.psi.api.spell.SpellRuntimeException
import vazkii.psi.api.spell.param.ParamNumber
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceOperator
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:21 PM on 3/23/16.
 */
class PieceOperatorVectorRotate(spell: Spell) : PieceOperator(spell) {
    internal lateinit var vec: SpellParam
    internal lateinit var axis: SpellParam
    internal lateinit var angle: SpellParam

    override fun initParams() {
        vec = ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false)
        axis = ParamVector("${LibMisc.MOD_ID_SHORT}.spellparam.axis", SpellParam.CYAN, false, false)
        angle = ParamNumber("${LibMisc.MOD_ID_SHORT}.spellparam.angle", SpellParam.GREEN, false, false)
        this.addParam(vec)
        this.addParam(axis)
        this.addParam(angle)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val v = this.getParamValue<Vector3>(context, this.vec)
        val axis = this.getParamValue<Vector3>(context, this.axis)
        val angle = this.getParamValue<Double>(context, this.angle)
        if (v == null || axis == null)
            throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)
        return v.rotate(angle, axis)
    }

    override fun getEvaluationType(): Class<*> {
        return Vector3::class.java
    }
}
