package wiresegal.psionup.common.spell.trick

import net.minecraft.block.Block
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
import wiresegal.psionup.common.block.ModBlocks
import wiresegal.psionup.common.block.tile.TileCracklingStar

/**
 * @author WireSegal
 * Created at 4:24 PM on 3/20/16.
 */
open class PieceTrickConjureStar(spell: Spell) : PieceTrick(spell) {
    internal lateinit var position: SpellParam
    internal lateinit var ray: SpellParam
    internal lateinit var time: SpellParam

    override fun initParams() {
        this.position = ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false)
        this.ray = ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false)
        this.time = ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, true, false)
        this.addParam(position)
        this.addParam(ray)
        this.addParam(time)
    }

    @Throws(SpellCompilationException::class)
    override fun addToMetadata(meta: SpellMetadata) {
        super.addToMetadata(meta)
        meta.addStat(EnumSpellStat.POTENCY, 40)
        meta.addStat(EnumSpellStat.COST, 200)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val positionVal = this.getParamValue<Vector3>(context, this.position)
        val timeVal = this.getParamValue<Double>(context, this.time)
        val rayVal = this.getParamValue<Vector3>(context, this.ray)

        if (positionVal == null || rayVal == null) {
            throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)
        } else if (!context.isInRadius(positionVal) || !context.isInRadius(positionVal.copy().add(rayVal))) {
            throw SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS)
        } else {
            val pos = BlockPos(positionVal.x, positionVal.y, positionVal.z)
            val world = context.caster.worldObj
            var state = world.getBlockState(pos)

            if (state.block !== ModBlocks.crackle) placeBlock(world, pos, false)

            state = world.getBlockState(pos)
            if (!world.isRemote && state.block == ModBlocks.crackle) {
                val tile = world.getTileEntity(pos) as TileCracklingStar
                if (timeVal != null && timeVal.toInt() > 0) {
                    val cad = timeVal.toInt()
                    tile.time = cad
                }

                tile.rays.add(rayVal)

                if (tile.colorizer == null) {
                    val cad1 = PsiAPI.getPlayerCAD(context.caster)
                    if (cad1 != null) {
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
                        world.setBlockState(pos, ModBlocks.crackle.defaultState)
                        if (particles) {
                            world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)))
                        }
                    }
                }
            }
        }
    }
}

