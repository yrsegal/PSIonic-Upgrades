package wiresegal.psionup.client.compat.jei.crafting

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponent

class ShapedCadRecipeHandler : IRecipeHandler<RecipeCadComponent> {

    override fun getRecipeClass(): Class<RecipeCadComponent> {
        return RecipeCadComponent::class.java
    }

    override fun getRecipeCategoryUid(): String {
        return VanillaRecipeCategoryUid.CRAFTING
    }

    override fun getRecipeWrapper(recipe: RecipeCadComponent): IRecipeWrapper {
        return ShapedCadRecipeJEI(recipe)
    }

    override fun isRecipeValid(recipe: RecipeCadComponent): Boolean {
        return recipe.input.size > 0
    }
}
