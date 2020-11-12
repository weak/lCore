package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "tphere", permission = "lcore.tphere")
public class TPHereCommand {

    public void execute(Player player, @CPL("target") Player target) {
        Profile profile = Profile.getByPlayer(player);
        Profile profileTarget = Profile.getByPlayer(target);
        target.teleport(player);
        target.sendMessage(CC.GREEN + "You have been teleported to " + CC.RESET + profile.getActiveRank().getPrefix()
            + player.getName());
        player.sendMessage(CC.GREEN + "You have teleported " + CC.RESET + profileTarget.getActiveRank().getPrefix()
            + target.getName() + CC.GREEN + " to you");
    }

}
