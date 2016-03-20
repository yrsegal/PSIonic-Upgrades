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
import vazkii.psi.common.block.tile.TileConjured
import wiresegal.psionup.common.block.ModBlocks

/**
 * @author WireSegal
 * Created at 4:24 PM on 3/20/16.
 */
class PieceTrickConjurePulsarLight(spell: Spell) : PieceTrickConjurePulsar(spell) {

    override fun addStats(meta: SpellMetadata) {
        meta.addStat(EnumSpellStat.POTENCY, 60)
        meta.addStat(EnumSpellStat.COST, 210)
        meta.addStat(EnumSpellStat.COMPLEXITY, 2)
    }

    override fun messWithState(state: IBlockState): IBlockState {
        return state.withProperty(BlockConjured.LIGHT, true)
    }
}

