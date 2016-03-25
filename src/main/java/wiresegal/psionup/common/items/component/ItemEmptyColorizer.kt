package wiresegal.psionup.common.items.component

import net.minecraft.item.ItemStack
import vazkii.psi.api.cad.ICADColorizer
import wiresegal.psionup.common.items.base.ItemComponent

/**
 * @author WireSegal
 * Created at 8:44 AM on 3/20/16.
 */
class ItemEmptyColorizer(name: String) : ItemComponent(name, name), ICADColorizer {

    override fun getColor(p0: ItemStack?): Int = 0x080808
}
