package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.util.DateUtil;
import dev.lallemand.lcore.util.string.CC;

import java.lang.management.ManagementFactory;

@CommandMeta(label = {"lag", "lagg"}, async = true)
public class LagCommand {

    // private static String format(double tps) {
    //    return (tps / 2.0D > 18.0D ? ChatColor.GREEN : (tps / 2.0D > 16.0D ? ChatColor.YELLOW : ChatColor.RED)).toString() + (tps / 2.0D > 20.0D ? "*" : "") + Math.min((double) Math.round(tps * 100.0D) / 100.0D, DedicatedServer.TPS);
    // }

    public void execute(CommandSender sender) {
        // double[] tps = Bukkit.spigot().getTPS();
        // String[] tpsAvg = new String[tps.length];
        // for (int i = 0; i < tps.length; i++) {
        //    tpsAvg[i] = format(tps[i]);
        // }

        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&6&lServer Info:"));
        sender.sendMessage(CC.translate("&7» &6Online&7: &f" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()));
        // sender.sendMessage(CC.translate("&7» &6TPS&7: &f" + StringUtils.join(tpsAvg, ", ")));
        sender.sendMessage(CC.translate("&7» &6Uptime&7: &f" + DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
        sender.sendMessage(CC.translate("&7» &6Last Tick Time: &f" + (System.currentTimeMillis() - MinecraftServer.currentTick) + "ms"));
        sender.sendMessage(CC.translate("&7» &6Max RAM&7: &f" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB"));
        sender.sendMessage(CC.translate("&7» &6Alloc RAM&7: &f" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB"));
        sender.sendMessage(CC.translate("&7» &6Free RAM&7: &f" + Runtime.getRuntime().freeMemory() / 1024 / 1024) + "MB");
        sender.sendMessage(CC.CHAT_BAR);
        for (World world : Bukkit.getWorlds())
            sender.sendMessage(CC.translate("&7» &6" + world.getName() + "&7: &6Loaded Chunks&7: &6" + world.getLoadedChunks().length + ", &6Entities: &7" + world.getEntities().size()));
        sender.sendMessage(CC.CHAT_BAR);
    }

}

