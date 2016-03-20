package wiresegal.psionup.common.items

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.ItemStack
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.common.item.base.IColorProvider
import vazkii.psi.common.item.component.ItemCADComponent
import wiresegal.psionup.common.core.CreativeTab

/**
 * @author WireSegal
 * Created at 8:44 AM on 3/20/16.
 */
class ItemEmptyColorizer(name: String) : ItemCADComponent(name), ICADColorizer, IColorProvider {

    init {
        creativeTab = CreativeTab.INSTANCE
        ItemMod.variantCache.add(this)
    }

    override fun getColor() = IItemColor {
        stack, tintIndex -> if (tintIndex == 1) 0 else 0xFFFFFF
    }

    override fun getColor(p0: ItemStack?): Int = 0x080808

    override fun getUnlocalizedName(par1ItemStack: ItemStack): String {
        return super.getUnlocalizedName(par1ItemStack).replace("psi", "psionup")
    }
}
