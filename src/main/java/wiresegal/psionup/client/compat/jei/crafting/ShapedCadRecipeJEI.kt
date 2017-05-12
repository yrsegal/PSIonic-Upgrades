package wiresegal.psionup.client.compat.jei.crafting

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vazkii.psi.api.cad.EnumCADComponent
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponent
import wiresegal.psionup.common.items.base.ICadComponentAcceptor

class ShapedCadRecipeJEI(recipe: RecipeCadComponent) : IShapedCraftingRecipeWrapper {

    private val inputs = recipe.input.mapNotNull {
        if (it is EnumCADComponent) ShapelessCadRecipeJEI.itemMap[it] else if (it is ItemStack) {
            it.count = 1
            listOf(it)
        } else listOf(ItemStack.EMPTY)
    }
    private val outputs = if (recipe.output.item is ICadComponentAcceptor)
        recipe.input.filter { it is EnumCADComponent }.flatMap { Array(ShapelessCadRecipeJEI.itemMap[it]?.size ?: 0, { i ->
            (recipe.output.item as ICadComponentAcceptor).setPiece(recipe.output.copy(), it as EnumCADComponent, ShapelessCadRecipeJEI.itemMap[it]!![i])
        }).toList()}
    else
        listOf(recipe.output)

    private val width = recipe.width
    private val height = recipe.height

    override fun getWidth(): Int {
        return width
    }

    override fun getHeight(): Int {
        return height
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        // NO-OP
    }

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(ItemStack::class.java, inputs)
        ingredients.setOutputs(ItemStack::class.java, outputs)
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String>? {
        return null
    }

    override fun handleClick(minecraft: Minecraft, mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        return false
    }
}
