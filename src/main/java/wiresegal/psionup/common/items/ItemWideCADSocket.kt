package wiresegal.psionup.common.items

import net.minecraft.item.ItemStack
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.common.item.component.ItemCADComponent
import wiresegal.psionup.common.core.CreativeTab

/**
 * @author WireSegal
 * Created at 8:43 AM on 3/20/16.
 */
class ItemWideCADSocket(name: String) : ItemCADComponent(name, name) {

    init {
        creativeTab = CreativeTab.INSTANCE
        ItemMod.variantCache.add(this)
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack): String {
        return super.getUnlocalizedName(par1ItemStack).replace("psi", "psionup")
    }

    override fun registerStats() {
        addStat(EnumCADStat.BANDWIDTH, 0, 9)
        addStat(EnumCADStat.SOCKETS, 0, 1)
    }

    override fun getComponentType(p0: ItemStack) = EnumCADComponent.SOCKET
}
