package dev.lallemand.lcore.chat;

import com.google.common.collect.Lists;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.chat.filter.ChatFilter;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.ProfileCooldown;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Chat {

    private final List<ChatFilter> filters = Lists.newArrayList();

    private lCore lcore;
    @Setter
    private int delayTime = 3;
    private boolean publicChatMuted = false;
    private boolean publicChatDelayed = false;
    private List<String> filteredPhrases = new ArrayList<>();
    private List<String> linkWhitelist = new ArrayList<>();

    public Chat(lCore lcore) {
        this.lcore = lcore;
    }

    public ChatAttempt attemptChatMessage(Player player, String message) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());

        if (profile.getActivePunishmentByType(PunishmentType.MUTE) != null) {
            return new ChatAttempt(ChatAttempt.Response.PLAYER_MUTED, profile.getActivePunishmentByType(PunishmentType.MUTE));
        }

        if (publicChatMuted && !player.hasPermission("lcore.chat.bypassmute")) {
            return new ChatAttempt(ChatAttempt.Response.CHAT_MUTED);
        }

        if (publicChatDelayed && !profile.getChatCooldown().hasExpired() && !player.hasPermission("lcore.chat.bypassslow")) {
            ChatAttempt attempt = new ChatAttempt(ChatAttempt.Response.CHAT_DELAYED);
            attempt.setValue(profile.getChatCooldown().getRemaining());
            return attempt;
        }

        String msg = message.toLowerCase()
            .replace("3", "e")
            .replace("1", "i")
            .replace("!", "i")
            .replace("@", "a")
            .replace("7", "t")
            .replace("0", "o")
            .replace("5", "s")
            .replace("8", "b")
            .replaceAll("\\p{Punct}|\\d", "").trim();

        String[] words = msg.trim().split(" ");

        for (ChatFilter chatFilter : filters) {
            if (chatFilter.isFiltered(msg, words)) {
                return new ChatAttempt(ChatAttempt.Response.MESSAGE_FILTERED, chatFilter);
            }
        }

        if (publicChatDelayed) {
            profile.setChatCooldown(new ProfileCooldown(delayTime * 1000L));
        }

        return new ChatAttempt(ChatAttempt.Response.ALLOWED);
    }

    public void togglePublicChatMute() {
        publicChatMuted = !publicChatMuted;
    }

    public void togglePublicChatDelay() {
        publicChatDelayed = !publicChatDelayed;
    }

}
