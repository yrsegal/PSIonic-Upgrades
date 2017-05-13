package wiresegal.psionup.common.spell.operator.block

import net.minecraft.util.math.BlockPos
import vazkii.psi.api.internal.Vector3
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
class PieceOperatorGetBlockProperties(spell: Spell) : PieceOperator(spell) {
    private lateinit var pos: SpellParam

    override fun initParams() {
        pos = ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.YELLOW, false, false)
        addParam(pos)
    }

    override fun execute(context: SpellContext): Any? {
        val positionVec = getParamValue<Vector3>(context, pos)
        val position = BlockPos(positionVec.toVec3D())
        val world = context.focalPoint.world
        val properties = BlockProperties(world.getBlockState(position), position, world)
        return properties
    }

    override fun getEvaluationType() = BlockProperties::class.java
}
