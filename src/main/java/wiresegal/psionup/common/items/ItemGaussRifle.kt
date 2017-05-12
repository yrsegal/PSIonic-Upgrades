package wiresegal.psionup.common.items

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.client.renderer.block.model.IBakedModel
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.core.handler.PsiSoundHandler
import wiresegal.psionup.common.entity.EntityGaussPulse
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 10:10 PM on 7/13/16.
 */
class ItemGaussRifle(name: String) : ItemMod(name), IItemColorProvider, IGlowingItem {

    init {
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel): IBakedModel? {
        return IGlowingItem.Helper.wrapperBake(model, false, 0)
    }

    override val itemColorFunction: ((stack: ItemStack, tintIndex: Int) -> Int)?
        get() = {
            _, i ->
            if (i == 0)
                pulseColor(0xB87333)
            else if (i == 1)
                ICADColorizer.DEFAULT_SPELL_COLOR
            else 0xFFFFFF
        }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val data = PlayerDataHandler.get(playerIn)
        val ammo = findAmmo(playerIn)
        if (playerIn.capabilities.isCreativeMode || data.availablePsi > 0 || (!ammo.isEmpty && data.availablePsi > 0)) {
            if (!playerIn.capabilities.isCreativeMode) {
                if (ammo.isEmpty)
                    data.deductPsi(1000, 100, true)
                else {
                    data.deductPsi(250, 10, true)
                    ammo.count--
                    if (ammo.count == 0)
                        playerIn.inventory.deleteStack(ammo)
                }
            }

            playerIn.swingArm(hand)

            val status = if (!ammo.isEmpty) {
                if (playerIn.capabilities.isCreativeMode)
                    EntityGaussPulse.AmmoStatus.DEPLETED
                else
                    EntityGaussPulse.AmmoStatus.AMMO
            } else
                EntityGaussPulse.AmmoStatus.NOTAMMO

            val proj = EntityGaussPulse(worldIn, playerIn, status)
            if (!worldIn.isRemote) worldIn.spawnEntity(proj)
            val look = playerIn.lookVec
            if (look.xCoord != 0.0 && look.zCoord != 0.0)
                playerIn.knockBack(playerIn, 0.5f, look.xCoord, look.zCoord)
            else
                playerIn.motionY += 0.5
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 1f, 1f)

            if (!ammo.isEmpty && !playerIn.capabilities.isCreativeMode)
                playerIn.cooldownTracker.setCooldown(this, 30)
        }
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand))
    }

    private fun findAmmo(player: EntityPlayer): ItemStack {
        if (player.heldItemOffhand?.item == ModItems.gaussBullet) {
            return player.getHeldItem(EnumHand.OFF_HAND)
        } else if (player.heldItemMainhand?.item == ModItems.gaussBullet) {
            return player.getHeldItem(EnumHand.MAIN_HAND)
        } else {
            for (i in 0..player.inventory.sizeInventory - 1) {
                val itemstack = player.inventory.getStackInSlot(i)

                if (itemstack?.item == ModItems.gaussBullet) {
                    return itemstack
                }
            }

            return ItemStack.EMPTY
        }
    }

    fun pulseColor(rgb: Int): Int {
        val add = (Math.sin(ClientTickHandler.ticksInGame * 0.2) * 24).toInt()
        val r = (rgb and (0xFF shl 16)) shr 16
        val b = (rgb and (0xFF shl 8)) shr 8
        val g = (rgb and (0xFF shl 0)) shr 0
        return (Math.max(Math.min(r + add, 255), 0) shl 16) or
                (Math.max(Math.min(b + add, 255), 0) shl 8) or
                (Math.max(Math.min(g + add, 255), 0) shl 0)
    }
}
