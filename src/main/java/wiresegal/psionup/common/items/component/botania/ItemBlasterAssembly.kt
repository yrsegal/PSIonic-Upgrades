package wiresegal.psionup.common.items.component.botania

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ItemManaGun
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICADAssembly
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.common.item.base.IExtraVariantHolder
import vazkii.psi.common.item.base.ModItems
import wiresegal.psionup.api.enabling.IManaTrick
import wiresegal.psionup.api.enabling.ITrickEnablerComponent
import wiresegal.psionup.client.core.ModelHandler
import wiresegal.psionup.common.crafting.recipe.botania.RecipeBlasterCADClip
import wiresegal.psionup.common.crafting.recipe.botania.RecipeBlasterCADLens
import wiresegal.psionup.common.items.base.ItemComponent
import wiresegal.psionup.common.lib.LibMisc
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 7:05 PM on 3/31/16.
 */
class ItemBlasterAssembly(name: String) : ItemComponent(name, name), ICADAssembly, IExtraVariantHolder, ITrickEnablerComponent {

    init {
        RecipeSorter.register("${LibMisc.MOD_ID_SHORT}:blasterClip", RecipeBlasterCADClip::class.java, RecipeSorter.Category.SHAPELESS, "")
        GameRegistry.addRecipe(RecipeBlasterCADClip())
        RecipeSorter.register("${LibMisc.MOD_ID_SHORT}:blaster", RecipeBlasterCADLens::class.java, RecipeSorter.Category.SHAPELESS, "")
        GameRegistry.addRecipe(RecipeBlasterCADLens())

        BlasterEventHandler.register()

        ModItems.cad.addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "clip"), {
            stack, world, player ->
            if (ItemManaGun.hasClip(stack)) 1f else 0f
        })
    }

    override fun enablePiece(player: EntityPlayer, component: ItemStack, cad: ItemStack, context: SpellContext, spell: Spell, x: Int, y: Int): Boolean {
        if (spell.grid.gridData[x][y] is IManaTrick) {
            val drain = (spell.grid.gridData[x][y] as IManaTrick).manaDrain(context, spell, x, y)
            return ManaItemHandler.requestManaExact(cad, context.caster, drain, true)
        }
        return false
    }

    override fun registerStats() {
        this.addStat(EnumCADStat.EFFICIENCY, 0, 80)
        this.addStat(EnumCADStat.POTENCY, 0, 250)
    }

    override fun getComponentType(p0: ItemStack) = EnumCADComponent.ASSEMBLY

    override fun getCADModel(stack: ItemStack, cad: ItemStack): ModelResourceLocation? {
        return ModelHandler.resourceLocations[LibNames.Items.LIVINGWOOD_CAD_MODEL]
    }

    override fun getExtraVariants() = arrayOf(LibNames.Items.LIVINGWOOD_CAD_MODEL)

}
