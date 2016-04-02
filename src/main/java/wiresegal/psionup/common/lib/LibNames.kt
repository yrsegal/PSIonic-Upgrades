package wiresegal.psionup.common.lib

/**
 * @author WireSegal
 * Created at 3:33 PM on 3/26/16.
 */
object LibNames {
    object Items {
        const val LIQUID_INK_COLORIZER = "liquidColorizer"
        const val DRAINED_COLORIZER = "emptyColorizer"
        const val INLINE_CASTER = "fakeCAD"
        const val SPELL_MAGAZINE = "magazine"
        const val WIDE_BAND_SOCKET = "wideSocket"
        const val LIVINGWOOD_CAD = "cadAssemblyLivingwood"
        const val LIVINGWOOD_CAD_MODEL = "cadLivingwood"
    }

    object Blocks {
        const val CONJURED_PULSAR = "conjuredPulsar"
    }

    object Spell {
        const val CONJURE_PULSAR = "conjurePulsar"
        const val CONJURE_PULSAR_SEQUENCE = "conjurePulsarSequence"
        const val CONJURE_PULSAR_LIGHT = "conjurePulsarLight"

        const val PLANAR_NORMAL_VECTOR = "planarNorm"
        const val VECTOR_ROTATE = "vectorRotate"

        const val STRONG_VECTOR_RAYCAST = "strongVectorRaycast"
        const val STRONG_VECTOR_RAYCAST_AXIS = "strongVectorRaycastAxis"

        const val PARTICLE_TRAIL = "particleTrail"

        const val MAKE_BURST = "makeManaBurst"
    }

    object PieceGroups {
        const val REDSTONE_CONJURATION = "${LibMisc.MOD_ID_SHORT}.redstoneConjuration"
        const val SECONDARY_VECTOR_OPERATORS = "${LibMisc.MOD_ID_SHORT}.secondaryVectors"

        const val BURST_CONJURATION = "${LibMisc.MOD_ID_SHORT}.burstConjuration"
    }
}
