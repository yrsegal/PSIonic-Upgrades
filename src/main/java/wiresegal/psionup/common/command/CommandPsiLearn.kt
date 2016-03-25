package wiresegal.psionup.common.command

import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.event.HoverEvent
import net.minecraft.util.text.translation.I18n
import vazkii.psi.api.PsiAPI
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.network.NetworkHandler
import vazkii.psi.common.network.message.MessageDataSync
import java.util.*

/**
 * @author WireSegal
 * Created at 12:50 PM on 3/25/16.
 */
open class CommandPsiLearn : CommandBase() {

    companion object {
        val groups: List<String>
            get() = listOf(*PsiAPI.groupsForName.keys.toTypedArray())

        fun PlayerDataHandler.PlayerData.lockPieceGroup(group: String) {
            if (this.isPieceGroupUnlocked(group)) {
                this.spellGroupsUnlocked.remove(group)
                if (this.lastSpellGroup == group)
                    this.lastSpellGroup = ""
                this.level--
                this.save()
            }

        }

        fun PlayerDataHandler.PlayerData.unlockPieceGroupFree(group: String) {
            if (!this.isPieceGroupUnlocked(group)) {
                this.spellGroupsUnlocked.add(group)
                this.lastSpellGroup = group
                this.level++
                this.save()
            }
        }

        fun PlayerDataHandler.PlayerData.unlockAll() {
            for (group in groups)
                if (group !in spellGroupsUnlocked)
                    unlockPieceGroupFree(group)
            lastSpellGroup = ""
            save()
        }

        fun PlayerDataHandler.PlayerData.lockAll() {
            val unlocked = ArrayList(spellGroupsUnlocked)
            for (group in unlocked)
                lockPieceGroup(group)
            level = 0
            lastSpellGroup = ""
            save()
        }

        fun EntityPlayer.hasGroup(group: String): Boolean {
            val data = PlayerDataHandler.get(this)
            return data.isPieceGroupUnlocked(group)
        }

        fun getGroupComponent(group: String): ITextComponent {
            val pieceGroup = PsiAPI.groupsForName[group]
            if (pieceGroup == null) {
                val errorComponent = TextComponentString("ERROR")
                errorComponent.chatStyle.color = TextFormatting.RED
                return errorComponent
            }
            val nameComponent = TextComponentString("[" + I18n.translateToLocal(pieceGroup.unlocalizedName) + "]")
            nameComponent.chatStyle.color = TextFormatting.AQUA
            nameComponent.chatStyle.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponentTranslation("psimisc.levelDisplay", pieceGroup.levelRequirement))
            return nameComponent
        }
    }

    override fun getCommandName(): String {
        return "psi-learn"
    }

    override fun getCommandUsage(var1: ICommandSender): String {
        return I18n.translateToLocal("$localizationkey.usage")
    }

    override fun getRequiredPermissionLevel(): Int {
        return 2
    }

    open fun applyPlayerData(player: EntityPlayer, group: String, sender: ICommandSender) {
        val data = PlayerDataHandler.get(player)
        if (group in groups) {
            val pieceGroup = PsiAPI.groupsForName[group]
            if (pieceGroup != null && !data.isPieceGroupUnlocked(group)) {
                for (subgroup in pieceGroup.requirements)
                    if (!data.isPieceGroupUnlocked(subgroup))
                        applyPlayerData(player, subgroup, sender)
                data.unlockPieceGroupFree(group)
                CommandBase.notifyOperators(sender, this, "$localizationkey.success", player.name, getGroupComponent(group))
            }
        }
    }

    open fun applyAll(player: EntityPlayer, sender: ICommandSender) {
        val data = PlayerDataHandler.get(player)
        data.unlockAll()
        CommandBase.notifyOperators(sender, this, "$localizationkey.success.all", player.displayName)
    }

    open fun shouldHave(player: EntityPlayer, group: String): Boolean {
        return player.hasGroup(group)
    }

    open val localizationkey: String
        get() = "psionup.learn"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (args.size < 1 || args.size > 3) {
            throw WrongUsageException("$localizationkey.usage")
        } else {
            val player: Entity?
            if (args.size == 2) {
                val entity = CommandBase.getEntity(server, sender, args[1])
                player = entity
            } else {
                val entity = sender.commandSenderEntity
                player = entity
            }

            if (player == null || player !is EntityPlayer) {
                throw CommandException("psionup.learn.players", player.displayName)
            } else if (args[0] == "*") {
                applyAll(player, sender)
                if (player is EntityPlayerMP) {
                    val message = MessageDataSync(PlayerDataHandler.get(player))
                    NetworkHandler.INSTANCE.sendTo(message, player)
                }
            } else if (args[0] !in groups) {
                throw CommandException("psionup.learn.notAGroup", args[0])
            } else if (shouldHave(player, args[0])) {
                throw CommandException("$localizationkey.shouldnt", player.displayName, getGroupComponent(args[0]))
            } else {
                applyPlayerData(player, args[0], sender)
                if (player is EntityPlayerMP) {
                    val message = MessageDataSync(PlayerDataHandler.get(player))
                    NetworkHandler.INSTANCE.sendTo(message, player)
                }
            }

        }
    }

    override fun getTabCompletionOptions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, pos: BlockPos?): List<String> {
        return when (args.size) {
            1 -> CommandBase.getListOfStringsMatchingLastWord(args, groups)
            2 -> CommandBase.getListOfStringsMatchingLastWord(args, *server.allUsernames)
            else -> emptyList()
        }
    }
}
