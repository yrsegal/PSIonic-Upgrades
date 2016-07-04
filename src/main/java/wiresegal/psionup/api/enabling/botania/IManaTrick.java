package wiresegal.psionup.api.enabling.botania;

import net.minecraft.client.resources.I18n;
import vazkii.psi.api.spell.SpellContext;
import wiresegal.psionup.api.enabling.IComponentPiece;

/**
 * @author WireSegal
 *         Created at 7:10 PM on 3/31/16.
 */
public interface IManaTrick extends IComponentPiece {
    int manaDrain(SpellContext context, int x, int y);

    default EnumManaTier tier() {
        return EnumManaTier.BASE;
    }

    @Override
    default String[] requiredObjects() {
        return new String[]{I18n.format("psionup.requirement." + tier().toString())};
    }
}
