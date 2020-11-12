package dev.lallemand.lcore.profile.punishment.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.network.packet.PacketBroadcastPunishment;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.punishment.Punishment;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import dev.lallemand.lcore.profile.punishment.menu.BanMenu;
import dev.lallemand.lcore.util.duration.Duration;
import dev.lallemand.lcore.util.string.CC;

@CommandMeta(label = "ban", permission = "lcore.staff.ban", async = true, options = "s")
public class BanCommand {

    public void execute(Player sender) {
        sender.sendMessage(CC.GRAY + "/ban <name>");
    }

    public void execute(Player sender, @CPL("player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(CC.RED + "That player is already banned.");
            return;
        }


        new BanMenu(profile).openMenu(sender);

        /*
        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "That duration is not valid.");
            sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
                .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
                reason, duration.getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        lcore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
                profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(lcore.get());
        }*/
    }

    public void execute(CommandSender sender
        , CommandOption option, @CPL("player") Profile profile,
                        @CPL("duration") Duration duration, @CPL("reason") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(CC.RED + "That player is already banned.");
            return;
        }


        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "That duration is not valid.");
            sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
            reason, duration.getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }
        punishment.setTargetID(profile.getUuid());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        lCore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(lCore.get());
        }
    }

    public void execute(CommandSender sender
        , CommandOption option, @CPL("player") Profile profile, @CPL("reason") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(CC.RED + "That player is already banned.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
            reason, new Duration(Long.MAX_VALUE).getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }
        punishment.setTargetID(profile.getUuid());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        lCore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(lCore.get());
        }
    }

    public void execute(CommandSender sender, @CPL("player") Profile profile, @CPL("reason") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(CC.RED + "That player is already banned.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
            reason, new Duration(Long.MAX_VALUE).getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }
        punishment.setTargetID(profile.getUuid());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        lCore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), false, Bukkit.getServerName()));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(lCore.get());
        }
    }

}
