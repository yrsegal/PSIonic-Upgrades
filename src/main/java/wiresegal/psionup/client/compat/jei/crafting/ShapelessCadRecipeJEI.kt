package wiresegal.psionup.client.compat.jei.crafting

import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.common.item.base.ItemMod
import vazkii.psi.common.item.base.ModItems
import wiresegal.psionup.common.crafting.RecipeCadComponentShapeless
import java.util.*

class ShapelessCadRecipeJEI(private val recipe: RecipeCadComponentShapeless) : ICraftingRecipeWrapper {

    private val inputs: List<Any?>
    private val output: ItemStack?

    companion object {
        val itemMap = mapOf(
                Pair(EnumCADComponent.ASSEMBLY, stackArray(ModItems.cadAssembly)),
                Pair(EnumCADComponent.BATTERY, stackArray(ModItems.cadBattery)),
                Pair(EnumCADComponent.CORE, stackArray(ModItems.cadCore)),
                Pair(EnumCADComponent.DYE, stackArray(ModItems.cadColorizer)),
                Pair(EnumCADComponent.SOCKET, stackArray(ModItems.cadSocket)))

        fun stackArray(item: Item, meta: IntRange): ArrayList<ItemStack> {
            val out = ArrayList<ItemStack>()
            for (i in meta) {
                out.add(ItemStack(item, 1, i))
            }
            return out
        }

        fun stackArray(item: ItemMod): ArrayList<ItemStack> {
            return stackArray(item, 0..item.variants.size - 1)
        }
    }

    init {
        for (input in recipe.input) {
            if (input is ItemStack) {
                if (input.stackSize != 1) {
                    input.stackSize = 1
                }
            }
        }

        output = recipe.recipeOutput
        val input = recipe.input

        val inputList = ArrayList(input)
        val safeList = ArrayList(input)

        for (obj in safeList) {
            if (obj is EnumCADComponent) {
                val replaceIndex = inputList.indexOf(obj)
                inputList[replaceIndex] = itemMap[obj]
            }
        }

        this.inputs = inputList
    }

    override fun getInputs(): List<Any?> {
        return inputs
    }

    override fun getOutputs(): List<ItemStack?> {
        return listOf(output)
    }

    override fun getFluidInputs(): List<FluidStack>? {
        return null
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {

    }

    override fun getFluidOutputs(): List<FluidStack>? {
        return null
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
