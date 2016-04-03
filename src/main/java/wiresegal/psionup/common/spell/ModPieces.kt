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
import wiresegal.psionup.common.spell.CompatTricks

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

        PsiAPI.setGroupRequirements(LibNames.PieceGroups.REDSTONE_CONJURATION, 21, LibPieceGroups.BLOCK_CONJURATION)
        PsiAPI.setGroupRequirements(LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS, 21, LibPieceGroups.TRIGNOMETRY)

        conjurePulsar = register(PieceTrickConjurePulsar::class.java,
                LibNames.Spell.CONJURE_PULSAR,
                LibNames.PieceGroups.REDSTONE_CONJURATION,
                true)
        conjurePulsarSequence = register(PieceTrickConjurePulsarSequence::class.java,
                LibNames.Spell.CONJURE_PULSAR_SEQUENCE,
                LibNames.PieceGroups.REDSTONE_CONJURATION)
        conjurePulsarLight = register(PieceTrickConjurePulsarLight::class.java,
                LibNames.Spell.CONJURE_PULSAR_LIGHT,
                LibNames.PieceGroups.REDSTONE_CONJURATION)

        planarNorm = register(PieceOperatorPlanarNorm::class.java,
                LibNames.Spell.PLANAR_NORMAL_VECTOR,
                LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS,
                true)
        vectorRotate = register(PieceOperatorVectorRotate::class.java,
                LibNames.Spell.VECTOR_ROTATE,
                LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS)

        strongCast = register(PieceOperatorVectorStrongRaycast::class.java,
                LibNames.Spell.STRONG_VECTOR_RAYCAST,
                LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS)
        strongCastAxis = register(PieceOperatorVectorStrongRaycastAxis::class.java,
                LibNames.Spell.STRONG_VECTOR_RAYCAST_AXIS,
                LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS)

        particleTrail = register(PieceTrickParticleTrail::class.java,
                LibNames.Spell.PARTICLE_TRAIL,
                LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS)

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
