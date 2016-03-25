package wiresegal.psionup.common.spell.operator

import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceOperator

/**
 * @author WireSegal
 * Created at 9:06 PM on 3/23/16.
 */
class PieceOperatorPlanarNorm(spell: Spell) : PieceOperator(spell) {

    internal lateinit var vec: SpellParam

    override fun initParams() {
        vec = ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false)
        this.addParam(vec)
    }

    override fun addToMetadata(meta: SpellMetadata) {
        super.addToMetadata(meta)
        meta.addStat(EnumSpellStat.COMPLEXITY, 2)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val v = this.getParamValue<Vector3>(context, this.vec)
        if (!v.isAxial)
            throw SpellRuntimeException("psionup.spellerror.nonaxial")
        return Vector3(-v.y, v.x + v.z, 0.0).normalize()
    }

    override fun getEvaluationType(): Class<*> {
        return Vector3::class.java
    }
}
