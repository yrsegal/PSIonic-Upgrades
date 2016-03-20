package wiresegal.psionup.common.block

import net.minecraft.block.Block
import net.minecraftforge.fml.common.registry.GameRegistry

/**
 * @author WireSegal
 * Created at 4:42 PM on 3/20/16.
 */
object ModBlocks {
    var conjured: Block

    init {
        conjured = BlockConjuredPulsar("conjuredPulsar")

        GameRegistry.registerTileEntity(TileConjuredPulsar::class.java, "psionup:pulsar");
    }
}
