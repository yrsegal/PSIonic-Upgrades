package wiresegal.psionup.common.items.component

import net.minecraft.item.ItemStack
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.EnumCADStat
import wiresegal.psionup.common.items.base.ItemComponent

/**
 * @author WireSegal
 * Created at 8:43 AM on 3/20/16.
 */
class ItemWideCADSocket(name: String) : ItemComponent(name) {

    override fun registerStats() {
        addStat(EnumCADStat.BANDWIDTH, 0, 9)
        addStat(EnumCADStat.SOCKETS, 0, 1)
    }

    override fun getComponentType(p0: ItemStack) = EnumCADComponent.SOCKET
}
