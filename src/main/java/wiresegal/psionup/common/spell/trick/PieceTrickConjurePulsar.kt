package wiresegal.psionup.common.spell.trick

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
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
 * Created at 4:24 PM on 3/20/16.
 */
open class PieceTrickConjurePulsar(spell: Spell) : PieceTrick(spell) {
    internal lateinit var position: SpellParam
    internal lateinit var time: SpellParam

    override fun initParams() {
        this.position = ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false)
        this.time = ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, true, false)
        this.addParam(position)
        this.addParam(time)
    }

    @Throws(SpellCompilationException::class)
    override fun addToMetadata(meta: SpellMetadata) {
        super.addToMetadata(meta)
        addStats(meta)
    }

    open fun addStats(meta: SpellMetadata) {
        meta.addStat(EnumSpellStat.POTENCY, 20)
        meta.addStat(EnumSpellStat.COST, 30)
        meta.addStat(EnumSpellStat.COMPLEXITY, 2)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext?): Any? {
        val positionVal = this.getParamValue<Vector3>(context, this.position)
        val timeVal = this.getParamValue<Double>(context, this.time)
        if (positionVal == null) {
            throw SpellRuntimeException("psi.spellerror.nullvector")
        } else if (!context!!.isInRadius(positionVal)) {
            throw SpellRuntimeException("psi.spellerror.outsideradius")
        } else {
            val pos = BlockPos(positionVal.x, positionVal.y, positionVal.z)
            val world = context.caster.world
            var state = world.getBlockState(pos)
            if (state.block !== ModBlocks.conjured) {
                placeBlock(world, pos, false)
                state = world.getBlockState(pos)
                if (!world.isRemote && state.block === ModBlocks.conjured) {
                    world.setBlockState(pos, this.messWithState(state))
                    val tile = world.getTileEntity(pos) as TileConjuredPulsar
                    if (timeVal != null && timeVal.toInt() > 0) {
                        val cad = timeVal.toInt()
                        tile.time = cad
                    }

                    val cad1 = PsiAPI.getPlayerCAD(context.caster)
                    if (!cad1.isEmpty) {
                        tile.colorizer = (cad1.item as ICAD).getComponentInSlot(cad1, EnumCADComponent.DYE)
                    }
                }
            }

            return null
        }
    }

    companion object {
        fun placeBlock(world: World, pos: BlockPos, particles: Boolean) {
            if (world.isBlockLoaded(pos)) {
                val state = world.getBlockState(pos)
                val block = state.block
                if (block == null || block.isAir(state, world, pos) || block.isReplaceable(world, pos)) {
                    if (!world.isRemote) {
                        world.setBlockState(pos, ModBlocks.conjured.defaultState)
                        if (particles) {
                            world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)))
                        }
                    }
                }
            }
        }
    }

    open fun messWithState(state: IBlockState): IBlockState {
        return state.withProperty(BlockConjured.SOLID, true)
    }
}

