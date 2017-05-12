package wiresegal.psionup.common.crafting.recipe.botania

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import vazkii.botania.common.item.ItemManaGun
import vazkii.botania.common.item.ModItems
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICAD
import wiresegal.psionup.api.enabling.botania.IBlasterComponent

/**
 * @author WireSegal
 * *         Created at 7:36 AM on 4/1/16.
 */
class RecipeBlasterCADClip : IRecipe {
    override fun matches(var1: InventoryCrafting, var2: World?): Boolean {
        var foundCAD = false
        var foundClip = false

        (0..var1.sizeInventory - 1)
                .map { var1.getStackInSlot(it) }
                .filterNot { it.isEmpty }
                .forEach {
                    if (it.item is ICAD && (it.item as ICAD).getComponentInSlot(it, EnumCADComponent.ASSEMBLY).item is IBlasterComponent && !ItemManaGun.hasClip(it) && !foundCAD)
                        foundCAD = true
                    else if (it.item === ModItems.clip && !foundClip)
                        foundClip = true
                    else {
                        return false // Found an invalid item, breaking the recipe
                    }
                }
        return foundCAD && foundClip
    }

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack {
        val gun: ItemStack = (0..var1.sizeInventory - 1)
                .map { var1.getStackInSlot(it) }
                .lastOrNull { !it.isEmpty && it.item is ICAD } ?: ItemStack.EMPTY

        if (gun.isEmpty)
            return gun

        val lens = ItemManaGun.getLens(gun)
        ItemManaGun.setLens(gun, null)
        val gunCopy = gun.copy()
        ItemManaGun.setClip(gunCopy, true)
        ItemManaGun.setLensAtPos(gunCopy, lens, 0)
        return gunCopy
    }

    override fun getRecipeSize(): Int {
        return 10
    }

    override fun getRecipeOutput(): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getRemainingItems(inv: InventoryCrafting): NonNullList<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }
}
