package wiresegal.psionup.common.items.spell

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICADComponent
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.internal.VanillaPacketDispatcher
import vazkii.psi.api.spell.EnumSpellStat
import vazkii.psi.api.spell.ISpellSettable
import vazkii.psi.api.spell.Spell
import vazkii.psi.common.block.tile.TileProgrammer
import vazkii.psi.common.core.handler.PsiSoundHandler
import vazkii.psi.common.core.helper.ItemNBTHelper
import vazkii.psi.common.item.ItemSpellDrive
import vazkii.psi.common.spell.SpellCompiler
import wiresegal.psionup.client.core.GuiHandler
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.crafting.ModRecipes
import wiresegal.psionup.common.items.base.ICadComponentAcceptor
import wiresegal.psionup.common.items.base.ItemMod
import wiresegal.psionup.common.lib.LibMisc
import vazkii.psi.common.item.base.ItemMod as PsiItem
import vazkii.psi.common.item.base.ModItems as PsiItems

/**
 * @author WireSegal
 * Created at 8:43 AM on 3/20/16.
 */
class ItemCADMagazine(name: String) : ItemMod(name, name), ISocketable, ICadComponentAcceptor, ISpellSettable {

    companion object {
        fun getSocket(stack: ItemStack): ItemStack {
            val nbt = ItemNBTHelper.getCompound(stack, "socket", true)
            return ItemStack.loadItemStackFromNBT(nbt ?: return ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket))
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

    override fun requiresSneakForSpellSet(p0: ItemStack?) = false

    init {
        setMaxStackSize(1)
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for (i in ModRecipes.examplesockets) {
            subItems.add(setSocket(ItemStack(itemIn), i))
        }
    }

    override fun getPiece(stack: ItemStack, type: EnumCADComponent): ItemStack? {
        if (type != EnumCADComponent.SOCKET)
            return null
        return getSocket(stack)
    }

    override fun setPiece(stack: ItemStack, type: EnumCADComponent, piece: ItemStack?): ItemStack {
        if (type != EnumCADComponent.SOCKET)
            return stack
        return setSocket(stack, piece)
    }

    override fun acceptsPiece(stack: ItemStack, type: EnumCADComponent): Boolean {
        return type == EnumCADComponent.SOCKET
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack>? {
        if (!worldIn.isRemote && PsiAPI.getPlayerCAD(playerIn) != null) {
            playerIn.openGui(PsionicUpgrades.instance, GuiHandler.GUI_MAGAZINE, worldIn, 0, 0, 0)
            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)
    }

    override fun onItemUse(stack: ItemStack, playerIn: EntityPlayer?, worldIn: World?, pos: BlockPos?, hand: EnumHand?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val tile = worldIn!!.getTileEntity(pos)
        if (tile is TileProgrammer && playerIn != null) {
            val spell = getSpell(stack)
            if (spell != null) {
                val enabled = tile.isEnabled
                val compiled = SpellCompiler(spell)
                if ((compiled.compiledSpell.metadata.stats[EnumSpellStat.BANDWIDTH] ?: Integer.MAX_VALUE) > getBandwidth(stack) && !worldIn.isRemote)
                    playerIn.addChatComponentMessage(TextComponentTranslation("${LibMisc.MOD_ID}.misc.tooComplexBullet").setStyle(Style().setColor(TextFormatting.RED)))
                else if (!worldIn.isRemote) {
                    if (enabled && !tile.playerLock.isEmpty()) {
                        if (tile.playerLock != playerIn.name) {
                            playerIn.addChatComponentMessage(TextComponentTranslation("psimisc.notYourProgrammer").setStyle(Style().setColor(TextFormatting.RED)))
                            return EnumActionResult.SUCCESS
                        }
                    } else {
                        tile.playerLock = playerIn.name
                    }

                    tile.spell = spell
                    tile.onSpellChanged()
                    VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile)

                    return EnumActionResult.SUCCESS
                } else {
                    if (!enabled || tile.playerLock.isEmpty() || tile.playerLock == playerIn.name) {
                        tile.spell = spell
                        tile.onSpellChanged()
                        worldIn.markBlockRangeForRenderUpdate(tile.pos, tile.pos)
                        worldIn.playSound(pos!!.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, PsiSoundHandler.bulletCreate, SoundCategory.PLAYERS, 0.5f, 1.0f, false)
                        return EnumActionResult.SUCCESS
                    }
                }
            }
        }

        return EnumActionResult.PASS
    }

    fun getSpell(stack: ItemStack): Spell? {
        return ItemSpellDrive.getSpell(getBulletInSocket(stack, getSelectedSlot(stack)))
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        tooltipIfShift(tooltip, {
            val socketName = local(getSocket(stack).displayName)
            var line = TextFormatting.GREEN.toString() + local(EnumCADComponent.SOCKET.getName()) + TextFormatting.GRAY.toString() + ": " + socketName
            addToTooltip(tooltip, line)
            val var12 = EnumCADStat::class.java.enumConstants.size

            for (var13 in 0..var12 - 1) {
                val stat = EnumCADStat::class.java.enumConstants[var13]
                if (stat.sourceType == EnumCADComponent.SOCKET) {
                    val shrt = stat.getName()
                    val item = getSocket(stack).item
                    if (item is ICADComponent) {
                        val statVal = item.getCADStatValue(getSocket(stack), stat)
                        val statValStr = if (statVal == -1) "∞" else "" + statVal
                        line = " " + TextFormatting.AQUA + local(shrt) + TextFormatting.GRAY + ": " + statValStr
                        if (!line.isEmpty()) {
                            addToTooltip(tooltip, line)
                        }
                    }
                }
            }

            var slot = 0
            while (isSocketSlotAvailable(stack, slot)) {
                val name = getSocketedItemName(stack, slot, null)
                if (name != null) {
                    if (slot == getSelectedSlot(stack))
                        addToTooltip(tooltip, "| ${TextFormatting.WHITE}${TextFormatting.BOLD}$name")
                    else
                        addToTooltip(tooltip, "| ${TextFormatting.WHITE}$name")
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

    override fun setSpell(player: EntityPlayer, stack: ItemStack, spell: Spell) {
        val slot = this.getSelectedSlot(stack)
        val bullet = this.getBulletInSocket(stack, slot)
        val compiled = SpellCompiler(spell)
        if ((compiled.compiledSpell.metadata.stats[EnumSpellStat.BANDWIDTH] ?: Integer.MAX_VALUE) > getBandwidth(stack)) {
            if (!player.worldObj.isRemote)
                player.addChatComponentMessage(TextComponentTranslation("${LibMisc.MOD_ID}.misc.tooComplex").setStyle(Style().setColor(TextFormatting.RED)))
        } else if (bullet != null && bullet.item is ISpellSettable) {
            (bullet.item as ISpellSettable).setSpell(player, bullet, spell)
            this.setBulletInSocket(stack, slot, bullet)
        }
    }
}
