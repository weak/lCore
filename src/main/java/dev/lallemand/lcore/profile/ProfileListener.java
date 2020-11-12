package dev.lallemand.lcore.profile;

import dev.lallemand.lcore.profile.punishment.Punishment;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.cache.RedisPlayerData;
import dev.lallemand.lcore.network.packet.PacketStaffChat;
import dev.lallemand.lcore.network.packet.PacketStaffJoinNetwork;
import dev.lallemand.lcore.network.packet.PacketStaffLeaveNetwork;
import dev.lallemand.lcore.util.TaskUtil;
import dev.lallemand.lcore.util.string.CC;

import java.util.Iterator;

public class ProfileListener implements Listener {

    private static lCore lcore = lCore.get();

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST)) {
            event.setKickMessage(lcore.getMainConfig().getString("GLOBAL_WHITELIST.KICK_MAINTENANCE"));
            return;
        }

        Player player = Bukkit.getPlayer(event.getUniqueId());

        // Need to check if player is still logged in when receiving another login attempt
        // This happens when a player using a custom client can access the server list while in-game (and reconnecting)
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.RED + "You tried to login too quickly after disconnecting.\nTry again in a few seconds.");
            lcore.getServer().getScheduler().runTask(lcore, () -> player.kickPlayer(CC.RED + "Duplicate login kick"));
            return;
        }

        Profile profile = null;

        try {
            profile = new Profile(event.getName(), event.getUniqueId());

            if (!profile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
                return;
            }

            if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.BLACKLIST));
                return;
            }

            if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.BAN));
                return;
            }
            if (profile.getActivePunishmentByType(PunishmentType.TEMP_BAN) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.TEMP_BAN));
                return;
            }

            profile.setName(event.getName());

            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }
            profile.setLastSeen(System.currentTimeMillis());
            profile.setLastSeenServer(Bukkit.getServerName());
            profile.setOnline(true);

            if (profile.getCurrentAddress() == null) {
                profile.setCurrentAddress(event.getAddress().getHostAddress());
            } else if (!profile.getCurrentAddress().equals(event.getAddress().getHostAddress())) {
                profile.setCurrentAddress(event.getAddress().getHostAddress());
                profile.setAuthenticated(false);
            }

            if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
                profile.getIpAddresses().add(event.getAddress().getHostAddress());
            }

            for (Profile alt : Profile.getByIpAddress(event.getAddress().getHostAddress())) {
                profile.addAlt(alt);
                alt.addAlt(profile);
                if (profile != alt) {
                    alt.save();
                }
                if (alt.getActivePunishmentByType(PunishmentType.BAN) != null) {
                    handleBan(event, alt.getActivePunishmentByType(PunishmentType.BAN));
                    return;
                }
                if (alt.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                    handleBan(event, alt.getActivePunishmentByType(PunishmentType.BLACKLIST));
                    return;
                }
            }


            profile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (profile == null || !profile.isLoaded()) {
            event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }

        Profile.getProfiles().put(profile.getUuid(), profile);

        RedisPlayerData playerData = new RedisPlayerData(event.getUniqueId(), event.getName());
        playerData.setLastAction(RedisPlayerData.LastAction.JOINING_SERVER);
        playerData.setLastSeenServer(Bukkit.getServerName());
        playerData.setLastSeenAt(System.currentTimeMillis());

        lcore.getRedisCache().updatePlayerData(playerData);
        lcore.getRedisCache().updateNameAndUUID(event.getName(), event.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL) && event.getPlayer().hasPermission("lcore.utils.bypassfull")) {
            event.setResult(PlayerLoginEvent.Result.ALLOWED);
            event.allow();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.setupBukkitPlayer(player);

        if (!player.hasPermission("lcore.staff")) return;

        TaskUtil.runLater(() -> {
            lcore.getPidgin().sendPacket(new PacketStaffJoinNetwork(profile.getColoredUsername(), Bukkit.getServerName()));
        }, 5L);
    }

    public static void leavePlayer(Player player, boolean disable) {

        Profile profile = Profile.getProfiles().remove(player.getUniqueId());
        profile.setLastSeen(System.currentTimeMillis());
        profile.setLastSeenServer(Bukkit.getServerName());
        profile.setOnline(false);

        if (profile.isFrozen()) {
            String format = "{player} &chas left server while being freeze.".replace("{player}", profile.getColoredUsername());
            CC.sendStaff(format);
        }

        if (profile.isLoaded()) {
            if (profile.getStaffOptions().isStaffModeEnabled()) {
                if (profile.getArmor() != null) {
                    player.getInventory().setArmorContents(profile.getArmor().toArray(new ItemStack[profile.getArmor().size()]));
                } else {
                    player.getInventory().setArmorContents(new ItemStack[4]);
                }
                if (profile.getInventory() != null) {
                    player.getInventory().setContents(profile.getInventory().toArray(new ItemStack[profile.getInventory().size()]));
                } else {
                    player.getInventory().setContents(new ItemStack[36]);
                }
                player.setGameMode(GameMode.SURVIVAL);
                player.saveData();
            }

            profile.setLastLocation(player.getLocation());

            if (disable) {
                profile.save();
            } else {
                TaskUtil.runAsync(profile::save);
            }
        }

        if (!disable) {
            RedisPlayerData playerData = new RedisPlayerData(player.getUniqueId(), player.getName());
            playerData.setLastAction(RedisPlayerData.LastAction.LEAVING_SERVER);
            playerData.setLastSeenServer(Bukkit.getServerName());
            playerData.setLastSeenAt(System.currentTimeMillis());

            lcore.getRedisCache().updatePlayerData(playerData);

            if (!player.hasPermission("lcore.staff")) return;

            lcore.getPidgin().sendPacket(new PacketStaffLeaveNetwork(profile.getColoredUsername(), Bukkit.getServerName()));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        leavePlayer(event.getPlayer(), false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("lcore.staff")) return;

        Profile profile = Profile.getByUuid(player.getUniqueId());
        /*if (!profile.isAuthenticated()) {
            player.sendMessage(Locale.SECURITY_AUTH_REQUIRED.format());
            event.setCancelled(true);
            return;
        }*/

        Iterator<Player> recipents = event.getRecipients().iterator();

        while (recipents.hasNext()) {
            Player recipient = recipents.next();
            Profile profileRecipient = Profile.getByUuid(recipient.getUniqueId());
            if (profileRecipient.getIgnoreList().contains(player.getUniqueId())) {
                recipents.remove();
            }
        }

        event.getRecipients().forEach(recipient -> {
            Profile profileRecipient = Profile.getByUuid(recipient.getUniqueId());
            if (profileRecipient.getIgnoreList().contains(player.getUniqueId())) {
                event.getRecipients().remove(recipient);
            }
        });

        if (profile.getStaffOptions().isStaffChatModeEnabled()) {
            lcore.getPidgin().sendPacket(new PacketStaffChat(
                profile.getColoredUsername(), Bukkit.getServerName(), event.getMessage()));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("lcore.staff") || event.getMessage().startsWith("/auth")) return;

        Profile profile = Profile.getByUuid(player.getUniqueId());
        /*if (!profile.isAuthenticated()) {
            player.sendMessage(Locale.SECURITY_AUTH_REQUIRED.format());
            event.setCancelled(true);
        }*/
    }

    /*@EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())
            return;

        Profile profile = Profile.getByUuid(e.getPlayer().getUniqueId());
        if (profile.isFrozen()) {
            e.getPlayer().teleport(e.getFrom());
            List<String> messages = Locale.STAFF_FREEZE.formatLines();
            for (String message : messages) {
                e.getPlayer().sendMessage(message);
            }
        }
    }*/

    private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(punishment.getKickMessage());
    }

}
