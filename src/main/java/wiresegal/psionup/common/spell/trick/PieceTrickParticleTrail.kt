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

        if (time < 0.0) {
            throw SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER)
        }


        if (pos == null || dir == null)
            throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)

        val ray = dir.copy().normalize()
        val steps = (len * 4).toInt()

        for (i in 0..steps - 1) {
            val extended = ray.copy().multiply(i / 4.0)
            val x = pos.x + extended.x
            val y = pos.y + extended.y
            val z = pos.z + extended.z
            if (!context.isInRadius(x, y, z))
                throw SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS)

            var color = Color(ICADColorizer.DEFAULT_SPELL_COLOR)
            val cad = PsiAPI.getPlayerCAD(context.caster)
            if (cad != null) {
                color = Psi.proxy.getCADColor(cad)
            }

            val r = color.red.toFloat() / 255.0f
            val g = color.green.toFloat() / 255.0f
            val b = color.blue.toFloat() / 255.0f

            makeParticle(context.caster.worldObj, r, g, b, x, y, z, 0.0, 0.0, 0.0, time.toInt())
        }

        return null
    }

    fun makeParticle(world: World, r: Float, g: Float, b: Float, xp: Double, yp: Double, zp: Double, xv: Double, yv: Double, zv: Double, time: Int) {
        val xvn = xv * 0.1
        val yvn = yv * 0.1
        val zvn = zv * 0.1
        Psi.proxy.sparkleFX(world, xp, yp, zp, r, g, b, xvn.toFloat(), yvn.toFloat(), zvn.toFloat(), 0.25f, time)

    }
}
