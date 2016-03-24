package wiresegal.psionup.common.crafting

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import vazkii.psi.api.cad.EnumCADComponent
import wiresegal.psionup.common.items.ItemCADMagazine
import wiresegal.psionup.common.items.ModItems
import vazkii.psi.common.item.base.ModItems as PsiItems

/**
 * @author WireSegal
 * Created at 1:44 PM on 3/20/16.
 */
object ModRecipes {

    val examplesockets = arrayOf(// These are examples for JEI, but it will be subverted by the special recipe.
            ItemStack(ModItems.socket),
            ItemStack(PsiItems.cadSocket, 1, 0),
            ItemStack(PsiItems.cadSocket, 1, 1),
            ItemStack(PsiItems.cadSocket, 1, 2),
            ItemStack(PsiItems.cadSocket, 1, 3),
            ItemStack(PsiItems.cadSocket, 1, 4))

    init {
        RecipeSorter.register("psionup:liquiddye", RecipeLiquidDye::class.java, RecipeSorter.Category.SHAPELESS, "")
        RecipeSorter.register("psionup:cadcraft", RecipeCadComponent::class.java, RecipeSorter.Category.SHAPED, "after:minecraft:shaped")
        RecipeSorter.register("psionup:cadcraftshapeless", RecipeCadComponentShapeless::class.java, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless")

        GameRegistry.addRecipe(RecipeLiquidDye())

        addOreDictRecipe(ItemStack(ModItems.liquidColorizer),
                " D ",
                "GBG",
                " I ",
                'I', "ingotIron",
                'G', "blockGlass",
                'B', ItemStack(Items.water_bucket),
                'D', "dustPsi")

        addShapelessCADRecipe(ItemStack(ModItems.liquidColorizer),
                EnumCADComponent.DYE, ItemStack(Items.water_bucket))

        addOreDictRecipe(ItemStack(ModItems.emptyColorizer),
                " D ",
                "G G",
                " I ",
                'I', "ingotIron",
                'G', "blockGlass",
                'D', "dustPsi")

        addShapelessOreDictRecipe(ItemStack(ModItems.liquidColorizer),
                ItemStack(Items.water_bucket), ItemStack(ModItems.emptyColorizer))

        addOreDictRecipe(ItemStack(ModItems.socket),
                " GI",
                "DI ",
                "I  ",
                'I', "ingotIron",
                'D', "dustPsi",
                'G', "dustGlowstone")

        addOreDictRecipe(ItemStack(ModItems.fakeCAD),
                "MMG",
                "MS ",
                'M', "ingotPsi",
                'S', ItemStack(ModItems.socket),
                'G', "gemPsi")

        addCADRecipe(ItemCADMagazine.setSocket(ItemStack(ModItems.magazine), ItemStack(PsiItems.cadSocket)),
                "MD",
                "MS",
                "MD",
                'M', "ingotPsi",
                'S', EnumCADComponent.SOCKET,
                'D', ItemStack(PsiItems.spellDrive))
        addCADRecipe(ItemCADMagazine.setSocket(ItemStack(ModItems.magazine), ItemStack(PsiItems.cadSocket)),
                "DM",
                "SM",
                "DM",
                'M', "ingotPsi",
                'S', EnumCADComponent.SOCKET,
                'D', ItemStack(PsiItems.spellDrive))
    }

    private fun addOreDictRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(ShapedOreRecipe(output, *recipe))
    }

    private fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(ShapelessOreRecipe(output, *recipe))
    }

    private fun addCADRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(RecipeCadComponent(output, *recipe))
    }

    private fun addShapelessCADRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(RecipeCadComponentShapeless(output, *recipe))
    }
}
