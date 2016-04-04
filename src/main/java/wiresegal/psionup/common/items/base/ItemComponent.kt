package wiresegal.psionup.common.items.base

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import org.apache.commons.lang3.tuple.Pair
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICADComponent
import wiresegal.psionup.common.core.CreativeTab
import java.util.*
import vazkii.psi.common.item.base.ItemMod as PsiItem

/**
 * @author WireSegal
 * Created at 10:23 AM on 3/25/16.
 */
abstract class ItemComponent(name: String, vararg variants: String) : ItemMod(name, *variants), ICADComponent {
    private val stats: HashMap<Pair<EnumCADStat, Int>, Int>

    init {
        this.setMaxStackSize(1)
        this.stats = HashMap()
        this.registerStats()
    }

    open fun registerStats() {
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        vazkii.psi.common.item.base.ItemMod.tooltipIfShift(tooltip) {
            val componentType = this.getComponentType(stack)
            val componentName = vazkii.psi.common.item.base.ItemMod.local(componentType.getName())
            vazkii.psi.common.item.base.ItemMod.addToTooltip(tooltip, "psimisc.componentType", *arrayOf<Any>(componentName))
            val var6 = EnumCADStat.values().size

            for (var7 in 0..var6 - 1) {
                val stat = EnumCADStat.values()[var7]
                if (stat.sourceType == componentType) {
                    val statVal = this.getCADStatValue(stack, stat)
                    val statValStr = if (statVal == -1) "∞" else "" + statVal
                    val name = PsiItem.local(stat.getName())
                    PsiItem.addToTooltip(tooltip, " " + TextFormatting.AQUA + name + TextFormatting.GRAY + ": " + statValStr, *arrayOfNulls<Any>(0))
                }
            }

        }
    }

    fun addStat(stat: EnumCADStat, meta: Int, value: Int) {
        this.stats.put(Pair.of(stat, Integer.valueOf(meta)), Integer.valueOf(value))
    }

    override fun getCADStatValue(stack: ItemStack, stat: EnumCADStat): Int {
        val p = Pair.of(stat, Integer.valueOf(stack.itemDamage))
        return if (this.stats.containsKey(p)) (this.stats[p] as Int).toInt() else 0
    }
}
