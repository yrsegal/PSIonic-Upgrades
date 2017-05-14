package wiresegal.psionup.common.spell.trick

import com.teamwizardry.librarianlib.features.kotlin.sendSpamlessMessage
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamAny
import vazkii.psi.api.spell.param.ParamNumber
import vazkii.psi.api.spell.piece.PieceTrick
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * *         Created at 1:44 PM on 5/14/17.
 */
class PieceTrickDebugSpamless(spell: Spell) : PieceTrick(spell) {

    private lateinit var target: SpellParam
    private lateinit var number: SpellParam

    override fun initParams() {
        target = ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false)
        number = ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, true, false)
        addParam(target)
        addParam(number)
    }

    @Throws(SpellCompilationException::class)
    override fun addToMetadata(meta: SpellMetadata) {
        // NO-OP
    }

    val blockStart = "Begin Spamless Block".hashCode()

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext?): Any? {
        if (context!!.caster.entityWorld.isRemote)
            return null

        val numberVal = this.getParamValue<Double>(context, number) ?: 0.0
        val targetVal = getParamValue<Any>(context, target)

        val cID = blockStart.toLong() + numberVal.hashCode().toLong()



        val baseS = if (cID.toInt() != blockStart) TextComponentString("[")
                .setStyle(Style().setColor(TextFormatting.RED))
                .appendSibling(TextComponentTranslation("${LibMisc.MOD_ID}.channelid", cID)
                        .setStyle(Style().setColor(TextFormatting.RED)))
                .appendSibling(TextComponentString("] ")
                        .setStyle(Style().setColor(TextFormatting.RED)))
        else TextComponentString("")

        var s = TextComponentString("null").setStyle(Style().setColor(TextFormatting.RESET))
        if (targetVal != null)
            s = TextComponentString(targetVal.toString()).setStyle(Style().setColor(TextFormatting.RESET))

        if (numberVal != 0.0) {
            var numStr = "" + numberVal
            if (numberVal - numberVal.toInt() == 0.0) {
                val numInt = numberVal.toInt()
                numStr = "" + numInt
            }

            s = TextComponentString("[$numStr] ").setStyle(Style().setColor(TextFormatting.AQUA))
                    .appendSibling(s)
        }

        context.caster.sendSpamlessMessage(baseS.appendSibling(s), cID.toInt())

        return null
    }


}
