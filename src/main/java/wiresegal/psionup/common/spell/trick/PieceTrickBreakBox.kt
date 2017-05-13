package wiresegal.psionup.common.spell.trick

import net.minecraft.util.math.BlockPos
import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamNumber
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceTrick
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock
import wiresegal.psionup.api.BlockProperties
import wiresegal.psionup.api.ParamBlockProperties
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 2:29 PM on 5/13/17.
 */
class PieceTrickBreakBox(spell: Spell) : PieceTrick(spell) {

    private lateinit var min: SpellParam
    private lateinit var max: SpellParam
    private lateinit var total: SpellParam
    private lateinit var mask: SpellParam

    override fun initParams() {
        min = ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.BLUE, false, false)
        max = ParamVector(SpellParam.GENERIC_NAME_VECTOR2, SpellParam.RED, false, false)
        total = ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.GREEN, false, true)
        mask = ParamBlockProperties("${LibMisc.MOD_ID}.spellparam.mask", SpellParam.CYAN, true)
        addParam(min)
        addParam(max)
        addParam(total)
        addParam(mask)
    }

    @Throws(SpellCompilationException::class)
    override fun addToMetadata(meta: SpellMetadata) {
        super.addToMetadata(meta)
        val maxBlocksVal = this.getParamEvaluation<Double>(total)
        if (maxBlocksVal != null && maxBlocksVal > 0.0) {
            meta.addStat(EnumSpellStat.POTENCY, (maxBlocksVal * 8.0).toInt())
            meta.addStat(EnumSpellStat.COST, (maxBlocksVal * 8.0).toInt())
        } else {
            throw SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, this.x, this.y)
        }
    }

    override fun execute(context: SpellContext): Any? {
        val minPos = BlockPos(getParamValue<Vector3>(context, min).toVec3D())
        val maxPos = BlockPos(getParamValue<Vector3>(context, max).toVec3D())
        val totalBlocks = getParamValue<Double>(context, total)
        val properties = getParamValue<BlockProperties?>(context, mask)

        var actions = 0
        for (pos in BlockPos.getAllInBoxMutable(minPos, maxPos)) {
            if (totalBlocks >= actions) break

            if (properties != null) {
                val state = context.caster.world.getBlockState(pos)
                if (state != properties.state)
                    continue
            }

            PieceTrickBreakBlock.removeBlockWithDrops(context, context.caster, context.caster.world, context.tool, pos, true)
            if (context.caster.world.isAirBlock(pos)) actions++
        }
        return null
    }
}
