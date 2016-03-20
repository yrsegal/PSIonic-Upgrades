package wiresegal.psionup.common.core

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.psionup.common.crafting.ModRecipes
import wiresegal.psionup.common.items.ModItems

/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {
        ModItems
        ModRecipes
    }

    open fun init(e: FMLInitializationEvent) {

    }

    open fun post(e: FMLPostInitializationEvent) {

    }
}
