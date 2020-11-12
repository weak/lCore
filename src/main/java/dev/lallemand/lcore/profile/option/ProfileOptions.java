package dev.lallemand.lcore.profile.option;

import dev.lallemand.lcore.profile.option.enmus.Time;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;

@Accessors(fluent = true)
@Getter
@Setter
public class ProfileOptions {

    private boolean publicChatEnabled = true;
    private boolean receivingNewConversations = true;
    private boolean playingMessageSounds = true;
    private Time time = Time.DAY;
    private ChatColor color = ChatColor.GRAY;
    private boolean tipsAnnounce = true;
    private boolean scoreboard = true;

}
