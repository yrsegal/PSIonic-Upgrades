//package wiresegal.psionup.common.crafting.recipe.botania
//
//import net.minecraft.inventory.InventoryCrafting
//import net.minecraft.item.ItemStack
//import net.minecraft.item.crafting.IRecipe
//import net.minecraft.world.World
//import net.minecraftforge.common.ForgeHooks
//import vazkii.botania.api.mana.ILens
//import vazkii.botania.api.mana.ILensControl
//import vazkii.botania.common.item.ItemManaGun
//import vazkii.psi.api.cad.EnumCADComponent
//import vazkii.psi.api.cad.ICAD
//import wiresegal.psionup.api.enabling.botania.IBlasterComponent
//
///**
// * @author WireSegal
// * *         Created at 10:19 PM on 3/31/16.
// */
//class RecipeBlasterCADLens : IRecipe {
//
//    override fun matches(var1: InventoryCrafting, var2: World?): Boolean {
//        var foundLens = false
//        var foundCAD = false
//        var cad: ItemStack? = null
//
//        for (i in 0..var1.sizeInventory - 1) {
//            val stack = var1.getStackInSlot(i)
//            if (stack != null) {
//                if (stack.item is ICAD) {
//                    if (foundCAD)
//                        return false
//
//                    val item = stack.item as ICAD
//                    if (item.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY).item is IBlasterComponent) {
//                        foundCAD = true
//                        cad = stack
//                    } else
//                        return false
//                } else if (stack.item is ILens) {
//                    if (foundLens)
//                        return false
//
//                    if (stack.item !is ILensControl || !(stack.item as ILensControl).isControlLens(stack)) {
//                        foundLens = true
//                    } else
//                        return false
//                } else
//                    return false // Found an invalid item, breaking the recipe
//            }
//        }
//        return foundCAD && ((ItemManaGun.getLens(cad) != null) xor foundLens)
//    }
//
//    override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
//        var lens: ItemStack? = null
//        var gun: ItemStack? = null
//
//        for (i in 0..var1.sizeInventory - 1) {
//            val stack = var1.getStackInSlot(i)
//            if (stack != null) {
//                if (stack.item is ICAD)
//                    gun = stack
//                else if (stack.item is ILens)
//                    lens = stack
//            }
//        }
//
//        if (gun == null)
//            return null
//
//        val gunCopy = gun.copy()
//        ItemManaGun.setLens(gunCopy, lens)
//
//        return gunCopy
//    }
//
//
//    override fun getRecipeSize(): Int {
//        return 10
//    }
//
//    override fun getRecipeOutput(): ItemStack? {
//        return null
//    }
//
//    override fun getRemainingItems(inv: InventoryCrafting): Array<ItemStack?> {
//        val ret = arrayOfNulls<ItemStack>(inv.sizeInventory)
//        var cadIndex = -1
//        var cad: ItemStack? = null
//
//        for (icad in ret.indices) {
//            val stack = inv.getStackInSlot(icad)
//            if (stack != null && stack.item is ICAD) {
//                cad = stack
//                cadIndex = icad
//            } else {
//                ret[icad] = ForgeHooks.getContainerItem(stack)
//            }
//        }
//
//        if (cad != null && cadIndex != -1 && ItemManaGun.getLens(cad) != null) {
//            ret[cadIndex] = ItemManaGun.getLens(cad)
//        }
//
//        return ret
//    }
//}
