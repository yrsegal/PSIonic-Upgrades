package wiresegal.psionup.client.compat.jei.craftingTricks

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import wiresegal.psionup.common.lib.LibMisc

class TrickCraftingRecipeHandler : IRecipeHandler<TrickCraftingRecipeJEI> {
    override fun getRecipeClass(): Class<TrickCraftingRecipeJEI> {
        return TrickCraftingRecipeJEI::class.java
    }

    override fun getRecipeCategoryUid(recipe: TrickCraftingRecipeJEI?): String {
        return "${LibMisc.MOD_ID}:trickCrafting"
    }

    override fun getRecipeWrapper(recipe: TrickCraftingRecipeJEI): IRecipeWrapper {
        return recipe
    }

    override fun isRecipeValid(recipe: TrickCraftingRecipeJEI?): Boolean {
        return true
    }
}
