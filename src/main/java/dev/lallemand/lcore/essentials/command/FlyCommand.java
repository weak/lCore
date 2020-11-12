package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "fly", permission = "lcore.fly")
public class FlyCommand {

    public void excute(Player player) {
        player.setAllowFlight(!player.getAllowFlight());
        player.sendMessage(CC.translate("&eFlight mode " + (player.getAllowFlight() ? "&aenabled" : "&cdisabled")));
    }

    public void excute(Player player, Player target) {
        target.setAllowFlight(!target.getAllowFlight());
        target.sendMessage(CC.translate("&eFlight mode " + (target.getAllowFlight() ? "&aenabled" : "&cdisabled")));
        player.sendMessage(CC.translate("&eFlight mode for " + target.getName() +
            (target.getAllowFlight() ? " &aenabled" : " &cdisabled")));
    }

}
