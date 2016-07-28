package wiresegal.psionup.common.items.spell

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
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.core.helper.ItemNBTHelper
import vazkii.psi.common.item.ItemCAD
import vazkii.psi.common.item.base.ModItems
import vazkii.psi.common.item.tool.IPsimetalTool
import vazkii.psi.common.item.tool.ItemPsimetalTool
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.client.render.entity.GlowingItemHandler
import wiresegal.psionup.common.core.helper.FlowColors
import wiresegal.psionup.common.items.base.ItemModTool
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 10:23 PM on 7/11/16.
 */
open class ItemFlowTool(name: String, attackDamage: Float, speed: Float, effectiveBlocks: Set<Block>, type: String, val ebony: Boolean) : ItemModTool(name, PsiAPI.PSIMETAL_TOOL_MATERIAL, attackDamage, speed, type, effectiveBlocks), IPsimetalTool, ModelHandler.IItemColorProvider, FlowColors.IAcceptor, GlowingItemHandler.IOverlayable {

    init {
        addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "overlay")) {
            itemStack, world, entityLivingBase -> if (ItemNBTHelper.getBoolean(itemStack.copy(), GlowingItemHandler.IOverlayable.TAG_OVERLAY, false)) 1f else 0f
        }
    }

    override fun onBlockStartBreak(itemstack: ItemStack?, pos: BlockPos?, player: EntityPlayer?): Boolean {
        super.onBlockStartBreak(itemstack, pos, player)

        val data = PlayerDataHandler.get(player!!)
        val playerCad = PsiAPI.getPlayerCAD(player)

        if (playerCad != null) {
            val bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack))
            ItemCAD.cast(player.worldObj, player, data, bullet, playerCad, 5, 10, 0.05f) { context: SpellContext ->
                context.tool = itemstack
                context.positionBroken = ItemPsimetalTool.raytraceFromEntity(player.worldObj, player, false, 5.0)
            }
        }

        return false
    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
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

    class Axe(name: String, ebony: Boolean) : ItemFlowTool(name, 6F, -3.1F, EFFECTIVE_ON, "axe", ebony) {

        companion object {
            private val EFFECTIVE_ON = setOf(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER)
        }

        override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
            return if (state.material !== Material.WOOD && state.material !== Material.PLANTS && state.material !== Material.VINE) super.getStrVsBlock(stack, state) else efficiencyOnProperMaterial
        }
    }

    class Pickaxe(name: String, ebony: Boolean) : ItemFlowTool(name, 1.0f, -2.8f, EFFECTIVE_ON, "pickaxe", ebony) {

        companion object {
            private val EFFECTIVE_ON = setOf(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB)
        }

        override fun canHarvestBlock(state: IBlockState?): Boolean {
            val blockIn = state!!.block
            return if (blockIn === Blocks.OBSIDIAN) this.toolMaterial.harvestLevel == 3 else if (blockIn !== Blocks.DIAMOND_BLOCK && blockIn !== Blocks.DIAMOND_ORE) if (blockIn !== Blocks.EMERALD_ORE && blockIn !== Blocks.EMERALD_BLOCK) if (blockIn !== Blocks.GOLD_BLOCK && blockIn !== Blocks.GOLD_ORE) if (blockIn !== Blocks.IRON_BLOCK && blockIn !== Blocks.IRON_ORE) if (blockIn !== Blocks.LAPIS_BLOCK && blockIn !== Blocks.LAPIS_ORE) if (blockIn !== Blocks.REDSTONE_ORE && blockIn !== Blocks.LIT_REDSTONE_ORE) if (state.material === Material.ROCK) true else if (state.material === Material.IRON) true else state.material === Material.ANVIL else this.toolMaterial.harvestLevel >= 2 else this.toolMaterial.harvestLevel >= 1 else this.toolMaterial.harvestLevel >= 1 else this.toolMaterial.harvestLevel >= 2 else this.toolMaterial.harvestLevel >= 2 else this.toolMaterial.harvestLevel >= 2
        }

        override fun getStrVsBlock(stack: ItemStack, state: IBlockState): Float {
            return if (state.material !== Material.IRON && state.material !== Material.ANVIL && state.material !== Material.ROCK) super.getStrVsBlock(stack, state) else efficiencyOnProperMaterial
        }
    }

    class Shovel(name: String, ebony: Boolean) : ItemFlowTool(name, 1.5f, -3.0f, EFFECTIVE_ON, "shovel", ebony) {

        companion object {
            private val EFFECTIVE_ON = setOf(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND)
        }

        override fun canHarvestBlock(state: IBlockState?): Boolean {
            val blockIn = state!!.block
            return if (blockIn === Blocks.SNOW_LAYER) true else blockIn === Blocks.SNOW
        }

        override fun onItemUse(stack: ItemStack?, playerIn: EntityPlayer?, worldIn: World?, pos: BlockPos?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
            return Items.IRON_SHOVEL.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ)
        }
    }
}
