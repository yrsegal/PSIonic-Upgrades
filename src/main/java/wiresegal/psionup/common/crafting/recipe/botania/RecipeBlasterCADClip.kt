package wiresegal.psionup.common.crafting.recipe.botania

import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import vazkii.botania.common.item.ItemManaGun
import vazkii.botania.common.item.ModItems
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICAD
import wiresegal.psionup.common.items.component.botania.CompatItems

/**
 * @author WireSegal
 * *         Created at 7:36 AM on 4/1/16.
 */
class RecipeBlasterCADClip : IRecipe {
    override fun matches(var1: InventoryCrafting, var2: World): Boolean {
        var foundCAD = false
        var foundClip = false

        for (i in 0..var1.sizeInventory - 1) {
            val stack = var1.getStackInSlot(i)
            if (stack != null) {
                if (stack.item is ICAD && (stack.item as ICAD).getComponentInSlot(stack, EnumCADComponent.ASSEMBLY).item === CompatItems.blaster && !ItemManaGun.hasClip(stack) && !foundClip)
                    foundCAD = true
                else if (stack.item === ModItems.clip && !foundClip)
                    foundClip = true
                else {
                    return false // Found an invalid item, breaking the recipe
                }
            }
        }
        return foundCAD && foundClip
    }

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
        var gun: ItemStack? = null

        for (i in 0..var1.sizeInventory - 1) {
            val stack = var1.getStackInSlot(i)
            if (stack != null && stack.item is ItemManaGun)
                gun = stack
        }

        if (gun == null)
            return null

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

    override fun getRecipeOutput(): ItemStack? {
        return null
    }

    override fun getRemainingItems(inv: InventoryCrafting): Array<ItemStack> {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv)
    }
}
