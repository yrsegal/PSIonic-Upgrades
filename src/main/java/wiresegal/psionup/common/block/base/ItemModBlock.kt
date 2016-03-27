package wiresegal.psionup.common.block.base

import net.minecraft.block.Block
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry
import vazkii.psi.common.block.base.IPsiBlock
import vazkii.psi.common.item.base.IVariantHolder
import wiresegal.psionup.common.items.base.ItemMod
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 5:48 PM on 3/20/16.
 */
class ItemModBlock(block: Block) : ItemBlock(block), IVariantHolder {
    private val psiBlock: IPsiBlock

    init {
        this.psiBlock = block as IPsiBlock
        ItemMod.variantCache.add(this)
        if (this.variants.size > 1) {
            this.setHasSubtypes(true)
        }

    }

    override fun getMetadata(damage: Int): Int {
        return damage
    }

    override fun setUnlocalizedName(par1Str: String): ItemBlock {
        GameRegistry.registerItem(this, par1Str)
        return super.setUnlocalizedName(par1Str)
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack?): String {
        val dmg = par1ItemStack!!.itemDamage
        val variants = this.variants
        val name: String
        if (dmg >= variants.size) {
            name = this.psiBlock.bareName
        } else {
            name = variants[dmg]
        }

        return "tile.${LibMisc.MOD_ID_SHORT}:" + name
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        val variants = this.variants

        for (i in variants.indices) {
            subItems.add(ItemStack(itemIn, 1, i))
        }

    }

    override fun getVariants(): Array<String> {
        return this.psiBlock.variants
    }

    override fun getCustomMeshDefinition(): ItemMeshDefinition? {
        return this.psiBlock.customMeshDefinition
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return this.psiBlock.getBlockRarity(stack)
    }
}

