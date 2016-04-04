package wiresegal.psionup.api.enabling;

import net.minecraft.util.text.translation.I18n;
import vazkii.psi.api.spell.SpellContext;

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
        return new String[] {I18n.translateToLocal("psionup.requirement." + tier().toString())};
    }
}
