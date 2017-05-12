package wiresegal.psionup.common.items

import com.teamwizardry.librarianlib.features.base.item.ItemMod
import wiresegal.psionup.common.crafting.CompatRecipes
import wiresegal.psionup.common.effect.botania.brew.ModBrews
import wiresegal.psionup.common.items.component.botania.ItemBlasterAssembly
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
        ModBrews
    }

    fun initRecipes() {
        CompatRecipes.initRecipes()
    }
}
