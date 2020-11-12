package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"tp", "teleport", "tppos"}, permission = "lcore.teleport", autoAddSubCommands = false)
public class TeleportCommand {

    public void execute(Player player, @CPL("target") Profile profileTarget) {
        Player target = Bukkit.getPlayer(profileTarget.getUuid());
        if (target != null) {
            player.teleport(target);
            player.sendMessage(CC.GREEN + "You have been teleported to " + CC.RESET + profileTarget.getActiveRank().getPrefix()
                + target.getName());
            return;
        }
        Location lastLocation = profileTarget.getLastLocation();
        if (lastLocation == null) {
            player.sendMessage(CC.RED + "Last Location not found");
            return;
        }
        player.teleport(lastLocation);
        player.sendMessage(CC.translate("&eYou have been teleported to " + profileTarget.getActiveRank().getPrefix()
            + profileTarget.getName() + " &elast location."));
    }

    public void execute(Player player, @CPL("origin") Player origin, @CPL("target") Player target) {
        origin.teleport(target);
        origin.sendMessage(CC.GREEN + "You have been teleported to " + CC.RESET + target.getName() + CC.GREEN + " by " + CC.RESET + player.getName());
    }

    public void execute(Player player, @CPL("origin") Player origin, @CPL("x") Number x, @CPL("y") Number y, @CPL("z") Number z) {
        origin.teleport(new Location(origin.getWorld(), x.intValue(), y.intValue(), z.intValue()));
        player.sendMessage(CC.GREEN + "You have been teleported to " + CC.RESET + "(" + x.intValue() + "," + y.intValue() + "," + z.intValue() + ")" + CC.GREEN + " by " + CC.RESET + player.getName());
    }

    public void execute(Player player, @CPL("x") Number x, @CPL("y") Number y, @CPL("z") Number z) {
        player.teleport(new Location(player.getWorld(), x.intValue(), y.intValue(), z.intValue()));
        player.sendMessage(CC.GREEN + "You have been teleported to " + CC.RESET + "(" + x.intValue() + "," + y.intValue() + "," + z.intValue() + ")");
    }

}