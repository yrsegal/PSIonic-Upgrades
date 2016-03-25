package wiresegal.psionup.common.crafting

import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.ShapelessRecipes
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.oredict.OreDictionary
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICADComponent
import wiresegal.psionup.common.items.base.ICadComponentAcceptor
import java.util.*

/**
 * @author WireSegal
 * *         Created at 9:37 AM on 3/21/16.
 */
class RecipeCadComponentShapeless : IRecipe {
    private var output: ItemStack
    val input = ArrayList<Any>()

    constructor(result: Block, vararg recipe: Any) : this(ItemStack(result), *recipe) {
    }

    constructor(result: Item, vararg recipe: Any) : this(ItemStack(result), *recipe) {
    }

    constructor(result: ItemStack, vararg recipe: Any) {
        output = result.copy()

        for (`in` in recipe) {
            if (`in` is EnumCADComponent) {
                input.add(`in`)
            } else if (`in` is ItemStack) {
                input.add(`in`.copy())
            } else if (`in` is Item) {
                input.add(ItemStack(`in`))
            } else if (`in` is Block) {
                input.add(ItemStack(`in`))
            } else if (`in` is String) {
                input.add(OreDictionary.getOres(`in`))
            } else {
                var ret = "Invalid shapeless ore recipe: "
                for (tmp in recipe) {
                    ret += tmp.toString() + ", "
                }
                ret += output
                throw RuntimeException(ret)
            }
        }
    }

    internal constructor(recipe: ShapelessRecipes, replacements: Map<ItemStack, String>) {
        output = recipe.recipeOutput

        for (ingred in recipe.recipeItems) {
            var finalObj: Any = ingred
            for (replace in replacements.entries) {
                if (OreDictionary.itemMatches(replace.key, ingred, false)) {
                    finalObj = OreDictionary.getOres(replace.value)
                    break
                }
            }
            input.add(finalObj)
        }
    }

    override fun getRecipeSize(): Int {
        return input.size
    }

    override fun getRecipeOutput(): ItemStack? {
        return output
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

    @SuppressWarnings("unchecked")
    override fun matches(var1: InventoryCrafting, world: World): Boolean {
        val required = ArrayList(input)

        for (x in 0..var1.sizeInventory - 1) {
            val slot = var1.getStackInSlot(x)

            if (slot != null) {
                var inRecipe = false
                val req = required.iterator()

                while (req.hasNext()) {
                    var match = false

                    val next = req.next()

                    if (next is EnumCADComponent) {
                        if (slot.item is ICADComponent) {
                            val component = slot.item as ICADComponent
                            if (component.getComponentType(slot) == next && !(component is ICadComponentAcceptor && component.acceptsPiece(slot, next))) {
                                match = true
                            }
                        }
                    } else if (next is ItemStack) {
                        match = OreDictionary.itemMatches(next, slot, false)
                    } else if (next is List<*>) {
                        val itr = next.iterator()
                        while (itr.hasNext() && !match) {
                            val stack = itr.next()
                            if (stack is ItemStack)
                                match = OreDictionary.itemMatches(stack, slot, false)
                        }
                    }

                    if (match) {
                        inRecipe = true
                        required.remove(next)
                        break
                    }
                }

                if (!inRecipe) {
                    return false
                }
            }
        }

        return required.isEmpty()
    }

    override fun getRemainingItems(inv: InventoryCrafting): Array<ItemStack?> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }
}
