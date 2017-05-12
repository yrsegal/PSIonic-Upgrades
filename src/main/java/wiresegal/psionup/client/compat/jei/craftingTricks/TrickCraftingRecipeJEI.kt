package wiresegal.psionup.client.compat.jei.craftingTricks

import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IDrawableStatic
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.spell.SpellPiece
import wiresegal.psionup.api.TrickRecipe
import wiresegal.psionup.client.compat.jei.JEICompat
import java.util.*

class TrickCraftingRecipeJEI(private val recipe: TrickRecipe) : BlankRecipeWrapper() {

    companion object {
        val programmerHover: IDrawableStatic
                = JEICompat.helper.guiHelper.createDrawable(ResourceLocation("psi", "textures/gui/programmer.png"), 16, 184, 16, 16)
        val trickCoordX: Int
            get() = 57
        val trickCoordY: Int
            get() = 6
    }

    val icon: IDrawable?
    val clazz: Class<out SpellPiece>?
    val piece: SpellPiece?

    init {
        val location = PsiAPI.simpleSpellTextures[recipe.piece]
        clazz = PsiAPI.spellPieceRegistry.getObject(recipe.piece)

        icon = if (location != null) JEICompat.helper.guiHelper.createDrawable(location, 0, 0, 256, 256) else null
        piece = if (clazz != null) SpellPiece.create(clazz, null) else null
    }

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputs(ItemStack::class.java, listOf(recipe.input, recipe.cad))
        ingredients.setOutput(ItemStack::class.java, recipe.output)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        if (icon != null && clazz != null) {
            GlStateManager.pushMatrix()
            GlStateManager.scale(0.0625f, 0.0625f, 0.0625f)
            icon.draw(minecraft, trickCoordX * 16, trickCoordY * 16)
            GlStateManager.color(1f, 1f, 1f)
            GlStateManager.popMatrix()

            if (onTrick(mouseX, mouseY))
                programmerHover.draw(minecraft, trickCoordX, trickCoordY)
        }

    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): MutableList<String>? {
        if (onTrick(mouseX, mouseY) && piece != null) {
            val tooltip = ArrayList<String>()
            piece.getTooltip(tooltip)
            return tooltip
        }
        return null
    }

    fun onTrick(mouseX: Int, mouseY: Int) = (mouseX >= trickCoordX && mouseX <= trickCoordX + 16 && mouseY >= trickCoordY && mouseY <= trickCoordY + 16)
}
