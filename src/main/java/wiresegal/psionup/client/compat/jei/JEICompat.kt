package wiresegal.psionup.client.compat.jei

import mezz.jei.api.BlankModPlugin
import mezz.jei.api.IJeiHelpers
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import net.minecraft.item.ItemStack
import wiresegal.psionup.client.compat.jei.crafting.ShapedCadRecipeHandler
import wiresegal.psionup.client.compat.jei.crafting.ShapelessCadRecipeHandler
import wiresegal.psionup.client.compat.jei.craftingTricks.TrickCraftingCategory
import wiresegal.psionup.client.compat.jei.craftingTricks.TrickCraftingRecipeHandler
import wiresegal.psionup.client.compat.jei.craftingTricks.TrickCraftingRecipeMaker
import wiresegal.psionup.common.block.ModBlocks
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.lib.LibMisc
import vazkii.psi.common.block.base.ModBlocks as PsiBlocks

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

        registry.addRecipeHandlers(ShapedCadRecipeHandler(), ShapelessCadRecipeHandler(), TrickCraftingRecipeHandler())

        registry.addRecipeCategories(TrickCraftingCategory())

        registry.addRecipes(TrickCraftingRecipeMaker.recipes)

        helper.itemBlacklist.addItemToBlacklist(ItemStack(PsiBlocks.conjured))
        helper.itemBlacklist.addItemToBlacklist(ItemStack(ModBlocks.conjured))

        registry.addDescription(ItemStack(ModItems.liquidColorizer), "jei.${LibMisc.MOD_ID_SHORT}.drained.desc")
        registry.addDescription(ItemStack(ModItems.emptyColorizer), "jei.${LibMisc.MOD_ID_SHORT}.drained.desc")
    }
}
