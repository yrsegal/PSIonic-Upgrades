package wiresegal.psionup.common.items.base

import vazkii.psi.common.item.base.ItemMod
import wiresegal.psionup.common.items.spell.ItemCADMagazine
import wiresegal.psionup.common.items.spell.ItemFakeCAD
import wiresegal.psionup.common.items.component.ItemEmptyColorizer
import wiresegal.psionup.common.items.component.ItemLiquidColorizer
import wiresegal.psionup.common.items.component.ItemWideCADSocket
import wiresegal.psionup.common.items.spell.ItemPsimetalShield

/**
 * @author WireSegal
 * Created at 12:26 PM on 3/20/16.
 */
object ModItems {
    val liquidColorizer: ItemMod
    val emptyColorizer: ItemMod
    val fakeCAD: ItemMod
    val magazine: ItemMod
    val socket: ItemMod

    init {
        liquidColorizer = ItemLiquidColorizer("liquidColorizer")
        emptyColorizer = ItemEmptyColorizer("emptyColorizer")
        fakeCAD = ItemFakeCAD("fakeCAD")
        magazine = ItemCADMagazine("magazine")
        socket = ItemWideCADSocket("wideSocket")
    }
}
