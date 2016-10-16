package wiresegal.psionup.client.compat.jei.crafting

import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vazkii.psi.api.cad.EnumCADComponent
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponent
import wiresegal.psionup.common.items.base.ICadComponentAcceptor

class ShapedCadRecipeJEI(recipe: RecipeCadComponent) : IShapedCraftingRecipeWrapper {

    private val inputs: List<Any?>
    private val outputs: List<ItemStack>
    private val width: Int
    private val height: Int

    init {
        width = recipe.width
        height = recipe.height
        inputs = recipe.input.map {
            if (it is EnumCADComponent) ShapelessCadRecipeJEI.itemMap[it] else if (it is ItemStack) {
                it.stackSize = 1
                it
            } else it
        }
        outputs = if (recipe.output.item is ICadComponentAcceptor)
                recipe.input.filter { it is EnumCADComponent }.flatMap { Array(ShapelessCadRecipeJEI.itemMap[it]?.size ?: 0, { i ->
                        (recipe.output.item as ICadComponentAcceptor).setPiece(recipe.output.copy(), it as EnumCADComponent, ShapelessCadRecipeJEI.itemMap[it]!![i])
                }).toList()}
            else
                listOf(recipe.output)
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getHeight(): Int {
        return height
    }

    override fun getInputs(): List<Any?> {
        return inputs
    }

    override fun getOutputs(): List<ItemStack> {
        return outputs
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {

    }

    override fun getFluidInputs(): List<FluidStack> {
        return arrayListOf()
    }

    override fun getFluidOutputs(): List<FluidStack> {
        return arrayListOf()
    }

    override fun drawAnimations(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int) {

    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String>? {
        return null
    }

    override fun handleClick(minecraft: Minecraft, mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        return false
    }
}
