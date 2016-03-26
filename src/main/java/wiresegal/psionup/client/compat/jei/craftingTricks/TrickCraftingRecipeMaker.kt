package wiresegal.psionup.client.compat.jei.craftingTricks

import wiresegal.psionup.api.PsionicAPI
import java.util.*

object TrickCraftingRecipeMaker {
    val recipes: List<TrickCraftingRecipeJEI>
        get() {
            val trickArrayRecipeMap = PsionicAPI.getTrickRecipes()

            val recipes = ArrayList<TrickCraftingRecipeJEI>()

            for (trickRecipeEntry in trickArrayRecipeMap) {
                recipes.add(TrickCraftingRecipeJEI(trickRecipeEntry))
            }

            return recipes
        }
}
