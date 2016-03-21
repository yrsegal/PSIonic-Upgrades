package wiresegal.psionup.common.items

import net.minecraft.client.Minecraft
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
import net.minecraftforge.fml.relauncher.FMLLaunchHandler
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICADComponent
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.internal.VanillaPacketDispatcher
import vazkii.psi.api.spell.EnumSpellStat
import vazkii.psi.api.spell.ISpellSettable
import vazkii.psi.api.spell.Spell
import vazkii.psi.client.core.handler.PsiSoundHandler
import vazkii.psi.common.block.tile.TileProgrammer
import vazkii.psi.common.core.helper.ItemNBTHelper
import vazkii.psi.common.item.ItemSpellDrive
import vazkii.psi.common.spell.SpellCompiler
import vazkii.psi.common.item.base.ItemMod as PsiItem

/**
 * @author WireSegal
 * Created at 8:43 AM on 3/20/16.
 */
class ItemCADMagazine(name: String) : ItemMod(name, name), ISocketable, ICadComponentAcceptor, ISpellSettable {

    companion object {
        fun getSocket(stack: ItemStack): ItemStack {
            val nbt = ItemNBTHelper.getCompound(stack, "socket", true)
            return ItemStack.loadItemStackFromNBT(nbt) ?: return ItemStack(ModItems.socket)
        }
        fun setSocket(stack: ItemStack, socket: ItemStack?): ItemStack {
            if (socket == null) {
                ItemNBTHelper.setCompound(stack, "socket", null)
            } else {
                val nbt = NBTTagCompound()
                socket.writeToNBT(nbt)
                ItemNBTHelper.setCompound(stack, "socket", nbt)
            }
            return stack
        }

        fun getSocketSlots(stack: ItemStack): Int {
            val socket = getSocket(stack)
            val item = socket.item
            if (item is ICADComponent) {
                return item.getCADStatValue(socket, EnumCADStat.SOCKETS)
            }
            return 0
        }

        fun getBandwidth(stack: ItemStack): Int {
            val socket = getSocket(stack)
            val item = socket.item
            if (item is ICADComponent) {
                return item.getCADStatValue(socket, EnumCADStat.BANDWIDTH)
            }
            return 0
        }
    }

    override fun getPiece(stack: ItemStack, type: EnumCADComponent): ItemStack? {
        if (type != EnumCADComponent.SOCKET)
            return null
        return getSocket(stack)
    }

    override fun setPiece(stack: ItemStack, type: EnumCADComponent, socket: ItemStack?): ItemStack {
        if (type != EnumCADComponent.SOCKET)
            return stack
        return setSocket(stack, socket)
    }

    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer?, worldIn: World?, pos: BlockPos?, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val tile = worldIn!!.getTileEntity(pos)
        if (tile is TileProgrammer && playerIn != null) {
            val spell = getSpell(stack)
            if (spell != null && playerIn.isSneaking) {
                val enabled = tile.isEnabled
                val compiled = SpellCompiler(spell)
                if ((compiled.compiledSpell.metadata.stats[EnumSpellStat.BANDWIDTH] ?: Integer.MAX_VALUE) > getBandwidth(stack) && !worldIn.isRemote)
                    playerIn.addChatComponentMessage(TextComponentTranslation("psionup.misc.tooComplexBullet").setChatStyle(Style().setColor(TextFormatting.RED)))
                else if (!worldIn.isRemote) {
                    if (enabled && !tile.playerLock.isEmpty()) {
                        if (tile.playerLock != playerIn.name) {
                            if (!worldIn.isRemote) {
                                playerIn.addChatComponentMessage(TextComponentTranslation("psimisc.notYourProgrammer").setChatStyle(Style().setColor(TextFormatting.RED)))
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
        }

        return EnumActionResult.PASS
    }

    fun getSpell(stack: ItemStack): Spell? {
        return ItemSpellDrive.getSpell(getBulletInSocket(stack, getSelectedSlot(stack)))
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>?, advanced: Boolean) {
        PsiItem.tooltipIfShift(tooltip, {
            val socketName = PsiItem.local(getSocket(stack).displayName)
            var line = TextFormatting.GREEN.toString() + PsiItem.local(EnumCADComponent.SOCKET.getName()) + TextFormatting.GRAY.toString() + ": " + socketName
            PsiItem.addToTooltip(tooltip, line)
            val var12 = EnumCADStat::class.java.enumConstants.size

            for (var13 in 0..var12 - 1) {
                val stat = EnumCADStat::class.java.enumConstants[var13]
                if (stat.sourceType == EnumCADComponent.SOCKET) {
                    val shrt = stat.getName()
                    val item = getSocket(stack).item
                    if (item is ICADComponent) {
                        val statVal = item.getCADStatValue(getSocket(stack), stat)
                        val statValStr = if (statVal == -1) "∞" else "" + statVal
                        line = " " + TextFormatting.AQUA + PsiItem.local(shrt) + TextFormatting.GRAY + ": " + statValStr
                        if (!line.isEmpty()) {
                            PsiItem.addToTooltip(tooltip, line)
                        }
                    }
                }
            }

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
        return slot < getSocketSlots(stack)
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
        val compiled = SpellCompiler(spell)
        if ((compiled.compiledSpell.metadata.stats[EnumSpellStat.BANDWIDTH] ?: Integer.MAX_VALUE) > getBandwidth(stack))
            // TODO: tell player about it
        else if (bullet != null && bullet.item is ISpellSettable) {
            (bullet.item as ISpellSettable).setSpell(bullet, spell)
            this.setBulletInSocket(stack, slot, bullet)
        }
    }
}
