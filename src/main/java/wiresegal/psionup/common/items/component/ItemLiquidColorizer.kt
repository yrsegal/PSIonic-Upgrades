package wiresegal.psionup.common.items.component

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.local
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICADColorizer
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.items.base.ICadComponentAcceptor
import wiresegal.psionup.common.items.base.ItemComponent
import wiresegal.psionup.common.lib.LibMisc
import java.awt.Color

/**
 * @author WireSegal
 * Created at 8:44 AM on 3/20/16.
 */
class ItemLiquidColorizer(name: String) : ItemComponent(name), ICADColorizer, IItemColorProvider, ICadComponentAcceptor {

    companion object {
        fun getColorFromStack(p0: ItemStack): Int = ItemNBTHelper.getInt(p0, "color", Int.MAX_VALUE)

        fun getInheriting(stack: ItemStack): ItemStack {
            val nbt = ItemNBTHelper.getCompound(stack, "inheriting")
            return ItemStack(nbt ?: return ItemStack.EMPTY)
        }

        fun setInheriting(stack: ItemStack, inheriting: ItemStack): ItemStack {
            if (inheriting.isEmpty) {
                ItemNBTHelper.removeEntry(stack, "inheriting")
            } else {
                val nbt = NBTTagCompound()
                inheriting.writeToNBT(nbt)
                ItemNBTHelper.setCompound(stack, "inheriting", nbt)
            }
            return stack
        }
    }

    override fun acceptsPiece(stack: ItemStack, type: EnumCADComponent): Boolean = type == EnumCADComponent.DYE

    override fun setPiece(stack: ItemStack, type: EnumCADComponent, piece: ItemStack): ItemStack {
        if (type != EnumCADComponent.DYE)
            return stack
        return setInheriting(stack, piece)
    }

    override fun getPiece(stack: ItemStack, type: EnumCADComponent): ItemStack {
        if (type != EnumCADComponent.DYE)
            return ItemStack.EMPTY
        return getInheriting(stack)
    }

    override val itemColorFunction: ((stack: ItemStack, tintIndex: Int) -> Int)?
        get() = {
            stack, tintIndex ->
            if (tintIndex == 1) getColor(stack) else 0xFFFFFF
        }

    override fun getColor(p0: ItemStack): Int {
        var itemcolor = ItemNBTHelper.getInt(p0, "color", Int.MAX_VALUE)
        if (!p0.isEmpty) {
            val inheriting = getInheriting(p0)
            if (!inheriting.isEmpty && inheriting.item is ICADColorizer) {
                val inheritcolor = (inheriting.item as ICADColorizer).getColor(inheriting)
                if (itemcolor == Int.MAX_VALUE)
                    itemcolor = inheritcolor
                else {
                    val it = Color(itemcolor)
                    val inh = Color(inheritcolor)
                    itemcolor = Color((it.red + inh.red) / 2, (it.green + inh.green) / 2, (it.blue + inh.blue) / 2).rgb
                }
            }
        }
        return if (itemcolor == Int.MAX_VALUE) ICADColorizer.DEFAULT_SPELL_COLOR else itemcolor
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        super.addInformation(stack, playerIn, tooltip, advanced)

        if (GuiScreen.isShiftKeyDown()) {
            val inheriting = getInheriting(stack)
            if (!inheriting.isEmpty) {
                tooltip.add("${TextFormatting.GREEN}${local("${LibMisc.MOD_ID}.misc.color_inheritance")}${TextFormatting.GRAY}: ${inheriting.displayName}")
            }

            if (advanced) {
                val color = getColorFromStack(stack)
                var number = String.format("%06X", color)
                if (number.length > 6) number = number.substring(number.length - 6)
                if (color != Int.MAX_VALUE)
                    tooltip.add("${TextFormatting.GREEN}${local("${LibMisc.MOD_ID}.misc.color")}${TextFormatting.GRAY}: #$number")
            }
        }
    }

    override fun onItemRightClick(worldIn: World?, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>? {
        var outStack = playerIn.getHeldItem(hand)

        if (playerIn.isSneaking)
            outStack = ItemStack(ModItems.emptyColorizer)

        return ActionResult(EnumActionResult.SUCCESS, outStack)
    }
}
