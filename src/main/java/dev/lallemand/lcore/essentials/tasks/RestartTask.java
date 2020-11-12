package dev.lallemand.lcore.essentials.tasks;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.BungeeUtil;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.string.CC;

@AllArgsConstructor
public class RestartTask implements Runnable {

    int time;

    @Override
    public void run() {
        if (time <= 0) {
            Bukkit.broadcastMessage(CC.translate("&aThe server will restart now"));

            lCore.get().getEssentials().cancelRestart();
            for (Player player : Bukkit.getOnlinePlayers()) {
                BungeeUtil.connect(player, "lobby");
            }
            Bukkit.shutdown();
            return;
        }
        if (String.valueOf(time).endsWith("0") || String.valueOf(time).endsWith("5")) {
            Bukkit.broadcastMessage(CC.translate("&aThe server will restart in&e " + TimeUtil.formatIntoDetailedString(time)));
        }
        time--;
    }
}
