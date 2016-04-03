package wiresegal.psionup.api.enabling;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

/**
 * @author WireSegal
 *         Created at 7:10 PM on 3/31/16.
 */
public interface IManaTrick {
    int manaDrain(SpellContext context, Spell spell, int x, int y);

    EnumManaTier tier(SpellContext context, Spell spell, int x, int y);
}
