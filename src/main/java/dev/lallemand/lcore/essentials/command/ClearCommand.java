package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"clearinv", "clear", "ci"}, permission = "lcore.clearinv")
public class ClearCommand {

    public void execute(Player player) {
        player.getInventory().setContents(new ItemStack[36]);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.updateInventory();
        player.sendMessage(CC.GOLD + "You cleared your inventory.");
    }

    public void execute(CommandSender sender, Player player) {
        player.getInventory().setContents(new ItemStack[36]);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.updateInventory();
        player.sendMessage(CC.GOLD + "Your inventory has been cleared by " + sender.getName());
    }

}
