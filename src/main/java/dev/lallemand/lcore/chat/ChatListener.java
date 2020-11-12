package dev.lallemand.lcore.chat;

import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.chat.event.ChatAttemptEvent;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import dev.lallemand.lcore.Locale;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        ChatAttempt chatAttempt = lCore.get().getChat().attemptChatMessage(event.getPlayer(), event.getMessage());
        ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(event.getPlayer(), chatAttempt, event.getMessage());

        Bukkit.getServer().getPluginManager().callEvent(chatAttemptEvent);

        if (!chatAttemptEvent.isCancelled()) {
            switch (chatAttempt.getResponse()) {
                case ALLOWED: {
                    event.setFormat("%1$s" + CC.RESET + ": %2$s");
                }
                break;

                case MESSAGE_FILTERED: {
                    chatAttempt.getFilterFlagged().punish(event.getPlayer());
                    event.setCancelled(true);
                }
                break;

                case PLAYER_MUTED: {
                    event.setCancelled(true);

                    if (chatAttempt.getPunishment().isPermanent()) {
                        event.getPlayer().sendMessage(CC.RED + "You are muted for forever.");
                    } else {
                        event.getPlayer().sendMessage(CC.RED + "You are muted for another " +
                            chatAttempt.getPunishment().getTimeRemaining() + ".");
                    }
                }
                break;
                case CHAT_MUTED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.RED + "The public chat is currently muted.");
                }
                break;

                case CHAT_DELAYED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Locale.CHAT_DELAYED.format(
                        TimeUtil.millisToSeconds((long) chatAttempt.getValue())) + " seconds");
                }
                break;
            }
        }

        if (chatAttempt.getResponse() == ChatAttempt.Response.ALLOWED) {
            event.getRecipients().removeIf(player -> {
                Profile profile = Profile.getProfiles().get(player.getUniqueId());
                return profile != null && !profile.getOptions().publicChatEnabled();
            });
        }
    }

}
