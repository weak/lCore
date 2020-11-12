package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "invmodify", permission = "lcore.invmodify")
public class InvModify {

    public void excute(Player player, Player target) {
        player.openInventory(target.getInventory());
    }

}
