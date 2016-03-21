package wiresegal.psionup.common.items

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.translation.I18n
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.RecipeSorter
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.common.core.helper.ItemNBTHelper
import vazkii.psi.common.item.base.IColorProvider
import vazkii.psi.common.item.component.ItemCADComponent
import wiresegal.psionup.common.core.CreativeTab
import wiresegal.psionup.common.crafting.RecipeLiquidDye

/**
 * @author WireSegal
 * Created at 8:44 AM on 3/20/16.
 */
class ItemLiquidColorizer(name: String) : ItemCADComponent(name, name), ICADColorizer, IColorProvider {

    companion object {
        fun getColorFromStack(p0: ItemStack): Int = ItemNBTHelper.getInt(p0, "color", -1)
    }

    init {
        GameRegistry.addRecipe(RecipeLiquidDye())
        RecipeSorter.register("psionup:liquiddye", RecipeLiquidDye::class.java, RecipeSorter.Category.SHAPELESS, "")
        creativeTab = CreativeTab.INSTANCE
        ItemMod.variantCache.add(this)
    }

    override fun getColor() = IItemColor {
        stack, tintIndex ->
        if (tintIndex == 1) getColor(stack) else 0xFFFFFF
    }

    override fun getColor(p0: ItemStack?): Int = ItemNBTHelper.getInt(p0, "color", ICADColorizer.DEFAULT_SPELL_COLOR)

    override fun getUnlocalizedName(par1ItemStack: ItemStack): String {
        return super.getUnlocalizedName(par1ItemStack).replace("psi", "psionup")
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        super.addInformation(stack, playerIn, tooltip, advanced)
        if (advanced && GuiScreen.isShiftKeyDown()) {
            val color = getColorFromStack(stack)
            var number = String.format("%06X", color)
            if (number.length > 6) number = number.substring(number.length - 6)
            if (color != -1)
                tooltip.add(TextFormatting.GREEN.toString() + I18n.translateToLocal("psionup.misc.color") + "${TextFormatting.GRAY} #" + number)
        }
    }

    override fun onItemRightClick(itemStackIn: ItemStack?, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack>? {
        var outStack = itemStackIn

        if (playerIn != null && playerIn.isSneaking)
            outStack = ItemStack(ModItems.emptyColorizer)

        return super.onItemRightClick(outStack, worldIn, playerIn, hand)
    }
}
