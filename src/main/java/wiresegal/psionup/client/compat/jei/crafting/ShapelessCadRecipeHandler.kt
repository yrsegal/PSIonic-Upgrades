package wiresegal.psionup.client.compat.jei.crafting

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponentShapeless

object ShapelessCadRecipeHandler : IRecipeHandler<RecipeCadComponentShapeless> {
    override fun getRecipeClass(): Class<RecipeCadComponentShapeless> {
        return RecipeCadComponentShapeless::class.java
    }

    override fun getRecipeCategoryUid(recipe: RecipeCadComponentShapeless?): String {
        return VanillaRecipeCategoryUid.CRAFTING
    }

    override fun getRecipeWrapper(recipe: RecipeCadComponentShapeless): IRecipeWrapper {
        return ShapelessCadRecipeJEI(recipe)
    }

    override fun isRecipeValid(recipe: RecipeCadComponentShapeless): Boolean {
        return recipe.input.size > 0
    }
}
