package dev.lallemand.lcore.chat.filter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import dev.lallemand.lcore.lCore;

public abstract class ChatFilter implements Listener {

    private String command;

    public ChatFilter() {
    }

    public ChatFilter(String command) {
        this.command = command;
    }

    public abstract boolean isFiltered(String message, String[] words);

    public void punish(Player player) {
        if (command != null) {
            command = command
                .replace("{player}", player.getName())
                .replace("{player-uuid}", player.getUniqueId().toString());

            lCore.get().getLogger().info("Dispatching punish command for ChatFilter: \"" + command + "\"");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
        }
    }

}
