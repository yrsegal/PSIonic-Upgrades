package wiresegal.psionup.common.spell.trick

import vazkii.psi.common.lib.LibPieceGroups
import vazkii.psi.common.spell.base.ModSpellPieces

/**
 * @author WireSegal
 * Created at 5:05 PM on 3/20/16.
 */
object ModPieces {
    var conjurePulsar: ModSpellPieces.PieceContainer
    var conjurePulsarSequence: ModSpellPieces.PieceContainer
    var conjurePulsarLight: ModSpellPieces.PieceContainer

    init {
        conjurePulsar = ModSpellPieces.register(PieceTrickConjurePulsar::class.java, "conjurePulsar", LibPieceGroups.BLOCK_CONJURATION)
        conjurePulsarSequence = ModSpellPieces.register(PieceTrickConjurePulsarSequence::class.java, "conjurePulsarSequence", LibPieceGroups.BLOCK_CONJURATION)
        conjurePulsarLight = ModSpellPieces.register(PieceTrickConjurePulsarLight::class.java, "conjurePulsarLight", LibPieceGroups.BLOCK_CONJURATION)
    }
}
