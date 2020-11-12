package dev.lallemand.lcore.profile.option.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;

@CommandMeta(label = {"togglepm", "togglepms", "tpm", "tpms"})
public class TogglePrivateMessagesCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().receivingNewConversations(!profile.getOptions().receivingNewConversations());
        profile.getConversations().expireAllConversations();

        if (profile.getOptions().receivingNewConversations()) {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGES_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGES_DISABLED.format());
        }
    }

}
