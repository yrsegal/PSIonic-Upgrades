package wiresegal.psionup.common.items.component

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.cad.EnumCADComponent
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.common.core.helper.ItemNBTHelper
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.items.base.ICadComponentAcceptor
import wiresegal.psionup.common.items.base.ItemComponent
import wiresegal.psionup.common.lib.LibMisc
import java.awt.Color

/**
 * @author WireSegal
 * Created at 8:44 AM on 3/20/16.
 */
class ItemLiquidColorizer(name: String) : ItemComponent(name), ICADColorizer, ModelHandler.IItemColorProvider, ICadComponentAcceptor {

    companion object {
        fun getColorFromStack(p0: ItemStack): Int = ItemNBTHelper.getInt(p0, "color", Int.MAX_VALUE)

        fun getInheriting(stack: ItemStack): ItemStack? {
            val nbt = ItemNBTHelper.getCompound(stack, "inheriting", true)
            return ItemStack.loadItemStackFromNBT(nbt ?: return null)
        }

        fun setInheriting(stack: ItemStack, inheriting: ItemStack?): ItemStack {
            if (inheriting == null) {
                ItemNBTHelper.setCompound(stack, "inheriting", null)
            } else {
                val nbt = NBTTagCompound()
                inheriting.writeToNBT(nbt)
                ItemNBTHelper.setCompound(stack, "inheriting", nbt)
            }
            return stack
        }
    }

    override fun acceptsPiece(stack: ItemStack, type: EnumCADComponent): Boolean = type == EnumCADComponent.DYE

    override fun setPiece(stack: ItemStack, type: EnumCADComponent, piece: ItemStack?): ItemStack {
        if (type != EnumCADComponent.DYE)
            return stack
        return setInheriting(stack, piece)
    }

    override fun getPiece(stack: ItemStack, type: EnumCADComponent): ItemStack? {
        if (type != EnumCADComponent.DYE)
            return null
        return getInheriting(stack)
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor() = IItemColor {
        stack, tintIndex ->
        if (tintIndex == 1) getColor(stack) else 0xFFFFFF
    }

    override fun getColor(p0: ItemStack?): Int {
        var itemcolor = ItemNBTHelper.getInt(p0, "color", Int.MAX_VALUE)
        if (p0 != null) {
            val inheriting = getInheriting(p0)
            if (inheriting != null && inheriting.item is ICADColorizer) {
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
            if (inheriting != null) {
                tooltip.add("${TextFormatting.GREEN}${local("${LibMisc.MOD_ID}.misc.colorInheritance")}${TextFormatting.GRAY}: ${inheriting.displayName}")
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

    override fun onItemRightClick(itemStackIn: ItemStack?, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack>? {
        var outStack = itemStackIn

        if (playerIn != null && playerIn.isSneaking)
            outStack = ItemStack(ModItems.emptyColorizer)

        return super.onItemRightClick(outStack, worldIn, playerIn, hand)
    }
}
