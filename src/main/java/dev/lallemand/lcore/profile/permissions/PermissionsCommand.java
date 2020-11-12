package dev.lallemand.lcore.profile.permissions;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "perms", permission = "lcore.perms")
public class PermissionsCommand {

    public void execute(CommandSender sender) {
        sender.sendMessage(CC.translate("&7/perms add (name) (permission)"));
        sender.sendMessage(CC.translate("&7/perms remove (name) (permission)"));
    }

    @CommandMeta(label = "add", permission = "lcore.perms")
    public class AddCommand extends PermissionsCommand {
        public void execute(CommandSender sender, @CPL("target") Profile target, @CPL("permission") String permission) {
            target.getPermissions().add(permission);
            target.updatePermissions();
            sender.sendMessage(CC.translate("&aPermissions added successfully."));
        }
    }

    @CommandMeta(label = "remove", permission = "lcore.perms")
    public class RemoveCommand extends PermissionsCommand {
        public void execute(CommandSender sender, @CPL("target") Profile target, @CPL("permission") String permission) {
            target.getPermissions().remove(permission);
            target.updatePermissions();
            sender.sendMessage(CC.translate("&cPermissions removed successfully."));
        }
    }

}
