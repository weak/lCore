package dev.lallemand.lcore.profile.punishment.listener;

import dev.lallemand.lcore.profile.punishment.procedure.PunishmentProcedureStage;
import dev.lallemand.lcore.profile.punishment.procedure.PunishmentProcedureType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.network.packet.PacketBroadcastPunishment;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.punishment.Punishment;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import dev.lallemand.lcore.profile.punishment.procedure.PunishmentProcedure;
import dev.lallemand.lcore.util.callback.TypeCallback;
import dev.lallemand.lcore.util.menu.menus.ConfirmMenu;
import dev.lallemand.lcore.util.string.CC;

public class PunishmentListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("lcore.staff.grant")) return;

        PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(event.getPlayer());

        if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("cancel") || event.getMessage().equalsIgnoreCase("cancelban")) {
                PunishmentProcedure.getProcedures().remove(procedure);
                event.getPlayer().sendMessage(CC.RED + "You have cancelled the punishment procedure.");
                return;
            }

            if (procedure.getType() == PunishmentProcedureType.PARDON) {
                new ConfirmMenu(CC.YELLOW + "Pardon this punishment?", (TypeCallback<Boolean>) data -> {
                    if (data) {
                        procedure.getPunishment().setRemovedBy(event.getPlayer().getUniqueId());
                        procedure.getPunishment().setRemovedAt(System.currentTimeMillis());
                        procedure.getPunishment().setRemovedReason(event.getMessage());
                        procedure.getPunishment().setRemoved(true);
                        procedure.finish();

                        event.getPlayer().sendMessage(CC.GREEN + "The punishment has been removed.");
                    } else {
                        procedure.cancel();
                        event.getPlayer().sendMessage(CC.RED + "You did not confirm to pardon the punishment.");
                    }
                }, true) {
                    @Override
                    public void onClose(Player player) {
                        if (!isClosedByMenu()) {
                            procedure.cancel();
                            event.getPlayer().sendMessage(CC.RED + "You did not confirm to pardon the punishment.");
                        }
                    }
                }.openMenu(event.getPlayer());
            } else if (procedure.getType() == PunishmentProcedureType.ADD) {
                String reason = event.getMessage();
                Profile profile = procedure.getRecipient();

                Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
                    reason, Long.MAX_VALUE, Bukkit.getServerName());

                punishment.setAddedBy(event.getPlayer().getUniqueId());
                punishment.setTargetID(procedure.getRecipient().getUuid());
                punishment.save();
                profile.getPunishments().add(punishment);
                profile.save();

                lCore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, event.getPlayer().getName(),
                    profile.getColoredUsername(), profile.getUuid(), true, Bukkit.getServerName()));

                Player target = profile.getPlayer();

                if (target != null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            target.kickPlayer(punishment.getKickMessage());
                        }
                    }.runTask(lCore.get());
                }
                procedure.finish();
            }
        }
    }

}
