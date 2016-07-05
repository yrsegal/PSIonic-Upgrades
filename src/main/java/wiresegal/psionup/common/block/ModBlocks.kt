package wiresegal.psionup.common.block

import net.minecraft.block.Block
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.psionup.common.block.spell.BlockConjuredPulsar
import wiresegal.psionup.common.block.spell.BlockConjuredStar
import wiresegal.psionup.common.block.tile.TileConjuredPulsar
import wiresegal.psionup.common.block.tile.TileCracklingStar
import wiresegal.psionup.common.lib.LibMisc
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 4:42 PM on 3/20/16.
 */
object ModBlocks {
    val conjured: Block
    val crackle: Block

    init {
        conjured = BlockConjuredPulsar(LibNames.Blocks.CONJURED_PULSAR)
        crackle = BlockConjuredStar(LibNames.Blocks.CONJURED_STAR)

        GameRegistry.registerTileEntity(TileConjuredPulsar::class.java, "${LibMisc.MOD_ID}:pulsar")
        GameRegistry.registerTileEntity(TileCracklingStar::class.java, "${LibMisc.MOD_ID}:star")
    }
}
