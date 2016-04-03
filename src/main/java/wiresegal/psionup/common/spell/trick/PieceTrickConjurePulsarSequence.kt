package wiresegal.psionup.common.spell.trick

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamNumber
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceTrick
import vazkii.psi.common.block.BlockConjured
import wiresegal.psionup.common.block.ModBlocks
import wiresegal.psionup.common.block.tile.TileConjuredPulsar

/**
 * @author WireSegal
 * Created at 4:53 PM on 3/20/16.
 */
class PieceTrickConjurePulsarSequence(spell: Spell) : PieceTrick(spell) {
    internal lateinit var position: SpellParam
    internal lateinit var target: SpellParam
    internal lateinit var maxBlocks: SpellParam
    internal lateinit var time: SpellParam

    override fun initParams() {
        this.position = ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false)
        this.target = ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false)
        this.maxBlocks = ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true)
        this.time = ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false)
        this.addParam(position)
        this.addParam(target)
        this.addParam(maxBlocks)
        this.addParam(time)
    }

    @Throws(SpellCompilationException::class)
    override fun addToMetadata(meta: SpellMetadata) {
        super.addToMetadata(meta)
        val maxBlocksVal = this.getParamEvaluation<Double>(this.maxBlocks)
        if (maxBlocksVal != null && maxBlocksVal.toDouble() > 0.0) {
            meta.addStat(EnumSpellStat.POTENCY, (maxBlocksVal.toDouble() * 20.0).toInt())
            meta.addStat(EnumSpellStat.COST, (maxBlocksVal.toDouble() * 30.0).toInt())
        } else {
            throw SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, this.x, this.y)
        }
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val positionVal = this.getParamValue<Vector3>(context, this.position)
        val targetVal = this.getParamValue<Vector3>(context, this.target)
        val maxBlocksVal = this.getParamValue<Double>(context, this.maxBlocks)
        val timeVal = this.getParamValue<Double>(context, this.time)
        val maxBlocksInt = maxBlocksVal.toInt()
        if (positionVal == null) {
            throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)
        } else if (targetVal != null) {
            val len = targetVal.mag().toInt()
            val targetNorm = targetVal.copy().normalize()
            val cad = PsiAPI.getPlayerCAD(context.caster)

            for (i in 0..Math.min(len, maxBlocksInt) - 1) {
                val blockVec = positionVal.copy().add(targetNorm.copy().multiply(i.toDouble()))
                if (!context.isInRadius(blockVec)) {
                    throw SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS)
                }

                val pos = BlockPos(blockVec.x, blockVec.y, blockVec.z)
                val world = context.caster.worldObj
                var state = world.getBlockState(pos)
                if (state.block !== ModBlocks.conjured) {
                    PieceTrickConjurePulsar.placeBlock(world, pos, false)
                    state = world.getBlockState(pos)
                    if (!world.isRemote && state.block === ModBlocks.conjured) {
                        world.setBlockState(pos, this.messWithState(state))
                        val tile = world.getTileEntity(pos) as TileConjuredPulsar
                        if (timeVal != null && timeVal.toInt() > 0) {
                            val time = timeVal.toInt()
                            tile.time = time
                        }

                        if (cad != null) {
                            tile.colorizer = (cad.item as ICAD).getComponentInSlot(cad, EnumCADComponent.DYE)
                        }
                    }
                }
            }
        }
        return null
    }

    fun messWithState(state: IBlockState): IBlockState {
        return state.withProperty(BlockConjured.SOLID, true)
    }
}
