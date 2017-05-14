package wiresegal.psionup.client.compat.jei.crafting

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import vazkii.arl.item.ItemMod
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.common.item.base.ModItems
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponentShapeless
import java.util.*

class ShapelessCadRecipeJEI(recipe: RecipeCadComponentShapeless) : IRecipeWrapper {

    private val inputs: List<List<ItemStack>>
    private val output: ItemStack = recipe.recipeOutput

    companion object {
        val itemMap = mapOf(
                Pair(EnumCADComponent.ASSEMBLY, stackList(ModItems.cadAssembly)),
                Pair(EnumCADComponent.BATTERY, stackList(ModItems.cadBattery)),
                Pair(EnumCADComponent.CORE, stackList(ModItems.cadCore)),
                Pair(EnumCADComponent.DYE, stackList(ModItems.cadColorizer)),
                Pair(EnumCADComponent.SOCKET, stackList(ModItems.cadSocket)))

        fun stackList(item: Item, meta: IntRange): List<ItemStack> {
            return Array(meta.count()) {
                ItemStack(item, 1, it)
            }.toList()
        }

        fun stackList(item: ItemMod): List<ItemStack> {
            return stackList(item, item.variants.indices)
        }
    }

    init {

        val input = recipe.input

        val inputList = ArrayList(input)
        val safeList = ArrayList(input)

        for (obj in safeList) {
            if (obj is EnumCADComponent) {
                val replaceIndex = inputList.indexOf(obj)
                inputList[replaceIndex] = itemMap[obj]?.toMutableList()
            }
        }

        @Suppress("UNCHECKED_CAST")
        this.inputs = recipe.input.mapNotNull {
            if (it is EnumCADComponent) ShapelessCadRecipeJEI.itemMap[it] else if (it is ItemStack) {
                it.count = 1
                listOf(it)
            } else it as? List<ItemStack> ?: listOf(ItemStack.EMPTY)
        }
    }

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(ItemStack::class.java, inputs)
        ingredients.setOutput(ItemStack::class.java, output)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {

    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String>? {
        return null
    }

    override fun handleClick(minecraft: Minecraft, mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        return false
    }
}
