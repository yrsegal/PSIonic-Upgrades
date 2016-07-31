package wiresegal.psionup.common.spell.operator

import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.api.spell.SpellParam
import vazkii.psi.api.spell.SpellRuntimeException
import vazkii.psi.api.spell.param.ParamEntityListWrapper
import vazkii.psi.api.spell.piece.PieceOperator
import vazkii.psi.api.spell.wrapper.EntityListWrapper

/**
 * @author WireSegal
 * Created at 10:04 PM on 7/30/16.
 */
class PieceOperatorListSize(spell: Spell) : PieceOperator(spell) {

    internal lateinit var list: SpellParam

    override fun initParams() {
        list = ParamEntityListWrapper(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false)
        this.addParam(list)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val l = getParamValue<EntityListWrapper>(context, list)
        return l.unwrap().size.toDouble()
    }

    override fun getEvaluationType(): Class<*> {
        return Double::class.javaObjectType
    }
}
