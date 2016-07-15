package wiresegal.psionup.common.spell.trick

import net.minecraft.nbt.NBTTagCompound
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamNumber
import vazkii.psi.api.spell.piece.PieceTrick
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.entity.EntitySpellCircle

/**
 * @author WireSegal
 * Created at 8:58 PM on 7/13/16.
 */
class PieceTrickBreakLoop(spell: Spell) : PieceTrick(spell) {
    lateinit var target: SpellParam

    override fun initParams() {
        this.target = ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, false)
        this.addParam(target)
    }

    @Throws(SpellCompilationException::class)
    override fun addToMetadata(meta: SpellMetadata) {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1)
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {
        val value = this.getParamValue<Double>(context, this.target)
        if (Math.abs(value.toDouble()) < 1.0) {
            if (context.focalPoint != context.caster) {
                if (context.focalPoint is EntitySpellCircle) {
                    val circle = context.focalPoint as EntitySpellCircle
                    val nbt = circle.writeToNBT(NBTTagCompound())
                    nbt.setInteger("timesCast", 20)
                    nbt.setInteger("timeAlive", 100)
                    circle.readFromNBT(nbt)
                } else
                    context.focalPoint.setDead()
            } else {
                val data = PlayerDataHandler.get(context.caster)
                data.stopLoopcast()
            }
            context.stopped = true
        }
        return null
    }
}
