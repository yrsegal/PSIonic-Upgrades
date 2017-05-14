package wiresegal.psionup.common.items.spell

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemModArmor
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.addToTooltip
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.local
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper.tooltipIfShift
import net.minecraft.client.renderer.block.model.IBakedModel
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.api.cad.ISocketable
import vazkii.psi.api.exosuit.IExosuitSensor
import vazkii.psi.api.exosuit.IPsiEventArmor
import vazkii.psi.api.exosuit.ISensorHoldable
import vazkii.psi.api.exosuit.PsiArmorEvent
import vazkii.psi.api.spell.ISpellSettable
import vazkii.psi.api.spell.Spell
import vazkii.psi.client.model.ModelPsimetalExosuit
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.item.ItemCAD
import vazkii.psi.common.item.base.ModItems
import vazkii.psi.common.item.tool.ItemPsimetalTool
import wiresegal.psionup.common.core.helper.FlowColors
import wiresegal.psionup.common.items.spell.ItemFlowExosuit.Companion.models
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 4:42 PM on 7/9/16.
 */
open class ItemFlowExosuit(name: String, slot: EntityEquipmentSlot, val ebony: Boolean) : ItemModArmor(name, PsiAPI.PSIMETAL_ARMOR_MATERIAL, slot), ISocketable, ISpellSettable, IPsiEventArmor, IItemColorProvider, FlowColors.IAcceptor, IGlowingItem{
    companion object {
        val models by lazy {
            Array(4) {
                ModelPsimetalExosuit(it)
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel): IBakedModel? {
        return IGlowingItem.Helper.wrapperBake(model, false, 1)
    }

    @SideOnly(Side.CLIENT)
    override fun shouldDisableLightingForGlow(itemStack: ItemStack, model: IBakedModel): Boolean {
        return true
    }

    override fun isSocketSlotAvailable(stack: ItemStack, slot: Int): Boolean {
        return slot < 3
    }

    override fun showSlotInRadialMenu(stack: ItemStack, slot: Int): Boolean {
        return this.isSocketSlotAvailable(stack, slot - 1)
    }

    override fun getBulletInSocket(stack: ItemStack, slot: Int): ItemStack {
        val name = "bullet" + slot
        val cmp = ItemNBTHelper.getCompound(stack, name)
        return if (cmp == null) ItemStack.EMPTY else ItemStack(cmp)
    }

    override fun setBulletInSocket(stack: ItemStack, slot: Int, bullet: ItemStack) {
        val name = "bullet" + slot
        val cmp = NBTTagCompound()
        bullet.writeToNBT(cmp)

        ItemNBTHelper.setCompound(stack, name, cmp)
    }

    override fun getSelectedSlot(stack: ItemStack): Int {
        return ItemNBTHelper.getInt(stack, "selectedSlot", 0)
    }

    override fun setSelectedSlot(stack: ItemStack, slot: Int) {
        ItemNBTHelper.setInt(stack, "selectedSlot", slot)
    }

    override fun setSpell(player: EntityPlayer, stack: ItemStack, spell: Spell) {
        val slot = this.getSelectedSlot(stack)
        val bullet = this.getBulletInSocket(stack, slot)
        if (!bullet.isEmpty && bullet.item is ISpellSettable) {
            (bullet.item as ISpellSettable).setSpell(player, bullet, spell)
            this.setBulletInSocket(stack, slot, bullet)
        }

    }

    override fun onArmorTick(world: World?, player: EntityPlayer?, itemStack: ItemStack) {
        ItemPsimetalTool.regen(itemStack, player, false)
    }

    fun cast(stack: ItemStack, event: PsiArmorEvent) {
        val data = PlayerDataHandler.get(event.entityPlayer)
        val playerCad = PsiAPI.getPlayerCAD(event.entityPlayer)
        if (!playerCad.isEmpty) {
            val bullet = this.getBulletInSocket(stack, this.getSelectedSlot(stack))
            ItemCAD.cast(event.entityPlayer.world, event.entityPlayer, data, bullet, playerCad, this.getCastCooldown(stack), 0, this.castVolume) { context ->
                context.tool = stack
                context.attackingEntity = event.attacker
                context.damageTaken = event.damage
            }
        }

    }

    override fun onEvent(stack: ItemStack, event: PsiArmorEvent) {
        if (event.type == this.getEvent(stack)) {
            this.cast(stack, event)
        }

    }

    open fun getEvent(stack: ItemStack): String {
        return PsiArmorEvent.NONE
    }

    open fun getCastCooldown(stack: ItemStack): Int {
        return 5
    }

    open val castVolume: Float
        get() = 0.025f

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        tooltipIfShift(tooltip) {
            val componentName = local(ISocketable.getSocketedItemName(stack, "psimisc.none"))
            addToTooltip(tooltip, "psimisc.spellSelected", componentName)
            addToTooltip(tooltip, this.getEvent(stack))
        }
    }

    override fun getIsRepairable(par1ItemStack: ItemStack, par2ItemStack: ItemStack): Boolean {
        return if (par2ItemStack.item === ModItems.material && par2ItemStack.itemDamage == if (ebony) 4 else 5) true else super.getIsRepairable(par1ItemStack, par2ItemStack)
    }

    override fun getArmorTexture(stack: ItemStack, entity: Entity?, slot: EntityEquipmentSlot?, type: String?): String {
        val overlay = type != null && type == "overlay"
        if (overlay)
            return "${LibMisc.MOD_ID}:textures/model/${if (ebony) "ebony" else "ivory"}_exosuit.png"
        else
            return "psi:textures/model/psimetal_exosuit_sensor.png"
    }

    fun getColorFromPlayer(player: EntityPlayer): Int {
        val cad = PsiAPI.getPlayerCAD(player)
        return if (cad.isEmpty) 0 else Psi.proxy.getCADColor(cad).rgb
    }

    override fun hasColor(stack: ItemStack): Boolean {
        return true
    }

    override fun getColor(stack: ItemStack): Int {
        return ICADColorizer.DEFAULT_SPELL_COLOR
    }

    override val itemColorFunction: ((stack: ItemStack, tintIndex: Int) -> Int)?
        get() = {
            stack, tintIndex ->
            if (tintIndex == 1) {
                val colorizer = FlowColors.getColor(stack)
                if (colorizer.isEmpty) 0 else Psi.proxy.getColorizerColor(colorizer).rgb
            } else if (tintIndex == 2)
                this@ItemFlowExosuit.getColor(stack)
            else
                16777215
        }

    @SideOnly(Side.CLIENT)
    override fun getArmorModel(entityLiving: EntityLivingBase, itemStack: ItemStack, armorSlot: EntityEquipmentSlot?, _default: ModelBiped?): ModelBiped {
        val slotIndex = armorSlot!!.ordinal
        return models[slotIndex - 2]
    }

    override fun requiresSneakForSpellSet(stack: ItemStack): Boolean {
        return false
    }

    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean {
        FlowColors.purgeColor(entityItem.entityItem)
        return super.onEntityItemUpdate(entityItem)
    }

    class Helmet(name: String, ebony: Boolean) : ItemFlowExosuit(name, EntityEquipmentSlot.HEAD, ebony), ISensorHoldable {

        val TAG_SENSOR = "sensor"

        override fun getEvent(stack: ItemStack): String {
            val sensor = this.getAttachedSensor(stack)
            return if (!sensor.isEmpty && sensor.item is IExosuitSensor) (sensor.item as IExosuitSensor).getEventType(sensor) else super.getEvent(stack)
        }

        override fun getCastCooldown(stack: ItemStack): Int {
            return 40
        }

        override fun getColor(stack: ItemStack): Int {
            val sensor = this.getAttachedSensor(stack)
            return if (!sensor.isEmpty && sensor.item is IExosuitSensor) (sensor.item as IExosuitSensor).getColor(sensor) else super.getColor(stack)
        }

        override fun getAttachedSensor(stack: ItemStack): ItemStack {
            val cmp = ItemNBTHelper.getCompound(stack, TAG_SENSOR) ?: NBTTagCompound()
            val sensor = ItemStack(cmp)
            return sensor
        }

        override fun attachSensor(stack: ItemStack, sensor: ItemStack) {
            val cmp = NBTTagCompound()
            sensor.writeToNBT(cmp)

            ItemNBTHelper.setCompound(stack, TAG_SENSOR, cmp)
        }

        override fun hasContainerItem(stack: ItemStack): Boolean {
            return true
        }

        override fun getContainerItem(itemStack: ItemStack): ItemStack {
            return this.getAttachedSensor(itemStack)
        }
    }

    class Chest(name: String, ebony: Boolean) : ItemFlowExosuit(name, EntityEquipmentSlot.CHEST, ebony) {

        override fun getEvent(stack: ItemStack): String {
            return PsiArmorEvent.DAMAGE
        }
    }

    class Legs(name: String, ebony: Boolean) : ItemFlowExosuit(name, EntityEquipmentSlot.LEGS, ebony) {

        override fun getEvent(stack: ItemStack): String {
            return PsiArmorEvent.TICK
        }

        override fun getCastCooldown(stack: ItemStack): Int {
            return 0
        }

        override val castVolume: Float
            get() = 0.0f
    }

    class Boots(name: String, ebony: Boolean) : ItemFlowExosuit(name, EntityEquipmentSlot.FEET, ebony) {

        override fun getEvent(stack: ItemStack): String {
            return PsiArmorEvent.JUMP
        }
    }
}
