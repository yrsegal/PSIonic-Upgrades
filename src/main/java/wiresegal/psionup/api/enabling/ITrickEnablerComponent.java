package wiresegal.psionup.api.enabling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;

public interface ITrickEnablerComponent extends ICADComponent {
    boolean enablePiece(EntityPlayer player, ItemStack component, ItemStack cad, SpellContext context, Spell spell, int x, int y);
}
