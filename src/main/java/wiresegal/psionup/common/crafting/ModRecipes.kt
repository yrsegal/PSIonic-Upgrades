package wiresegal.psionup.common.crafting

import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.common.lib.LibPieceNames
import wiresegal.psionup.api.PsionicAPI
import wiresegal.psionup.common.block.ModBlocks
import wiresegal.psionup.common.crafting.recipe.RecipeLiquidDye
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponent
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponentShapeless
import wiresegal.psionup.common.items.CompatItems
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.lib.LibMisc
import wiresegal.psionup.common.lib.LibNames
import vazkii.psi.common.block.base.ModBlocks as PsiBlocks
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
        RecipeSorter.register("${LibMisc.MOD_ID}:liquiddye", RecipeLiquidDye::class.java, RecipeSorter.Category.SHAPELESS, "")
        RecipeSorter.register("${LibMisc.MOD_ID}:cadcraft", RecipeCadComponent::class.java, RecipeSorter.Category.SHAPED, "")
        RecipeSorter.register("${LibMisc.MOD_ID}:cadcraftshapeless", RecipeCadComponentShapeless::class.java, RecipeSorter.Category.SHAPELESS, "")

        GameRegistry.addRecipe(RecipeLiquidDye())

        addOreDictRecipe(ItemStack(ModItems.liquidColorizer),
                " D ",
                "GBG",
                " I ",
                'I', "ingotIron",
                'G', "blockGlass",
                'B', ItemStack(Items.WATER_BUCKET),
                'D', "dustPsi")

        addShapelessCADRecipe(ItemStack(ModItems.liquidColorizer),
                EnumCADComponent.DYE, ItemStack(Items.WATER_BUCKET))

        addOreDictRecipe(ItemStack(ModItems.emptyColorizer),
                " D ",
                "G G",
                " I ",
                'I', "ingotIron",
                'G', "blockGlass",
                'D', "dustPsi")

        addShapelessOreDictRecipe(ItemStack(ModItems.liquidColorizer),
                ItemStack(Items.WATER_BUCKET), ItemStack(ModItems.emptyColorizer))

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

        addCADRecipe(ItemStack(ModItems.magazine),
                "MD",
                "MS",
                "MD",
                'M', "ingotPsi",
                'S', EnumCADComponent.SOCKET,
                'D', ItemStack(PsiItems.spellDrive))
        addCADRecipe(ItemStack(ModItems.magazine),
                "DM",
                "SM",
                "DM",
                'M', "ingotPsi",
                'S', EnumCADComponent.SOCKET,
                'D', ItemStack(PsiItems.spellDrive))

        for (i in 0..15) {
            addShapelessOreDictRecipe(ItemStack(ModBlocks.brightColoredPlate, 1, i), ItemStack(PsiBlocks.psiDecorative, 1, 3), "dustGlowstone", "dye" + LibNames.Colors[i])
            addShapelessOreDictRecipe(ItemStack(ModBlocks.darkColoredPlate, 1, i), ItemStack(PsiBlocks.psiDecorative, 1, 5), "dustGlowstone", "dye" + LibNames.Colors[i])
        }

        for (i in 0..15)
            addOreDictRecipe(ItemStack(ModBlocks.cadCase, 1, i),
                    " B ",
                    "GWG",
                    "MMM",
                    'B', ItemStack(Blocks.WOOL, 1, 15),
                    'G', "gemPsi",
                    'W', ItemStack(Blocks.WOOL, 1, i),
                    'M', "ingotPsi")


        PsionicAPI.addTrickRecipe("", ItemStack(Items.REDSTONE), ItemStack(PsiItems.material), ItemStack(PsiItems.cadAssembly))
        PsionicAPI.addTrickRecipe(LibPieceNames.TRICK_INFUSION, ItemStack(Items.GOLD_INGOT), ItemStack(PsiItems.material, 1, 1), ItemStack(PsiItems.cadAssembly))
        PsionicAPI.addTrickRecipe(LibPieceNames.TRICK_GREATER_INFUSION, ItemStack(Items.DIAMOND), ItemStack(PsiItems.material, 1, 2), ItemStack(PsiItems.cadAssembly, 1, 2))
        PsionicAPI.addTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, ItemStack(Items.COAL), ItemStack(PsiItems.material, 1, 5), ItemStack(PsiItems.cadAssembly, 1, 2))
        PsionicAPI.addTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, ItemStack(Items.QUARTZ), ItemStack(PsiItems.material, 1, 6), ItemStack(PsiItems.cadAssembly, 1, 2))

        if (Loader.isModLoaded("Botania"))
            CompatItems.initRecipes()
    }


    fun addOreDictRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(ShapedOreRecipe(output, *recipe))
    }

    fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(ShapelessOreRecipe(output, *recipe))
    }

    fun addCADRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(RecipeCadComponent(output, *recipe))
    }

    fun addShapelessCADRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(RecipeCadComponentShapeless(output, *recipe))
    }
}
