package wiresegal.psionup.common.spell

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.spell.SpellPiece
import vazkii.psi.common.lib.LibPieceGroups
import vazkii.psi.common.spell.base.ModSpellPieces
import wiresegal.psionup.common.spell.operator.PieceOperatorPlanarNorm
import wiresegal.psionup.common.spell.operator.PieceOperatorVectorRotate
import wiresegal.psionup.common.spell.trick.PieceTrickConjurePulsar
import wiresegal.psionup.common.spell.trick.PieceTrickConjurePulsarLight
import wiresegal.psionup.common.spell.trick.PieceTrickConjurePulsarSequence

/**
 * @author WireSegal
 * Created at 5:05 PM on 3/20/16.
 */
object ModPieces {
    val conjurePulsar: ModSpellPieces.PieceContainer
    val conjurePulsarSequence: ModSpellPieces.PieceContainer
    val conjurePulsarLight: ModSpellPieces.PieceContainer

    val planarNorm: ModSpellPieces.PieceContainer
    val vectorRotate: ModSpellPieces.PieceContainer


    init {
        conjurePulsar = register(PieceTrickConjurePulsar::class.java, "conjurePulsar", LibPieceGroups.BLOCK_CONJURATION)
        conjurePulsarSequence = register(PieceTrickConjurePulsarSequence::class.java, "conjurePulsarSequence", LibPieceGroups.BLOCK_CONJURATION)
        conjurePulsarLight = register(PieceTrickConjurePulsarLight::class.java, "conjurePulsarLight", LibPieceGroups.BLOCK_CONJURATION)

        planarNorm = register(PieceOperatorPlanarNorm::class.java, "planarNorm", LibPieceGroups.SECONDARY_OPERATORS)
        vectorRotate = register(PieceOperatorVectorRotate::class.java, "vectorRotate", LibPieceGroups.SECONDARY_OPERATORS)
    }

    fun registerSpellPieceAndTexture(key: String, clazz: Class<out SpellPiece>) {
        val currMod = Loader.instance().activeModContainer().modId.toLowerCase()
        registerSpellPieceAndTexture(key, currMod, clazz)
    }

    fun registerSpellPieceAndTexture(key: String, mod: String, clazz: Class<out SpellPiece>) {
        PsiAPI.registerSpellPiece("$mod.$key", clazz)
        PsiAPI.simpleSpellTextures.put("$mod.$key", ResourceLocation(mod, "textures/spell/$key.png"))
    }

    fun register(clazz: Class<out SpellPiece>, name: String, group: String): ModSpellPieces.PieceContainer {
        return register(clazz, name, group, false)
    }

    fun register(clazz: Class<out SpellPiece>, name: String, group: String, main: Boolean): ModSpellPieces.PieceContainer {
        registerSpellPieceAndTexture(name, clazz)
        PsiAPI.addPieceToGroup(clazz, group, main)
        return ModSpellPieces.PieceContainer { s -> SpellPiece.create(clazz, s) }
    }

}
