package wiresegal.psionup.client.gui.flashring

import net.minecraft.client.gui.GuiTextField
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import vazkii.psi.api.spell.SpellCompilationException
import vazkii.psi.client.gui.GuiProgrammer
import vazkii.psi.common.spell.SpellCompiler
import wiresegal.psionup.client.core.PsionicClientMethodHandles
import wiresegal.psionup.client.gui.flashring.FlashRingProgrammingWrapper
import wiresegal.psionup.common.network.MessageFlashSync
import wiresegal.psionup.common.network.NetworkHandler

/**
 * @author WireSegal
 * Created at 10:11 PM on 7/9/16.
 */
class GuiFlashRing(val player: EntityPlayer, stack: ItemStack) : GuiProgrammer(FlashRingProgrammingWrapper(player, stack)) {

    val spellNameField: GuiTextField
        get() = PsionicClientMethodHandles.getSpellNameField(this)

    var compiler: SpellCompiler
        get() = PsionicClientMethodHandles.getSpellCompiler(this)
        set(value) = PsionicClientMethodHandles.setSpellCompiler(this, value)

    override fun onSpellChanged(nameOnly: Boolean) {
        val message = MessageFlashSync(programmer.spell)
        NetworkHandler.INSTANCE.sendToServer(message)

        onSelectedChanged()
        spellNameField.isFocused = nameOnly
        if (!nameOnly || compiler.error != null && compiler.error == SpellCompilationException.NO_NAME || programmer.spell.name.isEmpty()) {
            compiler = SpellCompiler(programmer.spell)
        }
    }
}
