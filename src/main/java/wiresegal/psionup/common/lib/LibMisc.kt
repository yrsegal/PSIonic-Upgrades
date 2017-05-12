package wiresegal.psionup.common.lib

/**
 * @author WireSegal
 * Created at 3:24 PM on 3/26/16.
 */
object LibMisc {
    const val MOD_NAME = "PSIonic Upgrades"
    const val MOD_ID = "psionup"

    const val BUILD = "1"
    const val VERSIONID = "15"
    const val VERSION = "$VERSIONID.$BUILD"
    const val DEPENDENCIES = "required-after:forge@[13.20.0.2259,);required-after:psi;after:botania@[r1.9-342,);"

    const val VERSIONS = "1.11.2"

    const val PROXY_COMMON = "wiresegal.psionup.common.core.CommonProxy"
    const val PROXY_CLIENT = "wiresegal.psionup.client.core.ClientProxy"
}
