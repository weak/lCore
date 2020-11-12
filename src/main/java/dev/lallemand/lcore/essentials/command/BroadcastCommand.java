package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"broadcast", "bc"}, permission = "lcore.broadcast")
public class BroadcastCommand {

    public void execute(CommandSender sender, @CPL("message") String broadcast) {
        String message = broadcast.replaceAll("(&([a-f0-9l-or]))", "\u00A7$2");
        Bukkit.broadcastMessage(CC.translate(message));
    }

}
