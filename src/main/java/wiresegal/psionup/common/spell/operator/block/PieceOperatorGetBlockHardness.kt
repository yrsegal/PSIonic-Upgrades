package wiresegal.psionup.common.spell.operator.block

import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.api.spell.SpellParam
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceOperator
import wiresegal.psionup.api.BlockProperties

/**
 * @author WireSegal
 * Created at 2:29 PM on 5/13/17.
 */
class PieceOperatorGetBlockHardness(spell: Spell) : BasePieceOperatorProperties<Double>(spell) {

    override fun getData(context: SpellContext, properties: BlockProperties): Double? = properties.hardness.toDouble()

    override fun getEvaluationType() = Double::class.javaObjectType
}
