package wiresegal.psionup.common.lib

/**
 * @author WireSegal
 * Created at 3:24 PM on 3/26/16.
 */
object LibMisc {
    const val MOD_NAME = "PSIonic Upgrades"
    const val MOD_ID = "psionup"

    const val BUILD = "GRADLE:BUILD"
    const val VERSIONID = "GRADLE:VERSION"
    const val VERSION = "$VERSIONID.$BUILD"
    const val DEPENDENCIES = "required-after:Forge@[12.17.0.1909,);required-after:Psi;after:Botania[r1.8-301,);"

    const val PROXY_COMMON = "wiresegal.psionup.common.core.CommonProxy"
    const val PROXY_CLIENT = "wiresegal.psionup.client.core.ClientProxy"
}
