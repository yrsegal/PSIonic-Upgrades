package wiresegal.psionup.common.spell.operator

import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamAny
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceOperator
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:06 PM on 3/23/16.
 */
class PieceOperatorEquality(spell: Spell) : PieceOperator(spell) {

    private lateinit var first: SpellParam
    private lateinit var second: SpellParam

    override fun initParams() {
        first = ParamAny("${LibMisc.MOD_ID}.spellparam.target_1", SpellParam.RED, false)
        second = ParamAny("${LibMisc.MOD_ID}.spellparam.target_2", SpellParam.BLUE, true)
        addParam(first)
        addParam(second)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val a = getParamValue<Any?>(context, first)
        val b = getParamValue<Any?>(context, second)
        return if (a == b) 1.0 else 0.0
    }

    override fun getEvaluationType(): Class<*> {
        return Double::class.javaObjectType
    }
}
