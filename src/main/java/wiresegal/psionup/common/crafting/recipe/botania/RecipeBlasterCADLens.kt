package wiresegal.psionup.common.crafting.recipe.botania

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import vazkii.botania.api.mana.ILens
import vazkii.botania.api.mana.ILensControl
import vazkii.botania.common.item.ItemManaGun
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICAD
import wiresegal.psionup.api.enabling.botania.IBlasterComponent

/**
 * @author WireSegal
 * *         Created at 10:19 PM on 3/31/16.
 */
class RecipeBlasterCADLens : IRecipe {

    override fun matches(var1: InventoryCrafting, var2: World?): Boolean {
        var foundLens = false
        var foundCAD = false
        var cad: ItemStack = ItemStack.EMPTY

        (0..var1.sizeInventory - 1)
                .map { var1.getStackInSlot(it) }
                .filterNot { it.isEmpty }
                .forEach {
                    if (it.item is ICAD) {
                        if (foundCAD)
                            return false

                        val item = it.item as ICAD
                        if (item.getComponentInSlot(it, EnumCADComponent.ASSEMBLY).item is IBlasterComponent) {
                            foundCAD = true
                            cad = it
                        } else
                            return false
                    } else if (it.item is ILens) {
                        if (foundLens)
                            return false

                        if (it.item !is ILensControl || !(it.item as ILensControl).isControlLens(it)) {
                            foundLens = true
                        } else
                            return false
                    } else
                        return false // Found an invalid item, breaking the recipe
                }
        return foundCAD && ((!ItemManaGun.getLens(cad).isEmpty) xor foundLens)
    }

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack {
        var lens: ItemStack = ItemStack.EMPTY
        var gun: ItemStack = ItemStack.EMPTY

        (0..var1.sizeInventory - 1)
                .mapNotNull { var1.getStackInSlot(it) }
                .forEach {
                    if (it.item is ICAD)
                        gun = it
                    else if (it.item is ILens)
                        lens = it
                }

        if (gun.isEmpty)
            return ItemStack.EMPTY

        val gunCopy = gun.copy()
        ItemManaGun.setLens(gunCopy, lens)

        return gunCopy
    }


    override fun getRecipeSize(): Int {
        return 10
    }

    override fun getRecipeOutput(): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack> {
        val ret = NonNullList.withSize(9, ItemStack.EMPTY)
        var cadIndex = -1
        var cad: ItemStack = ItemStack.EMPTY

        for (icad in ret.indices) {
            val stack = inv.getStackInSlot(icad)
            if (stack != null && stack.item is ICAD) {
                cad = stack
                cadIndex = icad
            } else {
                ret[icad] = ForgeHooks.getContainerItem(stack)
            }
        }

        if (!cad.isEmpty && cadIndex != -1 && ItemManaGun.getLens(cad) != null) {
            ret[cadIndex] = ItemManaGun.getLens(cad)
        }

        return ret
    }
}
