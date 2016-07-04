package wiresegal.psionup.client.compat.jei.craftingTricks

import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import wiresegal.psionup.client.compat.jei.JEICompat
import wiresegal.psionup.common.lib.LibMisc

class TrickCraftingCategory : IRecipeCategory {

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

    }

    override fun drawAnimations(minecraft: Minecraft) {

    }

    @SuppressWarnings("unchecked")
    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper) {

        recipeLayout.itemStacks.init(INPUT_SLOT, true, 0, 5)
        recipeLayout.itemStacks.init(CAD_SLOT, false, 17, 5)
        recipeLayout.itemStacks.init(OUTPUT_SLOT, false, 80, 5)

        if (recipeWrapper is TrickCraftingRecipeJEI) {
            recipeLayout.itemStacks.set(INPUT_SLOT, recipeWrapper.inputs[0] as ItemStack)
            recipeLayout.itemStacks.set(CAD_SLOT, recipeWrapper.inputs[1] as ItemStack)
            recipeLayout.itemStacks.set(OUTPUT_SLOT, recipeWrapper.outputs[0] as ItemStack)
        }
    }

    companion object {
        private val INPUT_SLOT = 0
        private val CAD_SLOT = 1
        private val OUTPUT_SLOT = 2
    }
}
