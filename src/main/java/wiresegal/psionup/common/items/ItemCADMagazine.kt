package wiresegal.psionup.common.items

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.internal.VanillaPacketDispatcher
import vazkii.psi.api.spell.ISpellSettable
import vazkii.psi.api.spell.Spell
import vazkii.psi.client.core.handler.PsiSoundHandler
import vazkii.psi.common.block.tile.TileProgrammer
import vazkii.psi.common.core.helper.ItemNBTHelper
import vazkii.psi.common.item.ItemSpellDrive
import vazkii.psi.common.item.base.ItemMod as PsiItem

/**
 * @author WireSegal
 * Created at 8:43 AM on 3/20/16.
 */
class ItemCADMagazine(name: String) : ItemMod(name, name), ISocketable, ISpellSettable {

    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer?, worldIn: World?, pos: BlockPos?, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val tile = worldIn!!.getTileEntity(pos)
        if (tile is TileProgrammer && playerIn != null && playerIn.isSneaking) {
            val spell = getSpell(stack)
            if (spell == null && tile.canCompile()) {
                setSpell(stack, tile.spell)
                if (!worldIn.isRemote) {
                    worldIn.playSound(pos!!.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, 0.5f, 1.0f, false)
                }

                return EnumActionResult.SUCCESS
            }

            if (spell != null) {
                val enabled = tile.isEnabled
                if (enabled && !tile.playerLock.isEmpty()) {
                    if (tile.playerLock != playerIn.name) {
                        if (!worldIn.isRemote) {
                            playerIn.addChatComponentMessage(TextComponentTranslation("psimisc.notYourProgrammer", *arrayOfNulls<Any>(0)).setChatStyle(Style().setColor(TextFormatting.RED)))
                        }

                        return EnumActionResult.SUCCESS
                    }
                } else {
                    tile.playerLock = playerIn.name
                }

                tile.spell = spell
                tile.onSpellChanged()
                if (!worldIn.isRemote) {
                    worldIn.playSound(pos!!.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, 0.5f, 1.0f, false)
                    VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile)
                }

                return EnumActionResult.SUCCESS
            }
        }

        return EnumActionResult.PASS
    }

    fun getSpell(stack: ItemStack): Spell? {
        return ItemSpellDrive.getSpell(getBulletInSocket(stack, getSelectedSlot(stack)))
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>?, advanced: Boolean) {
        PsiItem.tooltipIfShift(tooltip, {
            var slot = 0
            while (isSocketSlotAvailable(stack, slot)) {
                val name = getSocketedItemName(stack, slot, null)
                if (name != null) {
                    val displaySlot = if (slot < 9) "0${slot + 1}" else "${slot + 1}"
                    if (slot == getSelectedSlot(stack))
                        PsiItem.addToTooltip(tooltip, "$displaySlot | ${TextFormatting.WHITE}${TextFormatting.BOLD}$name")
                    else
                        PsiItem.addToTooltip(tooltip, "$displaySlot | ${TextFormatting.WHITE}$name")
                }
                slot++
            }
        })
    }

    fun getSocketedItemName(stack: ItemStack?, slot: Int, fallback: String?): String? {
        if (stack != null && stack.item is ISocketable) {
            val socketable = stack.item as ISocketable
            val item = socketable.getBulletInSocket(stack, slot)
            return if (item == null) fallback else item.displayName
        } else {
            return fallback
        }
    }

    override fun isSocketSlotAvailable(stack: ItemStack, slot: Int): Boolean {
        return slot < 12 // Temporary, use custom recipe later
    }

    override fun getBulletInSocket(stack: ItemStack, slot: Int): ItemStack? {
        val name = "bullet" + slot
        val cmp = ItemNBTHelper.getCompound(stack, name, true)
        return if (cmp == null) null else ItemStack.loadItemStackFromNBT(cmp)
    }

    override fun setBulletInSocket(stack: ItemStack, slot: Int, bullet: ItemStack?) {
        val name = "bullet" + slot
        val cmp = NBTTagCompound()
        bullet?.writeToNBT(cmp)

        ItemNBTHelper.setCompound(stack, name, cmp)
    }

    override fun getSelectedSlot(stack: ItemStack): Int {
        return ItemNBTHelper.getInt(stack, "selectedSlot", 0)
    }

    override fun setSelectedSlot(stack: ItemStack, slot: Int) {
        ItemNBTHelper.setInt(stack, "selectedSlot", slot)
    }

    override fun setSpell(stack: ItemStack, spell: Spell) {
        val slot = this.getSelectedSlot(stack)
        val bullet = this.getBulletInSocket(stack, slot)
        if (bullet != null && bullet.item is ISpellSettable) {
            (bullet.item as ISpellSettable).setSpell(bullet, spell)
            this.setBulletInSocket(stack, slot, bullet)
        }

    }
}
