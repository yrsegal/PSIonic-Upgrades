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

        const val EBONY_HELMET = "ebonyHelmet"
        const val EBONY_CHEST = "ebonyChestplate"
        const val EBONY_LEGS = "ebonyLeggings"
        const val EBONY_BOOTS = "ebonyBoots"

        const val IVORY_HELMET = "ivoryHelmet"
        const val IVORY_CHEST = "ivoryChestplate"
        const val IVORY_LEGS = "ivoryLeggings"
        const val IVORY_BOOTS = "ivoryBoots"

        const val FLASH_RING = "flashRing"

        const val EBONY_PICKAXE = "ebonyPickaxe"
        const val EBONY_SHOVEL = "ebonyShovel"
        const val EBONY_AXE = "ebonyAxe"
        const val EBONY_SWORD = "ebonySword"

        const val IVORY_PICKAXE = "ivoryPickaxe"
        const val IVORY_SHOVEL = "ivoryShovel"
        const val IVORY_AXE = "ivoryAxe"
        const val IVORY_SWORD = "ivorySword"

        const val BIOTIC_SENSOR = "bioticSensor"

        const val GAUSS_RIFLE = "gaussRifle"

        const val GAUSS_BULLET = "gaussBullet"

        const val LIVINGWOOD_CAD = "cadAssemblyBlaster"
        const val LIVINGWOOD_CAD_MODEL = "cadBlaster"

        const val UNSTABLE_BATTERY = "unstableBattery"
        const val TWINFLOW_BATTERY = "twinflowBattery"
    }

    object Blocks {
        const val CONJURED_PULSAR = "conjuredPulsar"
        const val CONJURED_STAR = "conjuredStar"

        const val BRIGHT_PLATE = "brightPlate"
        const val DARK_PLATE = "darkPlate"

        const val CAD_CASE = "cadCase"
    }

    object Spell {
        const val CONJURE_PULSAR = "conjurePulsar"
        const val CONJURE_PULSAR_SEQUENCE = "conjurePulsarSequence"
        const val CONJURE_PULSAR_LIGHT = "conjurePulsarLight"

        const val PLANAR_NORMAL_VECTOR = "planarNorm"
        const val VECTOR_ROTATE = "vectorRotate"

        const val STRONG_VECTOR_RAYCAST = "strongVectorRaycast"
        const val STRONG_VECTOR_RAYCAST_AXIS = "strongVectorRaycastAxis"

        const val VECTOR_FALLBACK = "vectorFallback"

        const val PARTICLE_TRAIL = "particleTrail"

        const val CONJURE_CRACKLE = "conjureCrackle"

        const val BREAK_LOOP = "loopcastBreak"

        const val LIST_SIZE = "listSize"


        const val GET_PROPERTIES = "getProperties"
        const val GET_HARDNESS = "getHardness"
        const val GET_LIGHT = "getLight"
        const val GET_SOLIDITY = "getSolidity"
        const val GET_COMPARATOR = "getComparator"
        const val EQUALITY = "operatorEquality"
        const val BREAK_BOX = "breakBox"
        const val CLONE_BOX = "cloneBox"

        const val SPAMLESS = "spamlessDebug"

        const val MAKE_BURST = "makeManaBurst"
        const val WILD_DRUM = "drumOfTheWild"
        const val CANOPY_DRUM = "drumOfTheCanopy"
        const val GATHERING_DRUM = "drumOfTheGathering"
    }

    object PieceGroups {
        const val ALTERNATE_CONJURATION = "${LibMisc.MOD_ID}.redstone_conjuration"
        const val SECONDARY_VECTOR_OPERATORS = "${LibMisc.MOD_ID}.secondary_vectors"
        const val BLOCK_PROPERTIES = "${LibMisc.MOD_ID}.block_properties"

        const val MANA_PSIONICS = "${LibMisc.MOD_ID}.mana_psionics"
    }

    object Entities {
        const val GAUSS_PULSE = "gauss_pulse"
    }

    object Potions {
        const val PSISHOCK = "psishock"
        const val PSISHOCK_STRONG = "psishock_strong"
        const val PSISHOCK_LONG = "psishock_long"

        const val PSIPULSE = "psipulse"
        const val PSIPULSE_STRONG = "psipulse_strong"
        const val PSIPULSE_LONG = "psipulse_long"
    }

    val Colors = arrayOf("White", "Orange", "Magenta", "LightBlue",
            "Yellow", "Lime", "Pink", "Gray",
            "LightGray", "Cyan", "Purple", "Blue",
            "Brown", "Green", "Red", "Black")
}
