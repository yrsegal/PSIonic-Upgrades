package wiresegal.psionup.common.crafting

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import wiresegal.psionup.common.items.ItemCADMagazine
import vazkii.psi.common.item.base.ModItems as PsiItems
import wiresegal.psionup.common.items.ModItems

/**
 * @author WireSegal
 * Created at 1:44 PM on 3/20/16.
 */
object ModRecipes {
    init {
        addOreDictRecipe(ItemStack(ModItems.liquidColorizer),
                " D ",
                "GBG",
                " I ",
                'I', "ingotIron",
                'G', "blockGlass",
                'B', ItemStack(Items.water_bucket),
                'D', "dustPsi")

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



        val sockets = arrayOf( // These are examples for JEI, but it will be subverted by the special recipe.
                ItemStack(ModItems.socket),
                ItemStack(PsiItems.cadSocket, 1, 0),
                ItemStack(PsiItems.cadSocket, 1, 1),
                ItemStack(PsiItems.cadSocket, 1, 2),
                ItemStack(PsiItems.cadSocket, 1, 3),
                ItemStack(PsiItems.cadSocket, 1, 4))

        CraftingManager.getInstance().recipeList.add(RecipeCadComponent(ItemStack(ModItems.magazine),
                "MD",
                "MS",
                "MD",
                'M', "ingotPsi",
                'S', ItemStack(ModItems.socket),
                'D', ItemStack(PsiItems.spellDrive)))

        for (socket in sockets)
            addOreDictRecipe(ItemCADMagazine.setSocket(ItemStack(ModItems.magazine), socket),
                    "MD",
                    "MS",
                    "MD",
                    'M', "ingotPsi",
                    'S', socket,
                    'D', ItemStack(PsiItems.spellDrive))
    }

    private fun addOreDictRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(ShapedOreRecipe(output, *recipe))
    }

    private fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any) {
        CraftingManager.getInstance().recipeList.add(ShapelessOreRecipe(output, *recipe))
    }
}