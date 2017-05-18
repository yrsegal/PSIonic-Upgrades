package wiresegal.psionup.common.items.base


import com.teamwizardry.librarianlib.features.base.item.ItemMod
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import vazkii.psi.api.cad.EnumCADStat
import vazkii.psi.api.cad.ICADComponent
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 10:23 AM on 3/25/16.
 */
abstract class ItemComponent(name: String, vararg variants: String) : ItemMod(name, *variants), ICADComponent {
    private val stats = mutableMapOf<Pair<EnumCADStat, Int>, Int>()

    init {
        this.setMaxStackSize(1)
        this.registerStats()
    }

    open fun registerStats() {
        // NO-OP
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.tooltipIfShift(tooltip) {
            addHiddenTooltip(stack, playerIn, tooltip, advanced)
        }
    }

    open fun addHiddenTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        val componentType = this.getComponentType(stack)
        val componentName = TooltipHelper.local(componentType.getName())
        TooltipHelper.addToTooltip(tooltip, "psimisc.componentType", componentName)
        val var6 = EnumCADStat.values().size

        for (var7 in 0..var6 - 1) {
            val stat = EnumCADStat.values()[var7]
            if (stat.sourceType == componentType) {
                val statVal = this.getCADStatValue(stack, stat)
                val statValStr = if (statVal == -1) "∞" else "" + statVal
                val name = TooltipHelper.local(stat.getName())
                TooltipHelper.addToTooltip(tooltip, " " + TextFormatting.AQUA + name + TextFormatting.GRAY + ": " + statValStr, *arrayOfNulls<Any>(0))
            }
        }
    }

    protected fun addTooltipTag(tooltip: MutableList<String>, color: TextFormatting, nameKey: String, valueKey: String, vararg formatters: Any?) {
        TooltipHelper.addToTooltip(tooltip, " " + color +
                TooltipHelper.local(nameKey)
                + ": " + TextFormatting.GRAY + TooltipHelper.local(valueKey, *formatters))
    }

    protected fun addPositiveTag(tooltip: MutableList<String>, nameKey: String, valueKey: String, vararg formatters: Any?)
            = addTooltipTag(tooltip, TextFormatting.AQUA, nameKey, valueKey, *formatters)

    protected fun addNegativeTag(tooltip: MutableList<String>, nameKey: String, valueKey: String, vararg formatters: Any?)
            = addTooltipTag(tooltip, TextFormatting.RED, nameKey, valueKey, *formatters)

    fun addStat(stat: EnumCADStat, meta: Int, value: Int) {
        this.stats.put(stat to meta, value)
    }

    override fun getCADStatValue(stack: ItemStack, stat: EnumCADStat): Int {
        val p = stat to stack.itemDamage
        return if (this.stats.containsKey(p)) (this.stats[p] as Int).toInt() else 0
    }
}
