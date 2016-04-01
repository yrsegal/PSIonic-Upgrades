package wiresegal.psionup.common.items.component.botania

import vazkii.psi.common.item.base.ItemMod
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 7:25 PM on 3/31/16.
 */
object CompatItems {

    var isInitialized = false

    lateinit var blaster: ItemMod

    fun init() {
        isInitialized = true
        blaster = ItemBlasterAssembly(LibNames.Items.LIVINGWOOD_CAD)
    }
}
