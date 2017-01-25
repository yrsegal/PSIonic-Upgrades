package wiresegal.psionup.client.gui.flashring

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import vazkii.psi.common.block.tile.TileProgrammer
import vazkii.psi.common.item.ItemSpellDrive

/**
 * @author WireSegal
 * Created at 10:14 PM on 7/9/16.
 */
class FlashRingProgrammingWrapper(val player: EntityPlayer, val stack: ItemStack) : TileProgrammer() {

    init {
        spell = ItemSpellDrive.getSpell(stack)
        enabled = true
    }

    override fun onSpellChanged() {
        //NO-OP
    }

    override fun canPlayerInteract(player: EntityPlayer?): Boolean {
        return true
    }

    override fun getWorld(): World {
        return object : World(null, null, player.world.provider, null, true) {
            override fun createChunkProvider(): IChunkProvider? {
                return null
            }

            override fun isChunkLoaded(x: Int, z: Int, allowEmpty: Boolean): Boolean {
                return false
            }

            override fun getTileEntity(pos: BlockPos?): TileEntity {
                return this@FlashRingProgrammingWrapper
            }
        }
    }

}
