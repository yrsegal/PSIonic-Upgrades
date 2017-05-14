package wiresegal.psionup.common.spell.trick

import net.minecraft.util.math.BlockPos
import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceTrick
import vazkii.psi.common.spell.trick.block.PieceTrickPlaceBlock
import wiresegal.psionup.api.BlockProperties
import wiresegal.psionup.api.ParamBlockProperties
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 2:29 PM on 5/13/17.
 */
class PieceTrickCloneBox(spell: Spell) : PieceTrick(spell) {

    private lateinit var min: SpellParam
    private lateinit var max: SpellParam
    private lateinit var base: SpellParam
    private lateinit var mask: SpellParam

    override fun initParams() {
        min = ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.BLUE, false, false)
        max = ParamVector(SpellParam.GENERIC_NAME_VECTOR2, SpellParam.RED, false, false)
        base = ParamVector(SpellParam.GENERIC_NAME_BASE, SpellParam.GREEN, false, false)
        mask = ParamBlockProperties("${LibMisc.MOD_ID}.spellparam.mask", SpellParam.CYAN, false)
        addParam(min)
        addParam(max)
        addParam(base)
        addParam(mask)
    }

    @Throws(SpellCompilationException::class)
    override fun addToMetadata(meta: SpellMetadata) {
        super.addToMetadata(meta)
        meta.addStat(EnumSpellStat.POTENCY, 200)
        meta.addStat(EnumSpellStat.COST, 1800)
    }

    override fun execute(context: SpellContext): Any? {
        val minPos = BlockPos(getParamValue<Vector3>(context, min).toVec3D())
        val maxPos = BlockPos(getParamValue<Vector3>(context, max).toVec3D())
        val basePos = BlockPos(getParamValue<Vector3>(context, base).toVec3D())
        val properties = getParamValue<BlockProperties>(context, mask)

        val actualMin = BlockPos(Math.min(minPos.x, maxPos.x), Math.min(minPos.y, maxPos.y), Math.min(minPos.z, maxPos.z))

        val xHeight = Math.abs(minPos.x - maxPos.x)
        val yHeight = Math.abs(minPos.y - maxPos.y)
        val zHeight = Math.abs(minPos.z - maxPos.z)


        val mutTest = BlockPos.MutableBlockPos()
        val mutPlace = BlockPos.MutableBlockPos()

        for (x in 0..xHeight) for (y in 0..yHeight) for (z in 0..zHeight) {
            mutTest.setPos(actualMin.x + x, actualMin.y + y, actualMin.z + z)
            mutPlace.setPos(basePos.x + x, basePos.y + y, basePos.z + z)

            val state = context.caster.world.getBlockState(mutTest)
            if (state == properties.state)
                PieceTrickPlaceBlock.placeBlock(context.caster, context.caster.world, mutPlace, context.targetSlot, mutPlace == minPos || mutPlace == maxPos)
        }
        return null
    }
}
