package wiresegal.psionup.common.core.helper

import net.minecraft.item.ItemStack
import vazkii.psi.api.cad.ISocketable

/**
 * @author WireSegal
 * Created at 7:40 PM on 7/9/16.
 */
class IteratorSocketable(val stack: ItemStack) : Iterator<Pair<Int, ItemStack>> {

    val item = stack.item as ISocketable

    var current = -1

    override fun hasNext(): Boolean {
        return item.isSocketSlotAvailable(stack, current + 1)
    }

    override fun next(): Pair<Int, ItemStack> {
        current++
        return current to item.getBulletInSocket(stack, current)
    }
}
