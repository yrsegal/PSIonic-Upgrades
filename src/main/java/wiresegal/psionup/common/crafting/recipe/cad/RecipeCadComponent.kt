package wiresegal.psionup.common.crafting.recipe.cad

import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.ShapedRecipes
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.oredict.OreDictionary
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICADComponent
import wiresegal.psionup.common.items.base.ICadComponentAcceptor
import java.util.*

/**
 * @author WireSegal
 * Created at 7:49 PM on 3/20/16.
 */
open class RecipeCadComponent : IRecipe {

    var output: ItemStack
    var input: Array<Any?>
    var width = 0
    var height = 0
    private var mirrored = true

    constructor(result: Block, vararg recipe: Any) : this(ItemStack(result), *recipe)

    constructor(result: Item, vararg recipe: Any) : this(ItemStack(result), *recipe)

    constructor(result: ItemStack, vararg recipeIn: Any) {
        var recipe: MutableList<*> = arrayListOf(*recipeIn)
        output = result.copy()

        var shape = ""
        var idx = 0

        if (recipe[idx] is Boolean) {
            mirrored = recipe[idx] as Boolean
            val place = recipe[idx + 1]
            if (place is MutableList<*>) {
                recipe = place
            } else {
                idx = 1
            }
        }
        val place = recipe[idx]
        if (place is List<*>) {
            val parts = place

            for (s in parts) {
                if (s is String) {
                    width = s.length
                    shape += s
                }
            }

            height = parts.size
        } else {
            while (recipe[idx] is String) {
                val s = recipe[idx++] as String
                shape += s
                width = s.length
                height++
            }
        }

        if (width * height != shape.length) {
            var ret = "Invalid shaped ore recipe: "
            for (tmp in recipe) {
                ret += tmp.toString() + ", "
            }
            ret += output
            throw RuntimeException(ret)
        }

        val itemMap = HashMap<Char, Any>()

        while (idx < recipe.size) {
            val chr = recipe[idx] as Char
            val `in` = recipe[idx + 1]

            if (`in` is EnumCADComponent) {
                itemMap.put(chr, `in`)
            } else if (`in` is ItemStack) {
                itemMap.put(chr, `in`.copy())
            } else if (`in` is Item) {
                itemMap.put(chr, ItemStack(`in`))
            } else if (`in` is Block) {
                itemMap.put(chr, ItemStack(`in`, 1, OreDictionary.WILDCARD_VALUE))
            } else if (`in` is String) {
                itemMap.put(chr, OreDictionary.getOres(`in`))
            } else {
                var ret = "Invalid shaped CAD component recipe: "
                for (tmp in recipe) {
                    ret += tmp.toString() + ", "
                }
                ret += output
                throw RuntimeException(ret)
            }
            idx += 2
        }

        input = arrayOfNulls(width * height)
        var x = 0
        for (chr in shape.toCharArray()) {
            input[x++] = itemMap[chr]
        }
    }

    internal constructor(recipe: ShapedRecipes, replacements: Map<ItemStack, String>) {
        output = recipe.recipeOutput!!
        width = recipe.recipeWidth
        height = recipe.recipeHeight

        input = arrayOfNulls<Any>(recipe.recipeItems.size)

        for (i in input.indices) {
            val ingred = recipe.recipeItems[i] ?: continue

            input[i] = recipe.recipeItems[i]

            for ((key, value) in replacements) {
                if (OreDictionary.itemMatches(key, ingred, true)) {
                    input[i] = OreDictionary.getOres(value)
                    break
                }
            }
        }
    }

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack {
        val out = output.copy()
        val outitem = out.item
        if (outitem is ICadComponentAcceptor) {
            for (i in 0..var1.sizeInventory - 1) {
                val stack = var1.getStackInSlot(i)
                if (stack != null) {
                    val slotitem = stack.item
                    if (slotitem is ICADComponent && outitem.acceptsPiece(out, slotitem.getComponentType(stack)))
                        outitem.setPiece(out, slotitem.getComponentType(stack), stack)
                }
            }
        }
        return out
    }

    override fun getRecipeSize(): Int {
        return input.size
    }

    override fun getRecipeOutput(): ItemStack {
        return output
    }

    override fun matches(inv: InventoryCrafting, world: World?): Boolean {
        for (x in 0..MAX_CRAFT_GRID_WIDTH - width) {
            for (y in 0..MAX_CRAFT_GRID_HEIGHT - height) {
                if (checkMatch(inv, x, y, false)) {
                    return true
                }

                if (mirrored && checkMatch(inv, x, y, true)) {
                    return true
                }
            }
        }

        return false
    }

    @SuppressWarnings("unchecked")
    private fun checkMatch(inv: InventoryCrafting, startX: Int, startY: Int, mirror: Boolean): Boolean {
        for (x in 0..MAX_CRAFT_GRID_WIDTH - 1) {
            for (y in 0..MAX_CRAFT_GRID_HEIGHT - 1) {
                val subX = x - startX
                val subY = y - startY
                var target: Any? = null

                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    if (mirror) {
                        target = input[width - subX - 1 + subY * width]
                    } else {
                        target = input[subX + subY * width]
                    }
                }

                val slot = inv.getStackInRowAndColumn(x, y)
                if (target is EnumCADComponent) {
                    if (!slot.isEmpty && slot.item is ICADComponent) {
                        val component = slot.item as ICADComponent
                        if (component.getComponentType(slot) != target) {
                            return false
                        } else if (component is ICadComponentAcceptor && component.acceptsPiece(slot, target)) {
                            return false
                        }
                    } else
                        return false
                } else if (target is ItemStack) {
                    if (!OreDictionary.itemMatches(target, slot, false)) {
                        return false
                    }
                } else if (target is List<*>) {
                    var matched = false

                    val itr = target.iterator()
                    while (itr.hasNext() && !matched) {
                        val next = itr.next()
                        if (next is ItemStack)
                            matched = OreDictionary.itemMatches(next, slot, false)
                    }

                    if (!matched) {
                        return false
                    }
                } else if (target == null && !slot.isEmpty) {
                    return false
                }
            }
        }

        return true
    }

    override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }

    companion object {
        private val MAX_CRAFT_GRID_WIDTH = 3
        private val MAX_CRAFT_GRID_HEIGHT = 3
    }
}
