package wiresegal.psionup.client.compat.jei

import mezz.jei.api.BlankModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import wiresegal.psionup.client.compat.jei.crafting.ShapedCadRecipeHandler
import wiresegal.psionup.client.compat.jei.crafting.ShapelessCadRecipeHandler

/**
 * @author WireSegal
 * Created at 10:06 AM on 3/21/16.
 */
@JEIPlugin
class JEICompat : BlankModPlugin() {
    override fun register(registry: IModRegistry) {
        registry.addRecipeHandlers(ShapedCadRecipeHandler(), ShapelessCadRecipeHandler())
    }
}
