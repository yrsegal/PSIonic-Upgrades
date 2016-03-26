package wiresegal.psionup.common.items.base

import net.minecraft.item.ItemStack
import vazkii.psi.common.item.base.IVariantHolder
import wiresegal.psionup.common.core.CreativeTab
import wiresegal.psionup.common.lib.LibMisc
import java.util.*
import vazkii.psi.common.item.base.ItemMod as PsiItem

/**
 * @author WireSegal
 * Created at 8:50 AM on 3/20/16.
 */
open class ItemMod(name: String, vararg variants: String) : vazkii.psi.common.item.base.ItemMod(name, *variants) {

    companion object {
        val variantCache = ArrayList<IVariantHolder>()
    }

    init {
        variantCache.add(this)
        creativeTab = CreativeTab.INSTANCE
    }

    override fun getUnlocalizedName(par1ItemStack: ItemStack): String {
        return super.getUnlocalizedName(par1ItemStack).replace("psi", "${LibMisc.MOD_ID_SHORT}")
    }
}

