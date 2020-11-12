package dev.lallemand.lcore.profile.staff.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.string.CC;

import java.util.ArrayList;
import java.util.List;

@CommandMeta(label = "alts", async = true, permission = "lcore.staff.alts")
public class AltsCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        // search profile by IP address, so it loads all the alts if a player is offline
        List<Profile> alts = Profile.getByIpAddress(profile.getCurrentAddress());
        if (alts.size() <= 1) {
            sender.sendMessage(CC.RED + "This player has no known alt accounts.");
        } else {
            sender.sendMessage(CC.GOLD + profile.getName() + "'s Alts " + CC.GRAY + "(" + alts.size() + "): " + CC.RESET + getAltsMessage(alts));
        }
    }

    public static String getAltsMessage(List<Profile> alts) {
        List<String> messages = new ArrayList<>(alts.size());
        for (Profile altProfile : alts) {
            StringBuilder sb = new StringBuilder(CC.YELLOW + CC.BOLD + " * ");
            if (altProfile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                sb.append(CC.DARK_RED);
            } else if (altProfile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                sb.append(CC.RED);
            } else if (altProfile.isOnline()) {
                sb.append(CC.GREEN);
            } else {
                sb.append(CC.WHITE);
            }
            sb.append(altProfile.getName());
            sb.append(CC.GRAY)
                .append(" (")
                .append(altProfile.getLastSeenServer())
                .append(" ")
                .append(TimeUtil.getTimeAgo(altProfile.getLastSeen()))
                .append(")");
            messages.add(sb.toString());
        }
        return "\n" + StringUtils.join(messages, CC.RESET + "\n");
    }

}
