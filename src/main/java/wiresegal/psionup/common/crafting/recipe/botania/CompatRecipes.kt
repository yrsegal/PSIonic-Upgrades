package wiresegal.psionup.common.crafting.recipe.botania

import net.minecraft.item.ItemStack
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.lib.LibOreDict
import wiresegal.psionup.common.crafting.ModRecipes
import wiresegal.psionup.common.items.component.botania.CompatItems

/**
 * @author WireSegal
 * Created at 3:36 PM on 4/1/16.
 */
object CompatRecipes {
    fun initRecipes() {
        ModRecipes.addOreDictRecipe(ItemStack(CompatItems.blaster),
                "PLS",
                "M  ",
                'M', LibOreDict.RUNE[8], // Mana
                'L', LibOreDict.LIVING_WOOD,
                'P', "gemPsi",
                'S', ItemStack(ModBlocks.spreader, 1, 1)) // Redstone Spreader
    }
}
