package wiresegal.psionup.common.items

import net.minecraft.block.Block
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import wiresegal.psionup.client.core.handler.GuiHandler
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.block.BlockCADCase
import wiresegal.psionup.common.block.base.ItemModBlock
import wiresegal.psionup.common.core.PsionicSoundEvents

/**
 * @author WireSegal
 * Created at 9:09 PM on 7/5/16.
 */
class ItemCADCase(block: Block) : ItemModBlock(block), ModelHandler.IExtraVariantHolder {

    init {
        setMaxStackSize(1)
    }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack>? {
        if (!worldIn.isRemote) {
            playerIn.openGui(PsionicUpgrades.INSTANCE, GuiHandler.GUI_CASE, worldIn, 0, 0, 0)
            playerIn.world.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.PLAYERS, 1f, 1f)
        }
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand))
    }

    override fun onItemUse(playerIn: EntityPlayer, worldIn: World, pos: BlockPos?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult? {
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL
        return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ)
    }

    override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        target.knockBack(attacker, 1f, MathHelper.sin(attacker.rotationYaw * Math.PI.toFloat() / 180).toDouble(), (-MathHelper.cos(attacker.rotationYaw * Math.PI.toFloat() / 180)).toDouble())
        attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, PsionicSoundEvents.THWACK, SoundCategory.PLAYERS, 1.0f, 1.0f)
        return false
    }

    override fun initCapabilities(stack: ItemStack, nbt: NBTTagCompound?): ICapabilityProvider? {
        return CaseCapabilityProvider()
    }

    @SideOnly(Side.CLIENT)
    override fun getCustomMeshDefinition(): ItemMeshDefinition? {
        return ItemMeshDefinition {
            ModelHandler.resourceLocations[psiBlock.bareName]
        }
    }

    override val extraVariants: Array<out String>
        get() = arrayOf(psiBlock.bareName)

    class CaseCapabilityProvider : ICapabilitySerializable<NBTTagCompound> {
        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return itemHandler as T
            return null
        }

        val itemHandler: ItemStackHandler by lazy {
            BlockCADCase.CaseStackHandler()
        }

        override fun deserializeNBT(nbt: NBTTagCompound?) {
            itemHandler.deserializeNBT(nbt)
        }

        override fun serializeNBT(): NBTTagCompound? {
            return itemHandler.serializeNBT()
        }
    }
}

