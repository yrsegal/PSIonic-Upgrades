package wiresegal.psionup.client.core

import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelBakery
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.GameData
import vazkii.psi.common.block.base.IPsiBlock
import vazkii.psi.common.item.base.IExtraVariantHolder
import vazkii.psi.common.item.base.IVariantHolder
import wiresegal.psionup.common.items.base.ItemMod
import java.util.*

/**
 * @author WireSegal
 * Created at 2:12 PM on 3/20/16.
 */
object ModelHandler {


    val resourceLocations = HashMap<String, ModelResourceLocation>()

    fun preInit() {
        for (holder in ItemMod.variantCache) {
            registerModels(holder)
        }
    }

    // The following is a blatant copy of Psi's ModelHandler.

    fun registerModels(holder: IVariantHolder) {
        val def = holder.customMeshDefinition
        if (def != null) {
            ModelLoader.setCustomMeshDefinition(holder as Item, def)
        } else {
            val i = holder as Item
            registerModels(i, holder.variants, false)
            if (holder is IExtraVariantHolder) {
                registerModels(i, holder.extraVariants, true)
            }
        }

    }

    fun registerModels(item: Item, variants: Array<String>, extra: Boolean) {
        if (item is ItemBlock && item.getBlock() is IPsiBlock) {
            val i = item.getBlock() as IPsiBlock
            val name = i.variantEnum
            val loc = i.ignoredProperties
            if (loc != null && loc.size > 0) {
                val builder = StateMap.Builder()
                val var7 = loc
                val var8 = loc.size

                for (var9 in 0..var8 - 1) {
                    val p = var7[var9]
                    builder.ignore(p)
                }

                ModelLoader.setCustomStateMapper(i as Block, builder.build())
            }

            if (name != null) {
                registerVariantsDefaulted(item, i as Block, name, "variant")
                return
            }
        }

        for (var11 in variants.indices) {
            val var12 = "psionicupgrades:" + variants[var11]
            val var13 = ModelResourceLocation(var12, "inventory")
            if (!extra) {
                ModelLoader.setCustomModelResourceLocation(item, var11, var13)
                resourceLocations.put(getKey(item, var11), var13)
            } else {
                ModelBakery.registerItemVariants(item, *arrayOf<ResourceLocation>(var13))
                resourceLocations.put(variants[var11], var13)
            }
        }

    }

    private fun registerVariantsDefaulted(item: Item, b: Block, enumclazz: Class<*>, variantHeader: String) {
        val baseName = GameData.getBlockRegistry().getNameForObject(b).toString()

        if (enumclazz.enumConstants != null)
            for (e in enumclazz.enumConstants) {
                if (e is IStringSerializable && e is Enum<*>) {
                    val variantName = variantHeader + "=" + e.name
                    val loc = ModelResourceLocation(baseName, variantName)
                    val i = e.ordinal
                    ModelLoader.setCustomModelResourceLocation(item, i, loc)
                    resourceLocations.put(getKey(item, i), loc)
                }
            }

    }

    fun getModelLocation(item: Item, meta: Int): ModelResourceLocation? {
        val key = getKey(item, meta)
        return if (resourceLocations.containsKey(key)) resourceLocations[key] else null
    }

    private fun getKey(item: Item, meta: Int): String {
        return "i_" + item.registryName + "@" + meta
    }
}