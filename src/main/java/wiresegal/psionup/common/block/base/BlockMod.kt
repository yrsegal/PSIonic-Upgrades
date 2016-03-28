package wiresegal.psionup.common.block.base

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.registry.GameRegistry
import vazkii.psi.common.block.base.IPsiBlock

/**
 * @author WireSegal
 * Created at 5:45 PM on 3/20/16.
 */
open class BlockMod(name: String, materialIn: Material, vararg variants: String) : Block(materialIn), IPsiBlock {
    private var variants: Array<out String>
    private val bareName: String

    init {
        this.variants = variants
        if (variants.size == 0) {
            this.variants = arrayOf(name)
        }
        this.bareName = name
        this.variants = variants
        this.unlocalizedName = name
    }

    override fun setUnlocalizedName(name: String): Block {
        super.setUnlocalizedName(name)
        this.registryName = name
        GameRegistry.registerBlock(this, ItemModBlock::class.java)
        return this
    }

    override fun getBareName(): String {
        return this.bareName
    }

    override fun getVariants(): Array<out String> {
        return this.variants
    }

    override fun getCustomMeshDefinition(): ItemMeshDefinition? {
        return null
    }

    override fun getBlockRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.COMMON
    }

    override fun getIgnoredProperties(): Array<IProperty<*>> {
        return arrayOf()
    }

    override fun getVariantEnum(): Class<Any>? {
        return null
    }
}
