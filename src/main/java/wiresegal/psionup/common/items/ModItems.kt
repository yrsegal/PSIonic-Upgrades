package wiresegal.psionup.common.items

import vazkii.psi.common.item.base.ItemMod

/**
 * @author WireSegal
 * Created at 12:26 PM on 3/20/16.
 */
object ModItems {
    var liquidColorizer: ItemMod
    var emptyColorizer: ItemMod
    var fakeCAD: ItemMod
//    var magazine: ItemMod
    var socket: ItemMod

    init {
        liquidColorizer = ItemLiquidColorizer("liquidColorizer")
        emptyColorizer = ItemEmptyColorizer("emptyColorizer")
        fakeCAD = ItemFakeCAD("fakeCAD")
        socket = ItemWideCADSocket("wideSocket")
    }
}
