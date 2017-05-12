package wiresegal.psionup.client.compat.jei.craftingTricks

import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import wiresegal.psionup.client.compat.jei.JEICompat
import wiresegal.psionup.common.lib.LibMisc

class TrickCraftingCategory : IRecipeCategory<TrickCraftingRecipeJEI> {

    private val background = JEICompat.helper.guiHelper.createDrawable(ResourceLocation(LibMisc.MOD_ID, "textures/gui/jei/trick.png"), 0, 0, 108, 30)

    override fun getUid(): String {
        return "${LibMisc.MOD_ID}:trickCrafting"
    }

    override fun getTitle(): String {
        return I18n.format("jei.${LibMisc.MOD_ID}.recipe.trickCrafting")
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun drawExtras(minecraft: Minecraft) {
        //NO-OP
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: TrickCraftingRecipeJEI, ingredients: IIngredients) {
        recipeLayout.itemStacks.init(INPUT_SLOT, true, 0, 5)
        recipeLayout.itemStacks.init(CAD_SLOT, false, 17, 5)
        recipeLayout.itemStacks.init(OUTPUT_SLOT, false, 80, 5)

        recipeLayout.itemStacks.set(INPUT_SLOT, ingredients.getInputs(ItemStack::class.java)[0])
        recipeLayout.itemStacks.set(CAD_SLOT, ingredients.getInputs(ItemStack::class.java)[1])
        recipeLayout.itemStacks.set(OUTPUT_SLOT, ingredients.getOutputs(ItemStack::class.java)[0])
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String> {
        return emptyList()
    }

    override fun getIcon(): IDrawable? {
        return null
    }

    companion object {
        private val INPUT_SLOT = 0
        private val CAD_SLOT = 1
        private val OUTPUT_SLOT = 2
    }
}
