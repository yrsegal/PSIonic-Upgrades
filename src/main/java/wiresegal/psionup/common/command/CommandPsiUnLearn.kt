package wiresegal.psionup.common.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import vazkii.psi.api.PsiAPI
import vazkii.psi.common.core.handler.PlayerDataHandler
import wiresegal.psionup.common.lib.LibMisc

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
        if (group == level0) {
            data.lockPieceGroup(level0)
            CommandBase.notifyOperators(sender, this, "$localizationkey.success", player.name, getGroupComponent(group))
        }
        if (group in groups) {
            val pieceGroup = PsiAPI.groupsForName[group]
            if (pieceGroup != null && data.isPieceGroupUnlocked(group)) {
                data.lockPieceGroup(group)
                CommandBase.notifyOperators(sender, this, "$localizationkey.success", player.name, getGroupComponent(group))
            }
        }
    }

    override fun applyAll(player: EntityPlayer, sender: ICommandSender) {
        val data = PlayerDataHandler.get(player)
        data.lockAll()
        CommandBase.notifyOperators(sender, this, "$localizationkey.success.all", player.displayName)
    }

    override fun shouldntApply(player: EntityPlayer, group: String): Boolean {
        return !super.shouldntApply(player, group)
    }

    override val localizationkey: String
        get() = "${LibMisc.MOD_ID_SHORT}.unlearn"
}
