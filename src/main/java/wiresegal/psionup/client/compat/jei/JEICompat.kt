package wiresegal.psionup.client.compat.jei

import mezz.jei.api.BlankModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import net.minecraft.item.ItemStack
import wiresegal.psionup.client.compat.jei.crafting.ShapedCadRecipeHandler
import wiresegal.psionup.client.compat.jei.crafting.ShapelessCadRecipeHandler
import wiresegal.psionup.common.items.base.ModItems

/**
 * @author WireSegal
 * Created at 10:06 AM on 3/21/16.
 */
@JEIPlugin
class JEICompat : BlankModPlugin() {
    override fun register(registry: IModRegistry) {
        registry.addRecipeHandlers(ShapedCadRecipeHandler(), ShapelessCadRecipeHandler())

        registry.addDescription(ItemStack(ModItems.liquidColorizer), "jei.psionup.drained.desc")
        registry.addDescription(ItemStack(ModItems.emptyColorizer), "jei.psionup.drained.desc")
    }
}
