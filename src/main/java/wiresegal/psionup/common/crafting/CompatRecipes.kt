package wiresegal.psionup.common.crafting

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.lib.LibOreDict
import wiresegal.psionup.common.core.ConfigHandler
import wiresegal.psionup.common.effect.botania.brew.ModBrews
import wiresegal.psionup.common.items.CompatItems

/**
 * @author WireSegal
 * Created at 3:36 PM on 4/1/16.
 */
object CompatRecipes {

    var isInitialized = false

    fun initRecipes() {
        isInitialized = true
        ModRecipes.addOreDictRecipe(ItemStack(CompatItems.blaster),
                "MPS",
                "L  ",
                'M', LibOreDict.RUNE[8], // Mana
                'L', LibOreDict.MANA_STEEL,
                'P', "gemPsi",
                'S', ItemStack(ModBlocks.spreader, 1, 1)) // Redstone Spreader
        if (ConfigHandler.enablePsionicPulse)
            BotaniaAPI.registerBrewRecipe(ModBrews.psipulse, ItemStack(Items.NETHER_WART), "dustPsi", ItemStack(Items.GHAST_TEAR))
        BotaniaAPI.registerBrewRecipe(ModBrews.psishock, ItemStack(Items.NETHER_WART), "dustPsi", ItemStack(Items.FERMENTED_SPIDER_EYE))
    }
}
