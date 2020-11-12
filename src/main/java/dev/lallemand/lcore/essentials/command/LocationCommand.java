package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.LocationUtil;

@CommandMeta(label = "loc", permission = "lcore.loc")
public class LocationCommand {

    public void execute(Player player) {
        player.sendMessage(LocationUtil.serialize(player.getLocation()));
        System.out.println(LocationUtil.serialize(player.getLocation()));
    }

}
