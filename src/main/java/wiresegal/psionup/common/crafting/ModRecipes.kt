package wiresegal.psionup.common.crafting

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.common.lib.LibPieceNames
import wiresegal.psionup.api.PsionicAPI
import wiresegal.psionup.common.crafting.recipe.RecipeLiquidDye
import wiresegal.psionup.common.crafting.recipe.botania.CompatRecipes
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponent
import wiresegal.psionup.common.crafting.recipe.cad.RecipeCadComponentShapeless
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.items.CompatItems
import wiresegal.psionup.common.lib.LibMisc
import java.util.*
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

    fun pre() {
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

        PsionicAPI.addTrickRecipe("", ItemStack(Items.redstone), ItemStack(PsiItems.material), ItemStack(PsiItems.cadAssembly))
        PsionicAPI.addTrickRecipe(LibPieceNames.TRICK_INFUSION, ItemStack(Items.gold_ingot), ItemStack(PsiItems.material, 1, 1), ItemStack(PsiItems.cadAssembly))
        PsionicAPI.addTrickRecipe(LibPieceNames.TRICK_GREATER_INFUSION, ItemStack(Items.diamond), ItemStack(PsiItems.material, 1, 2), ItemStack(PsiItems.cadAssembly, 1, 2))
        PsionicAPI.addTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, ItemStack(Items.coal), ItemStack(PsiItems.material, 1, 5), ItemStack(PsiItems.cadAssembly, 1, 2))
        PsionicAPI.addTrickRecipe(LibPieceNames.TRICK_EBONY_IVORY, ItemStack(Items.quartz), ItemStack(PsiItems.material, 1, 6), ItemStack(PsiItems.cadAssembly, 1, 2))

    }

    fun init() {
        if (Loader.isModLoaded("Botania"))
            CompatItems.initRecipes()
    }

    fun post() {
        
    }


    fun addOreDictRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(object : ShapedOreRecipe(output, *recipe) {
            override fun toString(): String {
                return recipeToString(this)
            }
        })
    }

    fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(object : ShapelessOreRecipe(output, *recipe) {
            override fun toString(): String {
                return recipeToString(this)
            }
        })
    }

    fun addCADRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(object : RecipeCadComponent(output, *recipe) {
            override fun toString(): String {
                return recipeToString(this)
            }
        })
    }

    fun addShapelessCADRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(object : RecipeCadComponentShapeless(output, *recipe) {
            override fun toString(): String {
                return recipeToString(this)
            }
        })
    }


    private fun recipeToString(recipe: IRecipe): String {
        if (recipe is ShapedOreRecipe)
            return "${joinArr(recipe.input)} -> ${recipe.recipeOutput}"
        if (recipe is ShapelessOreRecipe)
            return "${joinArr(recipe.input)} -> ${recipe.recipeOutput}"
        return recipe.toString()
    }

    private fun joinArr(arr: Array<*>): String {
        var out = "["
        for (item in arr) {
            out += if (out.equals("[")) "" else ", " + item.toString()
        }
        return out + "]"
    }
    private fun joinArr(arr: List<*>): String {
        var out = "["
        for (item in arr) {
            out += if (out.equals("[")) "" else ", " + item.toString()
        }
        return out + "]"
    }
}
