package dev.lallemand.lcore.profile.prefix.commands;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.prefix.Prefix;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = {"unsetprefix", "removeprefix"}, permission = "lcore.prefix.setprefix")
public class UnSetPrefixCommand {
    public void execute(CommandSender player, @CPL("prefix_name") String name, @CPL("target") Profile target) {
        Prefix prefix = Prefix.getPrefixByName(name);
        if (prefix == null) {
            player.sendMessage(CC.RED + "A prefix type with that name could not be found.");
            return;
        }
        target.getPermissions().remove(prefix.getPermission());
        target.updatePermissions();
        target.setPrefix(null);
        target.save();
        target.updateDisplayName();
        player.sendMessage(CC.GREEN + "You updated " + CC.RESET + target.getName() + CC.GREEN + "'s " + " prefix to: " + prefix.getName());
    }
}
