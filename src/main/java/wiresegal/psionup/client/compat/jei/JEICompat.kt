package wiresegal.psionup.client.compat.jei

import com.teamwizardry.librarianlib.features.helpers.nonnullListOf
import mezz.jei.api.BlankModPlugin
import mezz.jei.api.IJeiHelpers
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import vazkii.arl.block.BlockMod
import wiresegal.psionup.client.compat.jei.crafting.ShapedCadRecipeHandler
import wiresegal.psionup.client.compat.jei.crafting.ShapelessCadRecipeHandler
import wiresegal.psionup.client.compat.jei.craftingTricks.TrickCraftingCategory
import wiresegal.psionup.client.compat.jei.craftingTricks.TrickCraftingRecipeHandler
import wiresegal.psionup.client.compat.jei.craftingTricks.TrickCraftingRecipeMaker
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.lib.LibMisc
import vazkii.psi.common.block.base.ModBlocks as PsiBlocks
import vazkii.psi.common.item.base.ModItems as PsiItems

/**
 * @author WireSegal
 * Created at 10:06 AM on 3/21/16.
 */
@JEIPlugin
class JEICompat : BlankModPlugin() {

    companion object {
        lateinit var helper: IJeiHelpers
    }

    override fun register(registry: IModRegistry) {
        helper = registry.jeiHelpers

        registry.addRecipeHandlers(ShapedCadRecipeHandler, ShapelessCadRecipeHandler, TrickCraftingRecipeHandler)

        registry.addRecipeCategories(TrickCraftingCategory)

        registry.addRecipes(TrickCraftingRecipeMaker.recipes)

        helper.ingredientBlacklist.addIngredientToBlacklist(ItemStack(PsiBlocks.conjured))

        val list = nonnullListOf<ItemStack>()
        PsiItems.cad.getSubItems(PsiItems.cad, null, list)
        for (item in list)
            registry.addRecipeCategoryCraftingItem(item, TrickCraftingCategory.uid)

        registry.addDescription(ItemStack(ModItems.liquidColorizer), "jei.${LibMisc.MOD_ID}.drained.desc")
        registry.addDescription(ItemStack(ModItems.emptyColorizer), "jei.${LibMisc.MOD_ID}.drained.desc")
    }
}
