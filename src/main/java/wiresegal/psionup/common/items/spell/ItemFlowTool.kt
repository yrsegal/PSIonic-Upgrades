package wiresegal.psionup.common.items.spell

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemModTool
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.client.renderer.block.model.IBakedModel
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.ISpellSettable
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.item.ItemCAD
import vazkii.psi.common.item.base.ModItems
import vazkii.psi.common.item.tool.ItemPsimetalTool
import wiresegal.psionup.common.core.helper.FlowColors
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 10:23 PM on 7/11/16.
 */
open class ItemFlowTool(name: String, type: String, val ebony: Boolean) : ItemModTool(name, PsiAPI.PSIMETAL_TOOL_MATERIAL, type), ISpellSettable, ISocketable, IItemColorProvider, FlowColors.IAcceptor, IGlowingItem {


    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel): IBakedModel? {
        return IGlowingItem.Helper.wrapperBake(model, false, 1)
    }

    override fun isSocketSlotAvailable(stack: ItemStack, slot: Int): Boolean {
        return slot < 3
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

    override fun onBlockStartBreak(itemstack: ItemStack, pos: BlockPos?, player: EntityPlayer?): Boolean {
        super.onBlockStartBreak(itemstack, pos, player)

        val data = PlayerDataHandler.get(player!!)
        val playerCad = PsiAPI.getPlayerCAD(player)

        if (playerCad != null) {
            val bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack))
            ItemCAD.cast(player.world, player, data, bullet, playerCad, 5, 10, 0.05f) { context: SpellContext ->
                context.tool = itemstack
                context.positionBroken = ItemPsimetalTool.raytraceFromEntity(player.world, player, false, 5.0)
            }
        }

        return false
    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        ItemPsimetalTool.regen(stack, entityIn, isSelected)
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        addToTooltip(tooltip, "psimisc.spellSelected", ISocketable.getSocketedItemName(stack, "psimisc.none"))
    }

    override fun getIsRepairable(par1ItemStack: ItemStack, par2ItemStack: ItemStack): Boolean {
        return par2ItemStack.item === ModItems.material && par2ItemStack.itemDamage == (if (ebony) 4 else 5) || super.getIsRepairable(par1ItemStack, par2ItemStack)
    }

    override fun requiresSneakForSpellSet(stack: ItemStack): Boolean {
        return false
    }

    override val itemColorFunction: ((stack: ItemStack, tintIndex: Int) -> Int)?
        get() = {
            stack, tintIndex ->
            if (tintIndex == 1) {
                val colorizer = FlowColors.getColor(stack)
                if (colorizer.isEmpty) 0 else Psi.proxy.getColorizerColor(colorizer).rgb
            } else
                0xFFFFFF
        }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        FlowColors.purgeColor(entityItem.entityItem)
        return super.onEntityItemUpdate(entityItem)
    }

    class Axe(name: String, ebony: Boolean) : ItemFlowTool(name, "axe", ebony) {

        override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
            return if (state.material !== Material.WOOD && state.material !== Material.PLANTS && state.material !== Material.VINE) super.getStrVsBlock(stack, state) else efficiencyOnProperMaterial
        }
    }

    class Pickaxe(name: String, ebony: Boolean) : ItemFlowTool(name, "pickaxe", ebony) {

        override fun canHarvestBlock(state: IBlockState?): Boolean {
            val blockIn = state!!.block
            return if (blockIn === Blocks.OBSIDIAN) this.toolMaterial.harvestLevel == 3 else if (blockIn !== Blocks.DIAMOND_BLOCK && blockIn !== Blocks.DIAMOND_ORE) if (blockIn !== Blocks.EMERALD_ORE && blockIn !== Blocks.EMERALD_BLOCK) if (blockIn !== Blocks.GOLD_BLOCK && blockIn !== Blocks.GOLD_ORE) if (blockIn !== Blocks.IRON_BLOCK && blockIn !== Blocks.IRON_ORE) if (blockIn !== Blocks.LAPIS_BLOCK && blockIn !== Blocks.LAPIS_ORE) if (blockIn !== Blocks.REDSTONE_ORE && blockIn !== Blocks.LIT_REDSTONE_ORE) if (state.material === Material.ROCK) true else if (state.material === Material.IRON) true else state.material === Material.ANVIL else this.toolMaterial.harvestLevel >= 2 else this.toolMaterial.harvestLevel >= 1 else this.toolMaterial.harvestLevel >= 1 else this.toolMaterial.harvestLevel >= 2 else this.toolMaterial.harvestLevel >= 2 else this.toolMaterial.harvestLevel >= 2
        }

        override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
            return if (state.material !== Material.IRON && state.material !== Material.ANVIL && state.material !== Material.ROCK) super.getStrVsBlock(stack, state) else efficiencyOnProperMaterial
        }
    }

    class Shovel(name: String, ebony: Boolean) : ItemFlowTool(name, "shovel", ebony) {

        override fun canHarvestBlock(state: IBlockState?): Boolean {
            val blockIn = state!!.block
            return if (blockIn === Blocks.SNOW_LAYER) true else blockIn === Blocks.SNOW
        }

        override fun onItemUse(playerIn: EntityPlayer?, worldIn: World?, pos: BlockPos?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
            return Items.IRON_SHOVEL.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ)
        }
    }
}
