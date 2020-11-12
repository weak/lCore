package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.essentials.menus.InvseeMenu;

@CommandMeta(label = "invsee", permission = "lcore.invsee")
public class InvseeCommand {

    public void excute(Player player, Player target) {
        new InvseeMenu(target).openMenu(player);
    }

}
