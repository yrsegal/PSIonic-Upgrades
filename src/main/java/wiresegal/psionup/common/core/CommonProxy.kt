package wiresegal.psionup.common.core

import com.teamwizardry.librarianlib.features.config.EasyConfigHandler
import com.teamwizardry.librarianlib.features.structure.InWorldRender.pos
import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import vazkii.psi.api.cad.ICADColorizer
import wiresegal.psionup.api.PsionicAPI
import wiresegal.psionup.common.block.ModBlocks
import wiresegal.psionup.common.core.helper.FlowColors
import wiresegal.psionup.common.crafting.ModRecipes
import wiresegal.psionup.common.effect.ModPotions
import wiresegal.psionup.common.entity.ModEntities
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.spell.ModPieces

/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {

        EasyConfigHandler.init()

        ModPotions
        ModItems
        ModBlocks
        ModPieces
        ModEntities

        FlowColors.EventHandler

        PsionicAPI.setInternalPropertyComparator { (properties, side) ->
            if (side.axis == EnumFacing.Axis.Y) {
                EnumFacing.HORIZONTALS.map {
                    PsionicMethodHandles.calculateInputStrength(properties.world, properties.pos, it)
                }.max() ?: 0
            } else
                PsionicMethodHandles.calculateInputStrength(properties.world, properties.pos, side)
        }
    }

    open fun init(e: FMLInitializationEvent) {
        ModRecipes
    }

    open fun post(e: FMLPostInitializationEvent) {
        //NO-OP
    }
}
