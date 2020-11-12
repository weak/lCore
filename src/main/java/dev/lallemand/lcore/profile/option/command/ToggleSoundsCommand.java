package dev.lallemand.lcore.profile.option.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;

@CommandMeta(label = {"togglesounds", "sounds"})
public class ToggleSoundsCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().playingMessageSounds(!profile.getOptions().playingMessageSounds());

        if (profile.getOptions().playingMessageSounds()) {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGE_SOUND_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGE_SOUND_DISABLED.format());
        }
    }

}
