package dev.lallemand.lcore.profile.prefix.commands;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.prefix.Category;
import dev.lallemand.lcore.profile.prefix.Prefix;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.prefix.menu.PrefixMenu;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "prefix")
public class PrefixCommand {

    public void execute(Player player) {
        new PrefixMenu().openMenu(player);
    }

    @CommandMeta(label = "help", permission = "lcore.prefix.command")
    public class HelpCommand extends PrefixCommand {
        public void execute(Player player) {
            player.sendMessage(CC.translate("&7/prefix create (name)"));
            player.sendMessage(CC.translate("&7/prefix delete (name)"));
            player.sendMessage(CC.translate("&7/prefix prefix (name) (prefix)"));
            player.sendMessage(CC.translate("&7/setprefix (player) (prefix)"));
        }
    }

    @CommandMeta(label = "create", permission = "lcore.prefix.command")
    public class CreateCommand extends PrefixCommand {
        public void execute(Player player, @CPL("prefix_name") String name, @CPL("category") String categoryName) {
            if (Prefix.getPrefixByName(name) != null) {
                player.sendMessage(CC.RED + "A prefix with that name already exists.");
                return;
            }
            Category category = Category.getByName(categoryName);
            if (category == null) {
                player.sendMessage(CC.RED + "Category not found.");
                return;
            }
            final Prefix prefix = new Prefix(name, category);
            prefix.save();
            player.sendMessage(CC.GREEN + "Created new prefix: " + prefix.getName());
        }
    }

    @CommandMeta(label = "delete", permission = "lcore.prefix.command")
    public class DeleteCommand extends PrefixCommand {
        public void execute(Player player, @CPL("prefix") String name) {
            Prefix prefix = Prefix.getPrefixByName(name);
            if (prefix == null) {
                player.sendMessage(CC.RED + "A prefix type with that name could not be found.");
                return;
            }

            prefix.delete();
            player.sendMessage(CC.GREEN + "Removed " + prefix.getName());
        }
    }

    @CommandMeta(label = "prefix", permission = "lcore.prefix.command")
    public class AddPrefixCommand extends PrefixCommand {
        public void execute(Player player, @CPL("prefix_name") String name, @CPL("prefix") String prefixDisplay) {
            Prefix prefix = Prefix.getPrefixByName(name);
            if (prefix == null) {
                player.sendMessage(CC.RED + "A prefix type with that name could not be found.");
                return;
            }
            prefix.setPrefix(CC.translate(prefixDisplay));
            prefix.save();
            player.sendMessage(CC.GREEN + "Updated prefix of " + prefix.getName() + CC.GREEN + " to: " + prefix.getPrefix() + player.getName());
        }
    }

    @CommandMeta(label = "category", permission = "lcore.prefix.command")
    public class CategoryCommand extends PrefixCommand {
        public void execute(Player player, @CPL("prefix_name") String name, @CPL("category") String categoryName) {
            Prefix prefix = Prefix.getPrefixByName(name);
            if (prefix == null) {
                player.sendMessage(CC.RED + "A prefix type with that name could not be found.");
                return;
            }
            Category category = Category.getByName(categoryName);
            if (category == null) {
                player.sendMessage(CC.RED + "Category not found.");
                return;
            }
            prefix.setCategory(category);
            prefix.save();
            player.sendMessage(CC.GREEN + "Updated category of " + prefix.getName() + " to: " + category.getName());
        }
    }

    @CommandMeta(label = "list", permission = "lcore.prefix.command")
    public class ListCommand extends PrefixCommand {

        public void execute(Player player, @CPL("target") Player target) {
            player.sendMessage(CC.translate("&7Prefix list of " + target.getName() + ": "));
            Prefix.getPrefixes().forEach(prefix -> {
                if (target.hasPermission(prefix.getPermission())) {
                    player.sendMessage(CC.translate(prefix.getName()));
                }
            });
        }

    }
}
