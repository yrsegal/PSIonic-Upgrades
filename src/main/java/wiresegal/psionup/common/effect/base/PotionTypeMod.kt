package wiresegal.psionup.common.effect.base

import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 10:03 PM on 7/17/16.
 */
class PotionTypeMod(name: String, vararg potionEffects: PotionEffect) : PotionType(name, *potionEffects) {
    init {
        GameRegistry.register(this, ResourceLocation(LibMisc.MOD_ID, name))
    }
}
