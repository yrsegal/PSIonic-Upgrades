package wiresegal.psionup.common.items.base

import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import vazkii.psi.api.internal.TooltipHelper
import vazkii.psi.common.core.PsiCreativeTab
import vazkii.psi.common.item.base.IVariantHolder
import wiresegal.psionup.common.core.CreativeTab
import wiresegal.psionup.common.lib.LibMisc
import java.util.*
import vazkii.psi.common.item.base.ItemMod as PsiItem

/**
 * @author WireSegal
 * Created at 8:50 AM on 3/20/16.
 */
open class ItemMod(name: String, vararg variants: String) : Item(), IVariantHolder {

    companion object {
        val variantCache = ArrayList<IVariantHolder>()

        fun tooltipIfShift(tooltip: List<String>, r: () -> Unit) {
            TooltipHelper.tooltipIfShift(tooltip, r)
        }

        fun addToTooltip(tooltip: List<String>, s: String, vararg format: Any) {
            TooltipHelper.addToTooltip(tooltip, s, *format)
        }

        fun local(s: String): String {
            return TooltipHelper.local(s)
        }
    }

    private val variants: Array<out String>
    private val bareName: String


    init {
        var variantTemp = variants
        this.unlocalizedName = name
        this.creativeTab = PsiCreativeTab.INSTANCE
        if (variantTemp.size > 1) {
            this.setHasSubtypes(true)
        }

        if (variantTemp.size == 0) {
            variantTemp = arrayOf(name)
        }

        this.bareName = name
        this.variants = variantTemp
        variantCache.add(this)
        PsiItem.variantHolders.add(this)
        creativeTab = CreativeTab.INSTANCE
    }
    override fun setUnlocalizedName(name: String): Item {
        super.setUnlocalizedName(name)
        GameRegistry.register(this, ResourceLocation(LibMisc.MOD_ID_SHORT, name))
        return this
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack?): String {
        val dmg = par1ItemStack!!.itemDamage
        val variants = this.getVariants()
        val name: String
        if (dmg >= variants.size) {
            name = this.bareName
        } else {
            name = variants[dmg]
        }

        return "item.psionup:" + name
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for (i in 0..this.getVariants().size - 1) {
            subItems.add(ItemStack(itemIn, 1, i))
        }

    }

    override fun getVariants(): Array<out String> {
        return this.variants
    }

    override fun getCustomMeshDefinition(): ItemMeshDefinition? {
        return null
    }
}

