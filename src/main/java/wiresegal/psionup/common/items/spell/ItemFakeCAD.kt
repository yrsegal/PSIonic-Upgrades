package wiresegal.psionup.common.items.spell

import net.minecraft.client.renderer.color.IItemColor
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
import wiresegal.psionup.client.core.handler.ModelHandler
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.ISpellSettable
import vazkii.psi.api.spell.Spell
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.core.handler.PsiSoundHandler
import vazkii.psi.common.core.helper.ItemNBTHelper
import vazkii.psi.common.item.ItemCAD
import vazkii.psi.common.item.base.ModItems
import wiresegal.psionup.common.items.base.ItemMod
import java.awt.Color
import vazkii.psi.common.item.base.ItemMod as PsiItem

/**
 * @author WireSegal
 * Created at 8:46 AM on 3/20/16.
 */
class ItemFakeCAD(name: String) : ItemMod(name), ISocketable, ISpellSettable, ModelHandler.IItemColorProvider {

    init {
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor() = IItemColor {
        stack, tintIndex ->
        colorCalc()
    }

    fun colorCalc(): Int {
        val time = ClientTickHandler.total
        val w = (Math.sin(time.toDouble() * 0.4) * 0.5 + 0.5).toFloat() * 0.1f
        val r = (Math.sin(time.toDouble() * 0.1) * 0.5 + 0.5).toFloat() * 0.5f + 0.25f + w
        val g = 0.5f + w
        val b = 1.0f
        return Color((r * 255.0f).toInt(), (g * 255.0f).toInt(), (b * 255.0f).toInt()).rgb
    }

    override fun onItemRightClick(itemstack: ItemStack, worldIn: World, player: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack>? {
        val data = PlayerDataHandler.get(player)
        val playerCad = PsiAPI.getPlayerCAD(player)
        if (playerCad != null) {
            val bullet = this.getBulletInSocket(itemstack, this.getSelectedSlot(itemstack))
            if (bullet == null) {
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
                ItemCAD.cast(player.worldObj, player, data, bullet, playerCad, 40, 25, 0.5f, null)
                return ActionResult(EnumActionResult.SUCCESS, itemstack)
            }
        }
        return super.onItemRightClick(itemstack, worldIn, player, hand)
    }

    override fun requiresSneakForSpellSet(p0: ItemStack?): Boolean {
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
        if (bullet != null && bullet.item is ISpellSettable) {
            (bullet.item as ISpellSettable).setSpell(player, bullet, spell)
            this.setBulletInSocket(stack, slot, bullet)
        }

    }
}
