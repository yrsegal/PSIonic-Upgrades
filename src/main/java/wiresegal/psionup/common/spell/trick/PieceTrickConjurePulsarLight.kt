package wiresegal.psionup.common.spell.trick

import net.minecraft.block.state.IBlockState
import vazkii.psi.api.spell.EnumSpellStat
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellMetadata
import vazkii.psi.common.block.BlockConjured

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

