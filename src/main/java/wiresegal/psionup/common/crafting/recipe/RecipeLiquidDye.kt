package wiresegal.psionup.common.crafting.recipe

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemDye
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.oredict.OreDictionary
import vazkii.arl.util.ItemNBTHelper
import wiresegal.psionup.common.items.component.ItemLiquidColorizer
import java.awt.Color

/**
 * @author WireSegal
 * Created at 12:36 PM on 3/20/16.
 */
class RecipeLiquidDye : IRecipe {

    val dyeNames = arrayOf("White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black")
    val dyes = Array(dyeNames.size, { i -> "dye${dyeNames[i]}" })

    override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
        var ink: ItemStack? = null
        var foundDye = false

        for (i in 0..inv.sizeInventory - 1) {
            val stack = inv.getStackInSlot(i)

            if (stack != null) {
                if (stack.item is ItemLiquidColorizer) {
                    ink = stack
                } else {
                    if (!checkStack(stack, dyes)) {
                        return false
                    }

                    foundDye = true
                }
            }
        }

        return ink != null && foundDye
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
        var ink: ItemStack? = null
        var colors = 0
        var r = 0
        var g = 0
        var b = 0

        for (k in 0..inv.sizeInventory - 1) {
            val stack = inv.getStackInSlot(k)

            if (stack != null) {
                if (stack.item is ItemLiquidColorizer) {
                    ink = stack

                    val newstack = stack.copy()
                    newstack.count = 1

                    if (ItemLiquidColorizer.getColorFromStack(stack) != Int.MAX_VALUE) {
                        val color = Color(ItemLiquidColorizer.Companion.getColorFromStack(stack))
                        r += color.red
                        g += color.green
                        b += color.blue
                        colors++
                    }
                } else {
                    if (!checkStack(stack, dyes)) {
                        return null
                    }

                    val color = Color(getColorFromDye(stack))
                    r += color.red
                    g += color.green
                    b += color.blue
                    colors++
                }
            }
        }

        if (ink == null || colors == 0) {
            return null
        }

        r /= colors
        g /= colors
        b /= colors

        val newink = ink.copy()
        ItemNBTHelper.setInt(newink, "color", Color(r, g, b).rgb)
        return newink
    }

    fun checkStack(stack: ItemStack, keys: Array<String>): Boolean {
        for (key in keys) {
            if (checkStack(stack, key))
                return true
        }
        return false
    }

    fun checkStack(stack: ItemStack, key: String): Boolean {
        val ores = OreDictionary.getOres(key, false)
        for (ore in ores) {
            if (OreDictionary.itemMatches(stack, ore, false))
                return true
        }
        return false
    }

    fun getColorFromDye(stack: ItemStack): Int {
        for (i in dyes.indices) {
            if (checkStack(stack, dyes[i])) {
                return ItemDye.DYE_COLORS[15 - stack.metadata]
            }
        }
        return 0xFFFFFF
    }

    /**
     * Returns the size of the recipe area
     */
    override fun getRecipeSize(): Int {
        return 10
    }

    override fun getRecipeOutput(): ItemStack? {
        return null
    }

    override fun getRemainingItems(inv: InventoryCrafting?): NonNullList<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }
}
