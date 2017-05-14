package wiresegal.psionup.common.spell

import com.teamwizardry.librarianlib.features.helpers.VariantHelper
import com.teamwizardry.librarianlib.features.helpers.currentModId
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.spell.SpellPiece
import vazkii.psi.common.lib.LibPieceGroups
import vazkii.psi.common.spell.base.ModSpellPieces
import vazkii.psi.common.spell.base.ModSpellPieces.PieceContainer
import wiresegal.psionup.common.lib.LibNames
import wiresegal.psionup.common.spell.operator.*
import wiresegal.psionup.common.spell.operator.block.*
import wiresegal.psionup.common.spell.trick.*

/**
 * @author WireSegal
 * Created at 5:05 PM on 3/20/16.
 */
object ModPieces {

    val conjurePulsar: PieceContainer
    val conjurePulsarSequence: PieceContainer
    val conjurePulsarLight: PieceContainer

    val planarNorm: PieceContainer
    val vectorRotate: PieceContainer

    val getProperties: PieceContainer
    val getHardness: PieceContainer
    val getLight: PieceContainer
    val getSolidity: PieceContainer
    val getComparator: PieceContainer

    val strongCast: PieceContainer
    val strongCastAxis: PieceContainer

    val vectorFallback: PieceContainer

    val particleTrail: PieceContainer

    val conjureCrackle: PieceContainer

    val loopcastBreak: PieceContainer

    val listSize: PieceContainer

    val equality: PieceContainer

    val breakBox: PieceContainer
    val cloneBox: PieceContainer

    val debugSpamless: PieceContainer

    init {

        PsiAPI.setGroupRequirements(LibNames.PieceGroups.ALTERNATE_CONJURATION, 21, LibPieceGroups.BLOCK_CONJURATION)
        PsiAPI.setGroupRequirements(LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS, 21, LibPieceGroups.TRIGNOMETRY)
        PsiAPI.setGroupRequirements(LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS, 21, LibPieceGroups.TRIGNOMETRY)
        PsiAPI.setGroupRequirements(LibNames.PieceGroups.BLOCK_PROPERTIES, 21, LibPieceGroups.BLOCK_CONJURATION)

        conjurePulsar = register(PieceTrickConjurePulsar::class.java,
                LibNames.Spell.CONJURE_PULSAR,
                LibNames.PieceGroups.ALTERNATE_CONJURATION,
                true)
        conjurePulsarSequence = register(PieceTrickConjurePulsarSequence::class.java,
                LibNames.Spell.CONJURE_PULSAR_SEQUENCE,
                LibNames.PieceGroups.ALTERNATE_CONJURATION)
        conjurePulsarLight = register(PieceTrickConjurePulsarLight::class.java,
                LibNames.Spell.CONJURE_PULSAR_LIGHT,
                LibNames.PieceGroups.ALTERNATE_CONJURATION)

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

        vectorFallback = register(PieceOperatorVectorFallback::class.java,
                LibNames.Spell.VECTOR_FALLBACK,
                LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS)

        particleTrail = register(PieceTrickParticleTrail::class.java,
                LibNames.Spell.PARTICLE_TRAIL,
                LibNames.PieceGroups.SECONDARY_VECTOR_OPERATORS)

        conjureCrackle = register(PieceTrickConjureStar::class.java,
                LibNames.Spell.CONJURE_CRACKLE,
                LibNames.PieceGroups.ALTERNATE_CONJURATION)

        loopcastBreak = register(PieceTrickBreakLoop::class.java,
                LibNames.Spell.BREAK_LOOP,
                LibPieceGroups.FLOW_CONTROL)

        listSize = register(PieceOperatorListSize::class.java,
                LibNames.Spell.LIST_SIZE,
                LibPieceGroups.ENTITIES_INTRO)

        getProperties = register(PieceOperatorGetBlockProperties::class.java,
                LibNames.Spell.GET_PROPERTIES,
                LibNames.PieceGroups.BLOCK_PROPERTIES,
                true)

        getHardness = register(PieceOperatorGetBlockHardness::class.java,
                LibNames.Spell.GET_HARDNESS,
                LibNames.PieceGroups.BLOCK_PROPERTIES)

        getLight = register(PieceOperatorGetBlockLight::class.java,
                LibNames.Spell.GET_LIGHT,
                LibNames.PieceGroups.BLOCK_PROPERTIES)

        getSolidity = register(PieceOperatorGetBlockSolidity::class.java,
                LibNames.Spell.GET_SOLIDITY,
                LibNames.PieceGroups.BLOCK_PROPERTIES)

        getComparator = register(PieceOperatorGetComparatorStrength::class.java,
                LibNames.Spell.GET_COMPARATOR,
                LibNames.PieceGroups.BLOCK_PROPERTIES)

        equality = register(PieceOperatorEquality::class.java,
                LibNames.Spell.EQUALITY,
                LibNames.PieceGroups.BLOCK_PROPERTIES)


        breakBox = register(PieceTrickBreakBox::class.java,
                LibNames.Spell.BREAK_BOX,
                LibNames.PieceGroups.BLOCK_PROPERTIES)

        cloneBox = register(PieceTrickCloneBox::class.java,
                LibNames.Spell.CLONE_BOX,
                LibNames.PieceGroups.BLOCK_PROPERTIES)

        debugSpamless = register(PieceTrickDebugSpamless::class.java,
                LibNames.Spell.SPAMLESS,
                LibPieceGroups.TUTORIAL_1)

        if (Loader.isModLoaded("botania"))
            CompatTricks.init()
    }

    fun registerSpellPieceAndTexture(key: String, clazz: Class<out SpellPiece>) {
        val currMod = currentModId.toLowerCase()
        registerSpellPieceAndTexture(key, currMod, clazz)
    }

    fun registerSpellPieceAndTexture(key: String, mod: String, clazz: Class<out SpellPiece>) {
        PsiAPI.registerSpellPiece("$mod.$key", clazz)
        PsiAPI.simpleSpellTextures.put("$mod.$key", ResourceLocation(mod, "textures/spell/$key.png"))
    }

    fun register(clazz: Class<out SpellPiece>, name: String, group: String): PieceContainer {
        return register(clazz, name, group, false)
    }

    fun register(clazz: Class<out SpellPiece>, name: String, group: String, main: Boolean): PieceContainer {
        val newName = VariantHelper.toSnakeCase(name)
        registerSpellPieceAndTexture(newName, clazz)
        PsiAPI.addPieceToGroup(clazz, group, main)
        return PieceContainer { s -> SpellPiece.create(clazz, s) }
    }

}
