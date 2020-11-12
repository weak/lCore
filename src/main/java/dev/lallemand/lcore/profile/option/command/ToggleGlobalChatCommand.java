package dev.lallemand.lcore.profile.option.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;

@CommandMeta(label = {"toggleglobalchat", "tgc", "togglepublicchat", "tpc"})
public class ToggleGlobalChatCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());

        if (profile.getOptions().publicChatEnabled()) {
            player.sendMessage(Locale.OPTIONS_GLOBAL_CHAT_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_GLOBAL_CHAT_DISABLED.format());
        }
    }

}
