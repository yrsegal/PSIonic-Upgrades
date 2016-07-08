package wiresegal.psionup.common.items

import net.minecraft.block.Block
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import wiresegal.psionup.client.core.ModelHandler
import wiresegal.psionup.client.core.GuiHandler
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.block.BlockCADCase
import wiresegal.psionup.common.block.base.ItemModBlock

/**
 * @author WireSegal
 * Created at 9:09 PM on 7/5/16.
 */
class ItemCADCase(block: Block) : ItemModBlock(block), ModelHandler.IExtraVariantHolder {

    init {
        setMaxStackSize(1)
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack>? {
        if (!worldIn.isRemote) {
            playerIn.openGui(PsionicUpgrades.instance, GuiHandler.GUI_CASE, worldIn, 0, 0, 0)
            playerIn.worldObj.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.PLAYERS, 1f, 1f)
        }
        return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
    }

    override fun onItemUse(stack: ItemStack?, playerIn: EntityPlayer, worldIn: World, pos: BlockPos?, hand: EnumHand?, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult? {
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ)
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
        override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
            return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == null)
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>?, facing: EnumFacing?): T? {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == null)
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

