package wiresegal.psionup.common.core

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.psionup.common.block.ModBlocks
import wiresegal.psionup.common.core.helper.FlowColors
import wiresegal.psionup.common.crafting.ModRecipes
import wiresegal.psionup.common.effect.ModPotions
import wiresegal.psionup.common.entity.ModEntities
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.network.NetworkHandler
import wiresegal.psionup.common.spell.ModPieces

/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {

        ConfigHandler.loadConfig(e.suggestedConfigurationFile)

        ModItems
        ModBlocks
        ModPieces
        ModPotions
        ModEntities
        NetworkHandler

        FlowColors.EventHandler
    }

    open fun init(e: FMLInitializationEvent) {
        ModRecipes
    }

    open fun post(e: FMLPostInitializationEvent) {
        //NO-OP
    }
}
