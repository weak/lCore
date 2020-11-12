package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"killall", "clearmobs"}, permission = "lcore.staff.killall", async = true)
public class KillallCommand {

    public void execute(CommandSender sender) {
        sender.sendMessage(CC.CHAT_BAR);
        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(CC.GREEN + "Cleared " + CC.WHITE + lCore.get().getEssentials().clearEntities(world) + CC.GREEN + " entities from " + CC.WHITE + world.getName());
        }
        sender.sendMessage(CC.CHAT_BAR);
    }

}

