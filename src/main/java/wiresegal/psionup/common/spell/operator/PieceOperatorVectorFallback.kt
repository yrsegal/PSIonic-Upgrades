package wiresegal.psionup.common.spell.operator

import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.api.spell.SpellParam
import vazkii.psi.api.spell.SpellRuntimeException
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceOperator
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:06 PM on 3/23/16.
 */
class PieceOperatorVectorFallback(spell: Spell) : PieceOperator(spell) {

    internal lateinit var vec: SpellParam
    internal lateinit var fallback: SpellParam

    override fun initParams() {
        vec = ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false)
        fallback = ParamVector("${LibMisc.MOD_ID}.spellparam.fallback", SpellParam.GREEN, false, false)
        this.addParam(vec)
        this.addParam(fallback)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val v = this.getParamValue<Vector3>(context, this.vec)
        val fallback = this.getParamValue<Vector3>(context, this.fallback)
        return if (v == null || v.isZero) fallback else v
    }

    override fun getEvaluationType(): Class<*> {
        return Vector3::class.java
    }
}
