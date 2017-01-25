package wiresegal.psionup.common.items.spell

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.arl.util.ItemNBTHelper
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.ISpellSettable
import vazkii.psi.api.spell.Spell
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.item.ItemCAD
import vazkii.psi.common.item.base.ModItems
import vazkii.psi.common.item.tool.IPsimetalTool
import vazkii.psi.common.item.tool.ItemPsimetalTool
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.client.render.entity.GlowingItemHandler
import wiresegal.psionup.common.core.helper.FlowColors
import wiresegal.psionup.common.items.base.ItemModSword
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * *         Created at 10:42 PM on 7/11/16.
 */
class ItemFlowSword(name: String, val ebony: Boolean) : ItemModSword(name, PsiAPI.PSIMETAL_TOOL_MATERIAL), ISocketable, ISpellSettable, ModelHandler.IItemColorProvider, FlowColors.IAcceptor, GlowingItemHandler.IOverlayable {

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "overlay")) {
            itemStack, world, entityLivingBase -> if (ItemNBTHelper.getBoolean(itemStack.copy(), GlowingItemHandler.IOverlayable.TAG_OVERLAY, false)) 1f else 0f
        }
    }


    override fun isSocketSlotAvailable(stack: ItemStack, slot: Int): Boolean {
        return slot < 3
    }

    override fun showSlotInRadialMenu(stack: ItemStack, slot: Int): Boolean {
        return this.isSocketSlotAvailable(stack, slot - 1)
    }

    override fun getBulletInSocket(stack: ItemStack, slot: Int): ItemStack? {
        val name = "bullet" + slot
        val cmp = ItemNBTHelper.getCompound(stack, name, true)
        return if (cmp == null) null else ItemStack(cmp)
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

    override fun hitEntity(itemstack: ItemStack, target: EntityLivingBase?, attacker: EntityLivingBase): Boolean {
        super.hitEntity(itemstack, target, attacker)

        if (attacker is EntityPlayer) {

            val data = PlayerDataHandler.get(attacker)
            val playerCad = PsiAPI.getPlayerCAD(attacker)

            if (playerCad != null) {
                val bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack))
                ItemCAD.cast(attacker.world, attacker, data, bullet, playerCad, 5, 10, 0.05f) {
                    context ->
                    context.attackedEntity = target
                }
            }
        }

        return true
    }

    override fun onUpdate(stack: ItemStack?, worldIn: World?, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        ItemPsimetalTool.regen(stack, entityIn, isSelected)
    }

    override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        addToTooltip(tooltip, "psimisc.spellSelected", ISocketable.getSocketedItemName(stack, "psimisc.none"))
    }

    override fun getIsRepairable(par1ItemStack: ItemStack?, par2ItemStack: ItemStack): Boolean {
        return par2ItemStack.item === ModItems.material && par2ItemStack.itemDamage == (if (ebony) 4 else 5) || super.getIsRepairable(par1ItemStack, par2ItemStack)
    }

    override fun requiresSneakForSpellSet(stack: ItemStack): Boolean {
        return false
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor {
            stack, tintIndex ->
            if (tintIndex == 1) {
                val colorizer = FlowColors.getColor(stack)
                if (colorizer == null) 0 else Psi.proxy.getColorizerColor(colorizer).rgb
            } else
                16777215
        }
    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        FlowColors.purgeColor(entityItem.entityItem)
        return super.onEntityItemUpdate(entityItem)
    }

}
