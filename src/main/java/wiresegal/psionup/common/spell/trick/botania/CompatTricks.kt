package wiresegal.psionup.common.spell.trick.botania

import vazkii.psi.api.PsiAPI
import vazkii.psi.common.lib.LibPieceGroups
import vazkii.psi.common.spell.base.ModSpellPieces
import wiresegal.psionup.common.lib.LibNames
import wiresegal.psionup.common.spell.ModPieces

/**
 * @author WireSegal
 * Created at 9:01 AM on 4/1/16.
 */
object CompatTricks {
    var isInitialized = false

    lateinit var makeBurst: ModSpellPieces.PieceContainer

    fun init() {
        isInitialized = true

        PsiAPI.setGroupRequirements(LibNames.PieceGroups.BURST_CONJURATION, 16, LibPieceGroups.GREATER_INFUSION, LibPieceGroups.ELEMENTAL_ARTS)

        makeBurst = ModPieces.register(PieceTrickFormBurst::class.java, LibNames.Spell.MAKE_BURST, LibNames.PieceGroups.BURST_CONJURATION, true)
    }
}
