package wiresegal.psionup.common.items

import net.minecraftforge.fml.common.Loader
import wiresegal.psionup.common.items.base.ItemMod
import wiresegal.psionup.common.items.component.ItemEmptyColorizer
import wiresegal.psionup.common.items.component.ItemLiquidColorizer
import wiresegal.psionup.common.items.component.ItemWideCADSocket
import wiresegal.psionup.common.items.spell.ItemCADMagazine
import wiresegal.psionup.common.items.spell.ItemFakeCAD
import wiresegal.psionup.common.lib.LibNames

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
        liquidColorizer = ItemLiquidColorizer(LibNames.Items.LIQUID_INK_COLORIZER)
        emptyColorizer = ItemEmptyColorizer(LibNames.Items.DRAINED_COLORIZER)
        fakeCAD = ItemFakeCAD(LibNames.Items.INLINE_CASTER)
        magazine = ItemCADMagazine(LibNames.Items.SPELL_MAGAZINE)
        socket = ItemWideCADSocket(LibNames.Items.WIDE_BAND_SOCKET)

        if (Loader.isModLoaded("Botania"))
            CompatItems.init()
    }
}
