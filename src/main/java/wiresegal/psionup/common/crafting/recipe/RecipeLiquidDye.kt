package wiresegal.psionup.common.crafting.recipe

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemDye
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.oredict.OreDictionary
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
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
        var ink: ItemStack = ItemStack.EMPTY
        var foundDye = false

        (0..inv.sizeInventory - 1)
                .mapNotNull { inv.getStackInSlot(it) }
                .forEach {
                    if (it.item is ItemLiquidColorizer) {
                        ink = it
                    } else {
                        if (!checkStack(it, dyes)) {
                            return false
                        }

                        foundDye = true
                    }
                }

        return !ink.isEmpty && foundDye
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
        var ink: ItemStack = ItemStack.EMPTY
        var colors = 0
        var r = 0
        var g = 0
        var b = 0

        (0..inv.sizeInventory - 1)
                .mapNotNull { inv.getStackInSlot(it) }
                .forEach {
                    if (it.item is ItemLiquidColorizer) {
                        ink = it

                        val newstack = it.copy()
                        newstack.count = 1

                        if (ItemLiquidColorizer.getColorFromStack(it) != Int.MAX_VALUE) {
                            val color = Color(ItemLiquidColorizer.Companion.getColorFromStack(it))
                            r += color.red
                            g += color.green
                            b += color.blue
                            colors++
                        }
                    } else {
                        if (!checkStack(it, dyes)) {
                            return ItemStack.EMPTY
                        }

                        val color = Color(getColorFromDye(it))
                        r += color.red
                        g += color.green
                        b += color.blue
                        colors++
                    }
                }

        if (ink.isEmpty || colors == 0) {
            return ItemStack.EMPTY
        }

        r /= colors
        g /= colors
        b /= colors

        val newink = ink.copy()
        ItemNBTHelper.setInt(newink, "color", Color(r, g, b).rgb)
        return newink
    }

    fun checkStack(stack: ItemStack, keys: Array<String>): Boolean {
        return keys.any { checkStack(stack, it) }
    }

    fun checkStack(stack: ItemStack, key: String): Boolean {
        val ores = OreDictionary.getOres(key, false)
        return ores.any { OreDictionary.itemMatches(stack, it, false) }
    }

    fun getColorFromDye(stack: ItemStack): Int {
        return if (dyes.indices.any { checkStack(stack, dyes[it]) }) ItemDye.DYE_COLORS[15 - stack.metadata] else 0xFFFFFF
    }

    /**
     * Returns the size of the recipe area
     */
    override fun getRecipeSize(): Int {
        return 10
    }

    override fun getRecipeOutput(): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getRemainingItems(inv: InventoryCrafting?): NonNullList<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }
}
