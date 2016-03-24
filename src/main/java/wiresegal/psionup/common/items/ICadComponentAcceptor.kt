package wiresegal.psionup.common.items

import net.minecraft.item.ItemStack
import vazkii.psi.api.cad.EnumCADComponent

/**
 * @author WireSegal
 * Created at 7:52 PM on 3/20/16.
 */
interface ICadComponentAcceptor {
    fun setPiece(stack: ItemStack, type: EnumCADComponent, piece: ItemStack?): ItemStack

    fun getPiece(stack: ItemStack, type: EnumCADComponent): ItemStack?

    fun acceptsPiece(stack: ItemStack, type: EnumCADComponent): Boolean
}
