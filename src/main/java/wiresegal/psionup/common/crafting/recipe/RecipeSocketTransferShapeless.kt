package wiresegal.psionup.common.crafting.recipe

import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.ShapelessOreRecipe
import vazkii.psi.api.cad.ISocketable
import wiresegal.psionup.common.core.helper.IteratorSocketable

/**
 * @author WireSegal
 * Created at 7:22 PM on 7/9/16.
 */
class RecipeSocketTransferShapeless(val result: ItemStack, vararg recipe: Any) : ShapelessOreRecipe(result, *recipe) {

    override fun getCraftingResult(var1: InventoryCrafting): ItemStack {
        val output = result.copy()
        for (i in 0..var1.sizeInventory - 1) {
            val stack = var1.getStackInSlot(i)
            if (stack != null && stack.item is ISocketable && output.item is ISocketable) {
                output.tagCompound = stack.tagCompound
                break
            }
        }
        return output
    }

}
