package dev.lallemand.lcore.profile.punishment.command;

import com.mongodb.client.model.Filters;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.punishment.BanReason;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import dev.lallemand.lcore.util.duration.Duration;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "punishmenttype", permission = "lcore.staff.punishment", async = true)
public class PunishmentCommand {

    public void execute(Player player) {
        player.sendMessage(CC.RED + "Usage:");
        player.sendMessage(CC.RED + "/punishmenttype add <name> <type> <time> <admit>");
        player.sendMessage(CC.RED + "/punishmenttype remove <name>");
    }

    @CommandMeta(label = "add")
    public class AddCommand extends PunishmentCommand {
        public void execute(Player player, @CPL("name") String name,
                            @CPL("type") String type, @CPL("time") String duration, @CPL("admit") Boolean admit) {

            if (Duration.fromString(duration).getValue() == -1) {
                player.sendMessage(CC.RED + "That duration is not valid.");
                player.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
                return;
            }

            PunishmentType punishmentType;
            if (type.equalsIgnoreCase("tempban") && (!duration.contains("permanent") || !duration.contains("perm"))) {
                punishmentType = PunishmentType.TEMP_BAN;
            } else {
                punishmentType = PunishmentType.getByName(type);
                duration = "permanent";
            }

            if (punishmentType == null) {
                player.sendMessage(CC.RED + "Punishment type not found.");
                return;
            }

            BanReason banReason = new BanReason(name, duration, punishmentType, admit);

            banReason.save();
            player.sendMessage(CC.GREEN + "Punishment type create successfully.");
        }
    }

    @CommandMeta(label = "remove")
    public class RemoveCommand extends PunishmentCommand {
        public void execute(Player player, @CPL("name") String name) {

            BanReason banReason = BanReason.getByReason(name);

            if (banReason == null) {
                player.sendMessage(CC.RED + "Punishment type not found.");
                return;
            }

            BanReason.getBanReasons().remove(banReason);
            BanReason.getCollection().deleteOne(Filters.eq("reason", banReason.getReason()));
            player.sendMessage(CC.GREEN + "Punishment type remove successfully.");
        }
    }

}
