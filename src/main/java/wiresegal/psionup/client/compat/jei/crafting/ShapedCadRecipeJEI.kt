package wiresegal.psionup.client.compat.jei.crafting

import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vazkii.psi.api.cad.EnumCADComponent
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponent
import wiresegal.psionup.common.items.base.ICadComponentAcceptor
import java.util.*

class ShapedCadRecipeJEI(private val recipe: RecipeCadComponent) : IShapedCraftingRecipeWrapper {

    private val inputs: MutableList<Any?>
    private val outputs: ArrayList<ItemStack>
    private val width: Int
    private val height: Int

    init {
        for (input in this.recipe.input) {
            if (input is ItemStack) {
                if (input.stackSize != 1) {
                    input.stackSize = 1
                }
            }
        }
        this.width = recipe.width
        this.height = recipe.height

        val out = recipe.output
        val input = recipe.input

        val inputList = arrayListOf(*input)
        val safeList = arrayListOf(*input)

        val outputlist = ArrayList<ItemStack>()

        for (obj in safeList) {
            if (obj is EnumCADComponent) {
                val replaceIndex = inputList.indexOf(obj)
                inputList[replaceIndex] = ShapelessCadRecipeJEI.itemMap[obj]
                if (out.item is ICadComponentAcceptor) {
                    val map = ShapelessCadRecipeJEI.itemMap[obj]!!
                    for (item in map)
                        outputlist.add((out.item as ICadComponentAcceptor).setPiece(out.copy(), obj, item))
                }
            }
        }

        this.outputs = outputlist

        this.inputs = inputList
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
