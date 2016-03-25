package wiresegal.psionup.common.items.base

import net.minecraft.item.ItemStack
import vazkii.psi.common.item.component.ItemCADComponent
import wiresegal.psionup.common.core.CreativeTab

/**
 * @author WireSegal
 * Created at 10:23 AM on 3/25/16.
 */
abstract class ItemComponent(name: String, vararg variants: String) : ItemCADComponent(name, *variants) {

    init {
        ItemMod.variantCache.add(this)
        creativeTab = CreativeTab.INSTANCE
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack): String {
        return super.getUnlocalizedName(par1ItemStack).replace("psi", "psionup")
    }
}
