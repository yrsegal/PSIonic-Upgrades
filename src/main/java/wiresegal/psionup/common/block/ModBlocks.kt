package wiresegal.psionup.common.block

import net.minecraft.block.Block
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.psionup.common.block.spell.BlockConjuredPulsar
import wiresegal.psionup.common.block.tile.TileConjuredPulsar
import wiresegal.psionup.common.lib.LibMisc
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 4:42 PM on 3/20/16.
 */
object ModBlocks {
    var conjured: Block

    init {
        conjured = BlockConjuredPulsar(LibNames.Blocks.CONJURED_PULSAR)

        GameRegistry.registerTileEntity(TileConjuredPulsar::class.java, "${LibMisc.MOD_ID}:pulsar");
    }
}
