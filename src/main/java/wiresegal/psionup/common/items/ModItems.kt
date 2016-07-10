package wiresegal.psionup.common.items

import net.minecraftforge.fml.common.Loader
import wiresegal.psionup.common.items.base.ItemMod
import wiresegal.psionup.common.items.base.ItemModArmor
import wiresegal.psionup.common.items.component.ItemEmptyColorizer
import wiresegal.psionup.common.items.component.ItemLiquidColorizer
import wiresegal.psionup.common.items.component.ItemWideCADSocket
import wiresegal.psionup.common.items.spell.ItemCADMagazine
import wiresegal.psionup.common.items.spell.ItemFakeCAD
import wiresegal.psionup.common.items.spell.ItemFlashRing
import wiresegal.psionup.common.items.spell.ItemFlowExosuit
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

    val ebonyExosuitHead: ItemModArmor
    val ebonyExosuitChest: ItemModArmor
    val ebonyExosuitLegs: ItemModArmor
    val ebonyExosuitBoots: ItemModArmor

    val ivoryExosuitHead: ItemModArmor
    val ivoryExosuitChest: ItemModArmor
    val ivoryExosuitLegs: ItemModArmor
    val ivoryExosuitBoots: ItemModArmor

    val flashRing: ItemMod

    init {
        liquidColorizer = ItemLiquidColorizer(LibNames.Items.LIQUID_INK_COLORIZER)
        emptyColorizer = ItemEmptyColorizer(LibNames.Items.DRAINED_COLORIZER)
        fakeCAD = ItemFakeCAD(LibNames.Items.INLINE_CASTER)
        magazine = ItemCADMagazine(LibNames.Items.SPELL_MAGAZINE)
        socket = ItemWideCADSocket(LibNames.Items.WIDE_BAND_SOCKET)

        ebonyExosuitHead = ItemFlowExosuit.Helmet(LibNames.Items.EBONY_HELMET, true)
        ebonyExosuitChest = ItemFlowExosuit.Chest(LibNames.Items.EBONY_CHEST, true)
        ebonyExosuitLegs = ItemFlowExosuit.Legs(LibNames.Items.EBONY_LEGS, true)
        ebonyExosuitBoots = ItemFlowExosuit.Boots(LibNames.Items.EBONY_BOOTS, true)

        ivoryExosuitHead = ItemFlowExosuit.Helmet(LibNames.Items.IVORY_HELMET, false)
        ivoryExosuitChest = ItemFlowExosuit.Chest(LibNames.Items.IVORY_CHEST, false)
        ivoryExosuitLegs = ItemFlowExosuit.Legs(LibNames.Items.IVORY_LEGS, false)
        ivoryExosuitBoots = ItemFlowExosuit.Boots(LibNames.Items.IVORY_BOOTS, false)

        flashRing = ItemFlashRing(LibNames.Items.FLASH_RING)

        if (Loader.isModLoaded("Botania"))
            CompatItems.init()
    }
}
