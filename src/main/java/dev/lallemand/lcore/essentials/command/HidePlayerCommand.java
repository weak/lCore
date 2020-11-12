package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "hideplayer", permission = "lcore.hideplayer")
public class HidePlayerCommand {

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        player.hidePlayer(target);
        player.sendMessage(CC.GOLD + "Hiding " + target.getName() + " from your view");
    }

    public void execute(Player player, Player target1, Player target2) {
        if (target1 == null || target2 == null) {
            player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        target1.hidePlayer(target2);
        player.sendMessage(CC.GOLD + "Hiding " + target2.getName() + " from " + target1.getName() + "'s view");
    }

}
