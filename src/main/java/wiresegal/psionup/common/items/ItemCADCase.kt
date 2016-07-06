package wiresegal.psionup.common.items

import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable
import wiresegal.psionup.client.core.GuiHandler
import wiresegal.psionup.common.PsionicUpgrades
import wiresegal.psionup.common.block.BlockCADCase
import wiresegal.psionup.common.block.base.ItemModBlock

/**
 * @author WireSegal
 * Created at 9:09 PM on 7/5/16.
 */
class ItemCADCase(block: Block) : ItemModBlock(block) {

    init {
        setMaxStackSize(1)
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand?): ActionResult<ItemStack>? {
        if (!worldIn.isRemote)
            playerIn.openGui(PsionicUpgrades.instance, GuiHandler.GUI_CASE, worldIn, 0, 0, 0)
        return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
    }

    override fun initCapabilities(stack: ItemStack, nbt: NBTTagCompound?): ICapabilityProvider? {
        return CaseCapabilityProvider()
    }

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

