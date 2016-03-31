package wiresegal.psionup.api.enabling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ICADComponent;

public interface ITrickEnablerComponent extends ICADComponent {
    boolean enablesPiece(EntityPlayer player, ItemStack component, ItemStack cad, String piece);
}
