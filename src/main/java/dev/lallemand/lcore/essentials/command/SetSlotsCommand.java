package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.BukkitReflection;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "setslots", async = true, permission = "lcore.setslots")
public class SetSlotsCommand {

    // TODO: change slots parameter to int
    public void execute(CommandSender sender, @CPL("slots") String slots) {
        BukkitReflection.setMaxPlayers(lCore.get().getServer(), Integer.parseInt(slots));
        sender.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
    }

}
