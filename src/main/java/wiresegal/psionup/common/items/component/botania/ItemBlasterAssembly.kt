package wiresegal.psionup.common.items.component.botania

import com.teamwizardry.librarianlib.core.client.ModelHandler
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ItemManaGun
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.spell.Spell
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.common.item.base.ModItems
import wiresegal.psionup.api.enabling.ITrickEnablerComponent
import wiresegal.psionup.api.enabling.botania.EnumManaTier
import wiresegal.psionup.api.enabling.botania.IBlasterComponent
import wiresegal.psionup.api.enabling.botania.IManaTrick
import wiresegal.psionup.common.crafting.recipe.botania.RecipeBlasterCADClip
import wiresegal.psionup.common.crafting.recipe.botania.RecipeBlasterCADLens
import wiresegal.psionup.common.items.base.ItemComponent
import wiresegal.psionup.common.lib.LibMisc
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 7:05 PM on 3/31/16.
 */
class ItemBlasterAssembly(name: String) : ItemComponent(name), IExtraVariantHolder, IBlasterComponent {

    init {
        RecipeSorter.register("${LibMisc.MOD_ID}:blasterClip", RecipeBlasterCADClip::class.java, RecipeSorter.Category.SHAPELESS, "")
        GameRegistry.addRecipe(RecipeBlasterCADClip())
        RecipeSorter.register("${LibMisc.MOD_ID}:blaster", RecipeBlasterCADLens::class.java, RecipeSorter.Category.SHAPELESS, "")
        GameRegistry.addRecipe(RecipeBlasterCADLens())

        BlasterEventHandler.register()

        ModItems.cad.addPropertyOverride(ResourceLocation(LibMisc.MOD_ID, "clip")) {
            stack, _, _ ->
            if (ItemManaGun.hasClip(stack)) 1f else 0f
        }
    }

    override fun enablePiece(player: EntityPlayer, component: ItemStack, cad: ItemStack, context: SpellContext, spell: Spell, x: Int, y: Int): ITrickEnablerComponent.EnableResult {

        val isElven = ItemManaGun.hasClip(cad)
        val tier = if (isElven) EnumManaTier.ALFHEIM else EnumManaTier.BASE

        val spellpiece = spell.grid.gridData[x][y]
        if (spellpiece is IManaTrick && EnumManaTier.allowed(tier, spellpiece.tier())) {
            val drain = spellpiece.manaDrain(context, x, y)
            return ITrickEnablerComponent.EnableResult.fromBoolean(ManaItemHandler.requestManaExact(cad, context.caster, drain, true))
        }
        return ITrickEnablerComponent.EnableResult.NOT_ENABLED
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        if (GuiScreen.isShiftKeyDown())
            addToTooltip(tooltip, "${LibMisc.MOD_ID}.requirement.mana_cad")
        super.addInformation(stack, playerIn, tooltip, advanced)
    }

    override fun registerStats() {
        this.addStat(EnumCADStat.EFFICIENCY, 0, 80)
        this.addStat(EnumCADStat.POTENCY, 0, 250)
    }

    override fun getCADModel(stack: ItemStack, cad: ItemStack): ModelResourceLocation? {
        return ModelHandler.resourceLocations["psionup"]?.get(LibNames.Items.LIVINGWOOD_CAD_MODEL) as? ModelResourceLocation?
    }

    override val extraVariants: Array<out String>
        get() = arrayOf(LibNames.Items.LIVINGWOOD_CAD_MODEL)

}
