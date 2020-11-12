package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "more", permission = "lcore.more")
public class MoreCommand {

    public void execute(Player player) {
        if (player.getItemInHand() == null) {
            player.sendMessage(CC.RED + "There is nothing in your hand.");
            return;
        }

        player.getItemInHand().setAmount(64);
        player.updateInventory();
        player.sendMessage(CC.GREEN + "You gave yourself more of the item in your hand.");
    }

}
