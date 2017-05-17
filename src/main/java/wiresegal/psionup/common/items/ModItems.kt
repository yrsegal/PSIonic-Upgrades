package wiresegal.psionup.common.items

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.base.item.ItemModArmor
import com.teamwizardry.librarianlib.features.base.item.ItemModSword
import com.teamwizardry.librarianlib.features.base.item.ItemModTool
import net.minecraftforge.fml.common.Loader
import wiresegal.psionup.common.items.component.*
import wiresegal.psionup.common.items.spell.*
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

    val ebonyPickaxe: ItemModTool
    val ebonyShovel: ItemModTool
    val ebonyAxe: ItemModTool
    val ebonySword: ItemModSword

    val ivoryPickaxe: ItemModTool
    val ivoryShovel: ItemModTool
    val ivoryAxe: ItemModTool
    val ivorySword: ItemModSword

    val bioticSensor: ItemMod

    val gaussRifle: ItemMod

    val gaussBullet: ItemMod

    val unstableBattery: ItemUnstableBattery

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

        ebonyPickaxe = ItemFlowTool.Pickaxe(LibNames.Items.EBONY_PICKAXE, true)
        ebonyShovel = ItemFlowTool.Shovel(LibNames.Items.EBONY_SHOVEL, true)
        ebonyAxe = ItemFlowTool.Axe(LibNames.Items.EBONY_AXE, true)
        ebonySword = ItemFlowSword(LibNames.Items.EBONY_SWORD, true)

        ivoryPickaxe = ItemFlowTool.Pickaxe(LibNames.Items.IVORY_PICKAXE, false)
        ivoryShovel = ItemFlowTool.Shovel(LibNames.Items.IVORY_SHOVEL, false)
        ivoryAxe = ItemFlowTool.Axe(LibNames.Items.IVORY_AXE, false)
        ivorySword = ItemFlowSword(LibNames.Items.IVORY_SWORD, false)

        bioticSensor = ItemBioticSensor(LibNames.Items.BIOTIC_SENSOR)

        gaussRifle = ItemGaussRifle(LibNames.Items.GAUSS_RIFLE)

        gaussBullet = ItemMod(LibNames.Items.GAUSS_BULLET)

        unstableBattery = ItemUnstableBattery(LibNames.Items.UNSTABLE_BATTERY)

        if (Loader.isModLoaded("botania"))
            CompatItems.init()
    }
}
