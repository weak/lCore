package dev.lallemand.lcore.chat.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;

@CommandMeta(label = "mutechat", permission = "lcore.staff.mutechat")
public class MuteChatCommand {

    public void execute(CommandSender sender) {
        lCore.get().getChat().togglePublicChatMute();

        String senderName;

        if (sender instanceof Player) {
            Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
            senderName = profile.getActiveRank().getColor() + sender.getName();
        } else {
            senderName = ChatColor.DARK_RED + "Console";
        }

        String context = lCore.get().getChat().isPublicChatMuted() ? "muted" : "unmuted";

        Bukkit.broadcastMessage(Locale.MUTE_CHAT_BROADCAST.format(context, senderName));
    }

}
