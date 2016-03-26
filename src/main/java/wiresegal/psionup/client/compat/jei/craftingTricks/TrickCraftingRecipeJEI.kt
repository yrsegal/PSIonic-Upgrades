package wiresegal.psionup.client.compat.jei.craftingTricks

import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IDrawableStatic

import mezz.jei.api.recipe.BlankRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.translation.I18n
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.spell.SpellParam
import vazkii.psi.api.spell.SpellPiece
import vazkii.psi.common.item.base.ItemMod
import wiresegal.psionup.api.TrickRecipe
import wiresegal.psionup.client.compat.jei.JEICompat
import java.util.*

class TrickCraftingRecipeJEI
@SuppressWarnings("unchecked")
constructor(private val recipe: TrickRecipe) : BlankRecipeWrapper() {

    companion object {
        val programmerHover = JEICompat.helper.guiHelper.createDrawable(ResourceLocation("psi", "textures/gui/programmer.png"), 16, 184, 16, 16)
        val trickCoordX: Int
            get() = 57
        val trickCoordY: Int
            get() = 6
    }

    val icon: IDrawable?
    val clazz: Class<out SpellPiece>?

    init {
        val location = PsiAPI.simpleSpellTextures[recipe.piece]
        clazz = PsiAPI.spellPieceRegistry.getObject(recipe.piece)
        if (location != null)
            icon = JEICompat.helper.guiHelper.createDrawable(location, 0, 0, 256, 256)
        else
            icon = null
    }

    override fun getInputs(): List<Any> {
        return listOf(recipe.input, recipe.cad)
    }

    override fun getOutputs(): List<Any> {
        return listOf(recipe.output)
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
        if (onTrick(mouseX, mouseY) && clazz != null) {
            val tooltip = ArrayList<String>()
            tooltip.add(I18n.translateToLocal("psi.spellpiece.${recipe.piece}"))
            ItemMod.tooltipIfShift(tooltip, {
                tooltip.add(TextFormatting.GRAY.toString() + I18n.translateToLocal("psi.spellpiece.${recipe.piece}.desc"))
            })
            return tooltip
        }
        return null
    }

    fun onTrick(mouseX: Int, mouseY: Int) = (mouseX >= trickCoordX && mouseX <= trickCoordX+16 && mouseY >= trickCoordY && mouseY <= trickCoordY+16)
}
