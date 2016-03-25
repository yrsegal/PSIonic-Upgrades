package wiresegal.psionup.common.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import vazkii.psi.api.PsiAPI
import vazkii.psi.common.core.handler.PlayerDataHandler

/**
 * @author WireSegal
 * Created at 12:50 PM on 3/25/16.
 */
open class CommandPsiUnlearn : CommandPsiLearn() {

    override fun getCommandName(): String {
        return "psi-unlearn"
    }

    override fun applyPlayerData(player: EntityPlayer, group: String, sender: ICommandSender) {
        val data = PlayerDataHandler.get(player)
        if (group in groups) {
            val pieceGroup = PsiAPI.groupsForName[group]
            if (pieceGroup != null && data.isPieceGroupUnlocked(group)) {
                data.lockPieceGroup(group)
                CommandBase.notifyOperators(sender, this, "$localizationkey.success", player.name, group)
            }
        }
    }

    override fun applyAll(player: EntityPlayer, sender: ICommandSender) {
        val data = PlayerDataHandler.get(player)
        data.lockAll()
        CommandBase.notifyOperators(sender, this, "$localizationkey.success.all", player.displayName)
    }

    override fun shouldHave(player: EntityPlayer, group: String): Boolean {
        return !super.shouldHave(player, group)
    }

    override val localizationkey: String
        get() = "psionup.unlearn"
}
