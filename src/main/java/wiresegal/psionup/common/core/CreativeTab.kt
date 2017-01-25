package wiresegal.psionup.common.core

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.lib.LibMisc
import java.util.*

/**
 * @author WireSegal
 * Created at 1:17 PM on 3/20/16.
 */
class CreativeTab : CreativeTabs(LibMisc.MOD_ID) {
    internal lateinit var list: MutableList<ItemStack>

    init {
        this.setNoTitle()
        this.backgroundImageName = "${LibMisc.MOD_ID}.png"
    }

    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModItems.liquidColorizer)
    }

    override fun getTabIconItem(): ItemStack {
        return this.iconItemStack
    }

    override fun hasSearchBar(): Boolean {
        return true
    }

    override fun displayAllRelevantItems(initialList: NonNullList<ItemStack>) {
        this.list = initialList
        for (item in items)
            addItem(item)
    }

    private fun addItem(item: Item) {
        val tempList = NonNullList.create<ItemStack>()
        item.getSubItems(item, this, tempList)
        if (item == tabIconItem.item)
            this.list.addAll(0, tempList)
        else
            this.list.addAll(tempList)
    }

    companion object {
        var INSTANCE = CreativeTab()

        private val items = ArrayList<Item>()

        fun set(block: Block) {
            val item = Item.getItemFromBlock(block) ?: return
            items.add(item)
            block.setCreativeTab(INSTANCE)
        }

        fun set(item: Item) {
            items.add(item)
            item.creativeTab = INSTANCE
        }
    }
}

