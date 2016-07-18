package wiresegal.psionup.common.crafting

import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.PotionTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemPotion
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.potion.PotionType
import net.minecraft.potion.PotionUtils
import net.minecraftforge.common.brewing.BrewingRecipe
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.common.lib.LibPieceNames
import wiresegal.psionup.api.PsionicAPI
import wiresegal.psionup.common.block.ModBlocks
import wiresegal.psionup.common.core.ConfigHandler
import wiresegal.psionup.common.crafting.recipe.RecipeLiquidDye
import wiresegal.psionup.common.crafting.recipe.RecipeSocketTransferShapeless
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponent
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponentShapeless
import wiresegal.psionup.common.effect.ModPotions
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

    val POTION_ITEMS: Array<ItemPotion> = arrayOf(Items.POTIONITEM, Items.SPLASH_POTION, Items.LINGERING_POTION)

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
        RecipeSorter.register("${LibMisc.MOD_ID}:transfershapeless", RecipeSocketTransferShapeless::class.java, RecipeSorter.Category.SHAPELESS, "")

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

        if (ConfigHandler.enableInline)
            addOreDictRecipe(ItemStack(ModItems.fakeCAD),
                    "MMG",
                    "MS ",
                    'M', "ingotPsi",
                    'S', ItemStack(ModItems.socket),
                    'G', "gemPsi")

        if (ConfigHandler.enableMagazine) {
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
        }

        for (i in 0..15) {
            addShapelessOreDictRecipe(ItemStack(ModBlocks.brightColoredPlate, 1, i), ItemStack(PsiBlocks.psiDecorative, 1, 3), "dustGlowstone", "dye" + LibNames.Colors[i])
            addShapelessOreDictRecipe(ItemStack(ModBlocks.darkColoredPlate, 1, i), ItemStack(PsiBlocks.psiDecorative, 1, 5), "dustGlowstone", "dye" + LibNames.Colors[i])
        }

        if (ConfigHandler.enableCase)
            for (i in 0..15)
                addOreDictRecipe(ItemStack(ModBlocks.cadCase, 1, i),
                        " B ",
                        "GWG",
                        "MMM",
                        'B', ItemStack(Blocks.WOOL, 1, 15),
                        'G', "gemPsi",
                        'W', ItemStack(Blocks.WOOL, 1, i),
                        'M', "ingotPsi")

        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ebonyExosuitHead), ItemStack(PsiItems.psimetalExosuitHelmet), "dustGlowstone", "ingotEbonyPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ebonyExosuitChest), ItemStack(PsiItems.psimetalExosuitChestplate), "dustGlowstone", "ingotEbonyPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ebonyExosuitLegs), ItemStack(PsiItems.psimetalExosuitLeggings), "dustGlowstone", "ingotEbonyPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ebonyExosuitBoots), ItemStack(PsiItems.psimetalExosuitBoots), "dustGlowstone", "ingotEbonyPsi"))

        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ivoryExosuitHead), ItemStack(PsiItems.psimetalExosuitHelmet), "dustGlowstone", "ingotIvoryPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ivoryExosuitChest), ItemStack(PsiItems.psimetalExosuitChestplate), "dustGlowstone", "ingotIvoryPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ivoryExosuitLegs), ItemStack(PsiItems.psimetalExosuitLeggings), "dustGlowstone", "ingotIvoryPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ivoryExosuitBoots), ItemStack(PsiItems.psimetalExosuitBoots), "dustGlowstone", "ingotIvoryPsi"))

        if (ConfigHandler.enableRing)
            addOreDictRecipe(ItemStack(ModItems.flashRing),
                    " E ",
                    "EGE",
                    " P ",
                    'E', "ingotEbonyPsi",
                    'G', "dustGlowstone",
                    'P', "gemPsi")


        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ebonyPickaxe), ItemStack(PsiItems.psimetalPickaxe), "dustGlowstone", "ingotEbonyPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ebonyShovel), ItemStack(PsiItems.psimetalShovel), "dustGlowstone", "ingotEbonyPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ebonyAxe), ItemStack(PsiItems.psimetalAxe), "dustGlowstone", "ingotEbonyPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ebonySword), ItemStack(PsiItems.psimetalSword), "dustGlowstone", "ingotEbonyPsi"))

        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ivoryPickaxe), ItemStack(PsiItems.psimetalPickaxe), "dustGlowstone", "ingotIvoryPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ivoryShovel), ItemStack(PsiItems.psimetalShovel), "dustGlowstone", "ingotIvoryPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ivoryAxe), ItemStack(PsiItems.psimetalAxe), "dustGlowstone", "ingotIvoryPsi"))
        GameRegistry.addRecipe(RecipeSocketTransferShapeless(ItemStack(ModItems.ivorySword), ItemStack(PsiItems.psimetalSword), "dustGlowstone", "ingotIvoryPsi"))

        addOreDictRecipe(ItemStack(ModItems.bioticSensor),
                " P ",
                "PEI",
                " I ",
                'P', "ingotPsi",
                'E', Items.ENDER_EYE,
                'I', "ingotIron")

        val hasCopper = OreDictionary.doesOreNameExist("ingotCopper")
        addOreDictRecipe(ItemStack(ModItems.gaussRifle),
                "C  ",
                "IIP",
                "C I",
                'C', if (hasCopper) "ingotCopper" else "dustRedstone",
                'I', "ingotIron",
                'P', "dustPsi")
        addOreDictRecipe(ItemStack(ModItems.gaussBullet, 2),
                "ICP",
                'I', "ingotIron",
                'C', if (hasCopper) "ingotCopper" else "dustRedstone",
                'P', "dustPsi")

        addCompletePotionRecipes("dustPsi", ModPotions.psishockType, ModPotions.longPsishockType, ModPotions.strongPsishockType)

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

    fun addPotionRecipe(inputItem: Item, inputMaterial: String, inputType: PotionType, outputItem: Item, outputType: PotionType) {
        BrewingRecipeRegistry.addRecipe(
                PotionUtils.addPotionToItemStack(ItemStack(inputItem), inputType),
                inputMaterial,
                PotionUtils.addPotionToItemStack(ItemStack(outputItem), outputType))
    }

    fun addPotionRecipe(inputItem: Item, inputMaterial: ItemStack, inputType: PotionType, outputItem: Item, outputType: PotionType) {
        BrewingRecipeRegistry.addRecipe(
                PotionUtils.addPotionToItemStack(ItemStack(inputItem), inputType),
                inputMaterial,
                PotionUtils.addPotionToItemStack(ItemStack(outputItem), outputType))
    }

    fun addCompletePotionRecipes(inputMaterial: String, typeNormal: PotionType, typeLong: PotionType?, typeStrong: PotionType?) {
        for (item in POTION_ITEMS) {
            addPotionRecipe(item, inputMaterial, PotionTypes.WATER, item, PotionTypes.MUNDANE)

            addPotionRecipe(item, inputMaterial, PotionTypes.AWKWARD, item, ModPotions.psishockType)
            if (typeStrong != null) addPotionRecipe(item, ItemStack(Items.GLOWSTONE_DUST), typeNormal, item, typeStrong)
            if (typeLong != null)  addPotionRecipe(item, ItemStack(Items.REDSTONE), typeNormal, item, typeLong)
        }

        val iteratorTypes = mutableListOf(typeNormal)
        if (typeLong != null) iteratorTypes.add(typeLong)
        if (typeStrong != null) iteratorTypes.add(typeStrong)

        for (type in iteratorTypes) {
            addPotionRecipe(Items.POTIONITEM, ItemStack(Items.GUNPOWDER), type, Items.SPLASH_POTION, type)
            addPotionRecipe(Items.SPLASH_POTION, ItemStack(Items.DRAGON_BREATH), type, Items.LINGERING_POTION, type)
        }
    }
}
