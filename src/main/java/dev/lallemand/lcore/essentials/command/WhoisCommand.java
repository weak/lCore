package dev.lallemand.lcore.essentials.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import dev.lallemand.lcore.profile.staff.command.AltsCommand;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.string.CC;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CommandMeta(label = {"whois", "who", "profile"}, permission = "lcore.whois", async = true)
public class WhoisCommand {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }
        showProfile(player, profile);
    }

    public void execute(CommandSender sender, @CPL("playerName") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }
        showProfile(sender, profile);
    }

    private void showProfile(CommandSender sender, Profile profile) {
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.GOLD + "Profile of " + CC.GREEN + profile.getName());
        sender.sendMessage(CC.GOLD + "UUID: " + CC.RESET + profile.getUuid().toString());
        sender.sendMessage(CC.GOLD + "Country: " + CC.RESET + profile.getCountryName() + CC.GRAY + " (" + profile.getCountryCode() + ")");
        sender.sendMessage(CC.GOLD + "Rank: " + CC.RESET + profile.getActiveRank().getColor() + profile.getActiveRank().getDisplayName());
        sender.sendMessage(CC.GOLD + "Status: " + getProfileStatus(profile));
        sender.sendMessage(CC.GOLD + "First Join: " + CC.RESET + new Timestamp(profile.getFirstSeen()).toLocalDateTime().format(formatter));
        sender.sendMessage(CC.GOLD + "Last Server: " + CC.RESET + profile.getLastSeenServer() + CC.GRAY + " (" + TimeUtil.getTimeAgo(profile.getLastSeen()) + ")");
        if (sender.hasPermission("lcore.staff")) {
            List<Profile> alts = Profile.getByIpAddress(profile.getCurrentAddress());
            if (alts.size() > 1) {
                sender.sendMessage(CC.GOLD + "Alts " + CC.GRAY + "(" + alts.size() + "): " + CC.RESET + AltsCommand.getAltsMessage(alts));
            }
        }
        sender.sendMessage(CC.CHAT_BAR);
    }

    private String getProfileStatus(Profile profile) {
        if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
            return CC.DARK_RED + "Blacklisted";
        } else if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            return CC.DARK_RED + "Banned";
        } else if (profile.isOnline()) {
            return CC.GREEN + "Online";
        }
        return CC.RED + "Offline";
    }

}

