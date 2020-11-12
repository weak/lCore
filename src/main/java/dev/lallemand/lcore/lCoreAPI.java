package dev.lallemand.lcore;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.lallemand.lcore.network.packet.PacketAnticheatAlert;
import dev.lallemand.lcore.network.packet.PacketBroadcastPunishment;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.punishment.Punishment;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import dev.lallemand.lcore.rank.Rank;
import dev.lallemand.lcore.util.cooldown.Cooldown;
import dev.lallemand.lcore.util.string.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class lCoreAPI {

    /**
     * Gets the color of a {@link Player}
     *
     * @param player The {@link Player} player to get the color from
     * @return The {@link ChatColor} based on its active {@link Rank} color
     */
    public static String getColorOfPlayer(Player player) {
        Profile profile = Profile.getByPlayer(player);
        return profile == null ? ChatColor.WHITE.toString() : profile.getActiveRank().getColor();
    }

    /**
     * Gets the colored username of a {@link Player}
     *
     * @param player The {@link Player} player to get the color from
     * @return The colored username of the {@code player} based on its active {@link Rank}
     */
    public static String getColoredName(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        return (profile == null ? ChatColor.WHITE : profile.getActiveRank().getColor()) + player.getName();
    }

    /**
     * Gets the active {@link Rank} of a {@link Player}
     *
     * @param player The {@link Player} to get its rank from
     * @return The {@code player}'s active {@link Rank}
     */
    public static Rank getRankOfPlayer(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        return profile == null ? Rank.getDefaultRank() : profile.getActiveRank();
    }

    /**
     * Gets if the player is in staff mode
     *
     * @param player The {@link Player} to check if its in staff mode
     * @return weather the {@code player} is in staff mode or not
     */
    public static boolean isInStaffMode(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        return profile != null && player.hasPermission("lcore.staff") && profile.getStaffOptions().isStaffModeEnabled();
    }

    /**
     * Gets if the player is in vanish
     *
     * @param player The {@link Player} to check if its in staff mode
     * @return weather the {@code player} is in staff mode or not
     */
    public static boolean isVanish(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        return profile != null && profile.getStaffOptions().isVanish();
    }

    /**
     * Bans a player using the integrated ban system
     *
     * @param player   The {@link Player} to punish
     * @param reason   The reason of the punishment
     * @param duration The duration of the ban
     */
    public static void banPlayer(Player player, String reason, long duration) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null) return;

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(), reason, duration, Bukkit.getServerName());
        profile.getPunishments().add(punishment);
        profile.save();

        lCore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, CC.DARK_RED + "Console", player.getDisplayName(), player.getUniqueId(), false, Bukkit.getServerName()));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(punishment.getKickMessage());
            }
        }.runTask(lCore.get());
    }

    /**
     * Broadcasts an anticheat flag
     *
     * @param player The {@link Player} that has flagged a check
     * @param flag   The flag the {@code player} has checked
     */
    public static void broadcastFlag(Player player, String flag) {
        lCore.get().getPidgin().sendPacket(new PacketAnticheatAlert(player.getName(), flag, ((CraftPlayer) player).getHandle().ping, Bukkit.getServerName()));
    }

    public static Profile getProfile(Player player) {
        return Profile.getByPlayer(player);
    }

    public static Profile getProfile(UUID uuid) {
        return Profile.getByUuid(uuid);
    }

    /**
     * Send player to server in bungee
     *
     * @param player
     * @param server Bungee server name
     */
    public static void sendToServer(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ConnectOther");
        output.writeUTF(player.getName());
        output.writeUTF(server);
        player.sendPluginMessage(lCore.get(), "BungeeCord", output.toByteArray());
    }

    /**
     * Sends a message to the staff channel
     *
     * @param message
     */
    public static void sendStaff(String message) {
        CC.sendStaff(message);
    }


    //COOLDOWN
    public static Cooldown getCooldown(String name) {
        return Cooldown.getCooldownMap().get(name);
    }

    public static List<Cooldown> getPlayerCooldowns(Player player) {
        return Cooldown.getCooldownMap()
            .values()
            .stream()
            .filter(check -> check.getLongMap().containsKey(player.getUniqueId()))
            .collect(Collectors.toList());
    }


}
