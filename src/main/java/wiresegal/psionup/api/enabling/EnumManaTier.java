package wiresegal.psionup.api.enabling;

/**
 * @author WireSegal
 *         Created at 9:54 PM on 4/2/16.
 */
public enum EnumManaTier {
    BASE,
    ALFHEIM,
    GAIA,
    RELICS;

    public static boolean allowed(EnumManaTier cadTier, EnumManaTier pieceTier) {
        return cadTier.ordinal() >= pieceTier.ordinal();
    }
}
