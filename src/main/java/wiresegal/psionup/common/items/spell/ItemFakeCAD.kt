package wiresegal.psionup.common.items.spell

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.local
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.tooltipIfShift
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.ISpellSettable
import vazkii.psi.api.spell.Spell
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.core.handler.PsiSoundHandler
import vazkii.psi.common.item.ItemCAD
import vazkii.psi.common.item.base.ModItems
import wiresegal.psionup.common.core.helper.FlowColors
import vazkii.arl.item.ItemMod as PsiItem

/**
 * @author WireSegal
 * Created at 8:46 AM on 3/20/16.
 */
class ItemFakeCAD(name: String) : ItemMod(name), ISocketable, ISpellSettable, IItemColorProvider, FlowColors.IAcceptor {

    init {
        setMaxStackSize(1)
    }

    override val itemColorFunction: ((stack: ItemStack, tintIndex: Int) -> Int)?
        get() = {
            stack, tintIndex ->
            if (tintIndex == 1) {
                val colorizer = FlowColors.getColor(stack)
                if (colorizer.isEmpty) 0 else Psi.proxy.getColorizerColor(colorizer).rgb
            } else 0xFFFFFF
        }

    override fun onItemRightClick(worldIn: World, player: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack>? {
        val itemstack = player.getHeldItem(hand)
        val data = PlayerDataHandler.get(player)
        val playerCad = PsiAPI.getPlayerCAD(player)
        if (playerCad != null) {
            val bullet = this.getBulletInSocket(itemstack, this.getSelectedSlot(itemstack))
            if (bullet.isEmpty) {
                if (ItemCAD.craft(player, ItemStack(Items.REDSTONE), ItemStack(ModItems.material))) {
                    if (!worldIn.isRemote) {
                        worldIn.playSound(player, player.posX, player.posY, player.posZ, PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 0.5f, (0.5 + Math.random() * 0.5).toFloat())
                    }

                    data.deductPsi(100, 60, true)
                    if (data.level == 0) {
                        data.levelUp()
                    }

                    return ActionResult(EnumActionResult.SUCCESS, itemstack)
                }
            } else {
                ItemCAD.cast(player.world, player, data, bullet, playerCad, 40, 25, 0.5f, null)
                return ActionResult(EnumActionResult.SUCCESS, itemstack)
            }
        }
        return super.onItemRightClick(worldIn, player, hand)
    }

    override fun requiresSneakForSpellSet(p0: ItemStack): Boolean {
        return false
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        tooltipIfShift(tooltip, {
            val componentName = local(ISocketable.getSocketedItemName(stack, "psimisc.none"))
            addToTooltip(tooltip, "psimisc.spellSelected", componentName)
        })
    }

    override fun isSocketSlotAvailable(stack: ItemStack, slot: Int): Boolean {
        return slot < 1
    }

    override fun showSlotInRadialMenu(stack: ItemStack, slot: Int): Boolean {
        return this.isSocketSlotAvailable(stack, slot - 1)
    }

    override fun getBulletInSocket(stack: ItemStack, slot: Int): ItemStack {
        val name = "bullet" + slot
        val cmp = ItemNBTHelper.getCompound(stack, name)
        return if (cmp == null) ItemStack.EMPTY else ItemStack(cmp)
    }

    override fun setBulletInSocket(stack: ItemStack, slot: Int, bullet: ItemStack) {
        val name = "bullet" + slot
        val cmp = NBTTagCompound()
        bullet.writeToNBT(cmp)

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
        if (!bullet.isEmpty && bullet.item is ISpellSettable) {
            (bullet.item as ISpellSettable).setSpell(player, bullet, spell)
            this.setBulletInSocket(stack, slot, bullet)
        }

    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        FlowColors.purgeColor(entityItem.entityItem)
        return super.onEntityItemUpdate(entityItem)
    }
}
