package wiresegal.psionup.common.spell

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.spell.SpellPiece
import vazkii.psi.common.lib.LibPieceGroups
import vazkii.psi.common.spell.base.ModSpellPieces
import wiresegal.psionup.common.lib.LibNames
import wiresegal.psionup.common.spell.operator.PieceOperatorPlanarNorm
import wiresegal.psionup.common.spell.operator.PieceOperatorVectorRotate
import wiresegal.psionup.common.spell.operator.PieceOperatorVectorStrongRaycast
import wiresegal.psionup.common.spell.operator.PieceOperatorVectorStrongRaycastAxis
import wiresegal.psionup.common.spell.trick.PieceTrickConjurePulsar
import wiresegal.psionup.common.spell.trick.PieceTrickConjurePulsarLight
import wiresegal.psionup.common.spell.trick.PieceTrickConjurePulsarSequence
import wiresegal.psionup.common.spell.trick.PieceTrickParticleTrail
import wiresegal.psionup.common.spell.trick.botania.CompatTricks

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

    val strongCast: ModSpellPieces.PieceContainer
    val strongCastAxis: ModSpellPieces.PieceContainer

    val particleTrail: ModSpellPieces.PieceContainer

    init {
        conjurePulsar = register(PieceTrickConjurePulsar::class.java,
                LibNames.Spell.CONJURE_PULSAR,
                LibPieceGroups.BLOCK_CONJURATION)
        conjurePulsarSequence = register(PieceTrickConjurePulsarSequence::class.java,
                LibNames.Spell.CONJURE_PULSAR_SEQUENCE,
                LibPieceGroups.BLOCK_CONJURATION)
        conjurePulsarLight = register(PieceTrickConjurePulsarLight::class.java,
                LibNames.Spell.CONJURE_PULSAR_LIGHT,
                LibPieceGroups.BLOCK_CONJURATION)

        planarNorm = register(PieceOperatorPlanarNorm::class.java,
                LibNames.Spell.PLANAR_NORMAL_VECTOR,
                LibPieceGroups.SECONDARY_OPERATORS)
        vectorRotate = register(PieceOperatorVectorRotate::class.java,
                LibNames.Spell.VECTOR_ROTATE,
                LibPieceGroups.SECONDARY_OPERATORS)

        strongCast = register(PieceOperatorVectorStrongRaycast::class.java,
                LibNames.Spell.STRONG_VECTOR_RAYCAST,
                LibPieceGroups.TUTORIAL_4)
        strongCastAxis = register(PieceOperatorVectorStrongRaycastAxis::class.java,
                LibNames.Spell.STRONG_VECTOR_RAYCAST_AXIS,
                LibPieceGroups.BLOCK_WORKS)

        particleTrail = register(PieceTrickParticleTrail::class.java,
                LibNames.Spell.PARTICLE_TRAIL,
                LibPieceGroups.DETECTION_DYNAMICS)

        if (Loader.isModLoaded("Botania"))
            CompatTricks.init()
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
