package wiresegal.psionup.common.spell.trick

import net.minecraft.world.World
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamNumber
import vazkii.psi.api.spell.param.ParamVector
import vazkii.psi.api.spell.piece.PieceTrick
import vazkii.psi.common.Psi
import wiresegal.psionup.common.network.MessageParticleTrail
import wiresegal.psionup.common.network.NetworkHandler
import java.awt.Color

/**
 * @author WireSegal
 * Created at 1:55 PM on 3/27/16.
 */
class PieceTrickParticleTrail(spell: Spell) : PieceTrick(spell) {
    internal lateinit var position: SpellParam
    internal lateinit var ray: SpellParam
    internal lateinit var length: SpellParam
    internal lateinit var time: SpellParam

    override fun initParams() {
        position = ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false)
        ray = ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false)
        length = ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.CYAN, false, true)
        time = ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false)

        this.addParam(position)
        this.addParam(ray)
        this.addParam(length)
        this.addParam(time)
    }

    @Throws(SpellCompilationException::class)
    override fun addToMetadata(meta: SpellMetadata) {
        super.addToMetadata(meta)

        val length = getParamEvaluation<Double>(length)
        if (length != null && length > 0.0) {
            meta.addStat(EnumSpellStat.POTENCY, (10 * length).toInt())
        } else {
            throw SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, this.x, this.y)
        }
    }

    @Throws(SpellRuntimeException::class)
    override fun execute(context: SpellContext): Any? {

        val pos = getParamValue<Vector3>(context, position)
        val dir = getParamValue<Vector3>(context, ray)
        val len = getParamValue<Double>(context, length)
        val time = Math.min(getParamValue<Double>(context, time) ?: 20.0, 400.0)

        if (time < 0.0)
            throw SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER)

        if (pos == null || dir == null)
            throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)

        if (!context.isInRadius(pos) || !context.isInRadius(pos.copy().add(dir.copy().multiply(len))))
            throw SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS)

        if (!context.caster.worldObj.isRemote)
            NetworkHandler.INSTANCE.sendToDimension(MessageParticleTrail(pos, dir, len, time.toInt(), PsiAPI.getPlayerCAD(context.caster)), context.caster.worldObj.provider.dimension)

        return null
    }
}
