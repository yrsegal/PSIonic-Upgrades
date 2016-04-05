package wiresegal.psionup.common.lib

/**
 * @author WireSegal
 * Created at 3:24 PM on 3/26/16.
 */
object LibMisc {
    const val MOD_NAME = "PSIonic Upgrades"
    const val MOD_ID = "psionup"

    const val BUILD = "GRADLE:BUILD"
    const val VERSION = "GRADLE:VERSION-$BUILD"
    const val DEPENDENCIES = "required-after:Forge@[12.16.0.1826,);required-after:Psi;after:Botania;"

    const val PROXY_COMMON = "wiresegal.psionup.common.core.CommonProxy"
    const val PROXY_CLIENT = "wiresegal.psionup.client.core.ClientProxy"
}
