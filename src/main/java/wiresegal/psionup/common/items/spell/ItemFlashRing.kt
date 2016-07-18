package wiresegal.psionup.common.items.spell

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.spell.ISpellContainer
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.item.ItemCAD
import vazkii.psi.common.item.ItemSpellDrive
import wiresegal.psionup.client.core.handler.GuiHandler
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.items.base.ItemMod
import wiresegal.psionup.common.lib.LibMisc
import vazkii.psi.common.item.base.ItemMod as PsiItem
import vazkii.psi.common.item.base.ModItems as PsiItems

/**
 * @author WireSegal
 * Created at 8:43 AM on 3/20/16.
 */
class ItemFlashRing(name: String) : ItemMod(name), ISpellContainer {

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "active")) {
            stack, world, entity ->
            if (containsSpell(stack)) 1f else 0f
        }
        setMaxStackSize(1)
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack> {
        if (!playerIn.isSneaking && containsSpell(itemStackIn)) {
            val playerCAD = PsiAPI.getPlayerCAD(playerIn)
            var did = false
            if (playerCAD != null)
                did = ItemCAD.cast(worldIn, playerIn, PlayerDataHandler.get(playerIn), itemStackIn, playerCAD, (15 * getCostModifier(itemStackIn)).toInt(), 25, 0.5f, null)
            return ActionResult(if (did) EnumActionResult.SUCCESS else EnumActionResult.PASS, itemStackIn)
        } else if (playerIn.isSneaking) {
            if (worldIn.isRemote)
                playerIn.openGui(PsionicUpgrades.INSTANCE, GuiHandler.GUI_FLASHRING, worldIn, 0, 0, 0)
            return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
        }
        return ActionResult(EnumActionResult.PASS, itemStackIn)
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        addToTooltip(tooltip, "psimisc.bulletCost", (this.getCostModifier(stack) * 100.0).toInt())
    }

    override fun getItemStackDisplayName(stack: ItemStack): String {
        if (!this.containsSpell(stack)) {
            return super.getItemStackDisplayName(stack)
        } else {
            val spell = getSpell(stack)
            return if (spell != null && !spell.name.isEmpty()) spell.name else super.getItemStackDisplayName(stack)
        }
    }

    override fun castSpell(p0: ItemStack, p1: SpellContext) {
        p1.cspell.safeExecute(p1)
    }

    override fun containsSpell(p0: ItemStack): Boolean {
        val spell = getSpell(p0)
        return spell != null && !spell.grid.isEmpty
    }

    override fun getCostModifier(p0: ItemStack): Double {
        return 5.0
    }

    override fun getSpell(p0: ItemStack): Spell? {
        return ItemSpellDrive.getSpell(p0)
    }

    override fun isCADOnlyContainer(p0: ItemStack): Boolean {
        return false
    }

    override fun setSpell(p0: EntityPlayer, p1: ItemStack, p2: Spell?) {
        return ItemSpellDrive.setSpell(p1, p2)
    }

    override fun requiresSneakForSpellSet(p0: ItemStack): Boolean {
        return true // Because the Flash Ring has shift-click behavior, this will never fire.
    }

    override fun hasContainerItem(stack: ItemStack?): Boolean {
        return true
    }

    override fun getContainerItem(itemStack: ItemStack): ItemStack {
        return itemStack.copy()
    }
}
