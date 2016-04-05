package wiresegal.psionup.api.enabling.botania;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;
import wiresegal.psionup.api.enabling.ITrickEnablerComponent;

/**
 * @author WireSegal
 *         Created at 9:51 AM on 4/5/16.
 */
public interface IBlasterComponent extends ITrickEnablerComponent, ICADAssembly {
    @Override
    default EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.ASSEMBLY;
    }
}
