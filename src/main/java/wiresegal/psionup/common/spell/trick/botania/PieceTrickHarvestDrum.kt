//package wiresegal.psionup.common.spell.trick.botania
//
//import net.minecraft.entity.EntityLiving
//import net.minecraft.entity.item.EntityItem
//import net.minecraft.entity.passive.EntityCow
//import net.minecraft.init.Items
//import net.minecraft.init.SoundEvents
//import net.minecraft.item.ItemStack
//import net.minecraft.util.EnumParticleTypes
//import net.minecraft.util.SoundCategory
//import net.minecraft.util.math.AxisAlignedBB
//import net.minecraft.util.math.BlockPos
//import net.minecraftforge.common.IShearable
//import vazkii.botania.common.item.ItemGrassHorn
//import vazkii.psi.api.PsiAPI
//import vazkii.psi.api.internal.MathHelper
//import vazkii.psi.api.internal.Vector3
//import vazkii.psi.api.spell.*
//import vazkii.psi.api.spell.param.ParamVector
//import wiresegal.psionup.api.enabling.PieceComponentTrick
//import wiresegal.psionup.api.enabling.botania.EnumManaTier
//import wiresegal.psionup.api.enabling.botania.IManaTrick
//import java.util.*
//
///**
// * @author WireSegal
// * Created at 9:04 PM on 4/2/16.
// */
//open class PieceTrickHarvestDrum(spell: Spell) : PieceComponentTrick(spell), IManaTrick {
//
//    companion object {
//        class PieceTrickLeafDrum(spell: Spell) : PieceTrickHarvestDrum(spell) {
//            override fun effect(context: SpellContext, pos: BlockPos) {
//                ItemGrassHorn.breakGrass(context.caster.world, null, 1, pos)
//            }
//        }
//
//        class PieceTrickShearDrum(spell: Spell) : PieceTrickHarvestDrum(spell) {
//            override fun effect(context: SpellContext, pos: BlockPos) {
//                val world = context.caster.world
//
//                val range = 10
//                val entities = world.getEntitiesWithinAABB(EntityLiving::class.java, AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)))
//                val shearables = ArrayList<EntityLiving>()
//                val stack = PsiAPI.getPlayerCAD(context.caster)
//
//                for (entity in entities) {
//                    if (entity is IShearable && entity.isShearable(stack, world, BlockPos(entity))) {
//                        shearables.add(entity)
//                    } else if (entity is EntityCow) {
//                        val items = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.posX + entity.width, entity.posY + entity.height, entity.posZ + entity.width))
//                        for (item in items) {
//                            val itemstack = item.entityItem
//                            if (itemstack != null && itemstack.item === Items.BUCKET && !world.isRemote) {
//                                while (itemstack.count > 0) {
//                                    val ent = entity.entityDropItem(ItemStack(Items.MILK_BUCKET), 1.0f)
//                                    ent.motionY += world.rand.nextFloat() * 0.05f
//                                    ent.motionX += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1f
//                                    ent.motionZ += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1f
//                                    itemstack.count--
//                                }
//                                item.setDead()
//                            }
//                        }
//                    }
//                }
//
//                Collections.shuffle(shearables)
//                var sheared = 0
//
//                for (entity in shearables) {
//                    if (sheared > 4)
//                        break
//
//                    val stacks = (entity as IShearable).onSheared(stack, world, BlockPos(entity), 0)
//                    if (stacks != null && !world.isRemote)
//                        for (wool in stacks) {
//                            val ent = entity.entityDropItem(wool, 1.0f)
//                            ent.motionY += world.rand.nextFloat() * 0.05f
//                            ent.motionX += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1f
//                            ent.motionZ += (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1f
//                        }
//                    ++sheared
//                }
//            }
//
//            override fun manaDrain(context: SpellContext?, x: Int, y: Int): Int {
//                return 500
//            }
//
//            override fun tier() = EnumManaTier.ALFHEIM
//        }
//    }
//
//    internal lateinit var position: SpellParam
//
//    override fun initParams() {
//        position = ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false)
//
//        this.addParam(position)
//    }
//
//    override fun addToMetadata(meta: SpellMetadata) {
//        super.addToMetadata(meta)
//
//        meta.addStat(EnumSpellStat.COST, 400)
//        meta.addStat(EnumSpellStat.POTENCY, 125)
//    }
//
//    override fun manaDrain(context: SpellContext?, x: Int, y: Int): Int {
//        return 120
//    }
//
//    @Throws(SpellRuntimeException::class)
//    override fun executeIfAllowed(context: SpellContext): Any? {
//        val position = getParamValue<Vector3>(context, position) ?: throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)
//        if (MathHelper.pointDistanceSpace(position.x, position.y, position.z, context.focalPoint.posX, context.focalPoint.posY, context.focalPoint.posZ) > 22.0)
//            throw SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS)
//
//        if (!context.caster.world.isRemote)
//            for (i in 0..9)
//                context.caster.world.playSound(null, BlockPos(position.x, position.y, position.z), SoundEvents.BLOCK_NOTE_BASEDRUM, SoundCategory.BLOCKS, 1F, 1F)
//        else context.caster.world.spawnParticle(EnumParticleTypes.NOTE, position.x + 0.5, position.y + 1.2, position.z + 0.5, 1.0 / 24.0, 0.0, 0.0)
//
//        effect(context, BlockPos(position.x, position.y, position.z))
//
//        return null
//    }
//
//    open fun effect(context: SpellContext, pos: BlockPos) {
//        ItemGrassHorn.breakGrass(context.caster.world, null, 0, pos)
//    }
//}
