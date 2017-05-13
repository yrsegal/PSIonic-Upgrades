package wiresegal.psionup.common.spell.operator.block

import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.api.spell.SpellParam
import vazkii.psi.api.spell.piece.PieceOperator
import wiresegal.psionup.api.BlockProperties
import wiresegal.psionup.api.ParamBlockProperties

/**
 * @author WireSegal
 * Created at 2:29 PM on 5/13/17.
 */
abstract class BasePieceOperatorProperties<T : Any>(spell: Spell) : PieceOperator(spell) {
    private lateinit var properties: SpellParam

    override fun initParams() {
        properties = ParamBlockProperties(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false)
        addParam(properties)
    }

    override fun execute(context: SpellContext): T? {
        val props = getParamValue<BlockProperties>(context, properties)
        return getData(context, props)
    }

    abstract fun getData(context: SpellContext, properties: BlockProperties): T?

    override abstract fun getEvaluationType(): Class<T>
}
