package dev.lallemand.lcore.essentials;

import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.Locale;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

import java.util.Arrays;
import java.util.List;

public class EssentialsListener implements Listener {

    private static List<String> BLOCKED_COMMANDS = Arrays.asList(
        "//calc",
        "//eval",
        "//solve",
        "/me",
        "/version",
        "/ver",
        "/setcubo",
        "/bukkit",
        "/minecraft",
        "/spigot"
    );

    @EventHandler
    final void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if ((block.getState() instanceof Skull)) {
                Skull skull = (Skull) block.getState();
                if ((skull.getOwner() != null) && (!skull.getOwner().startsWith("MHF_"))) {
                    event.getPlayer().sendMessage(CC.translate("&eThis is the head of: &f" + skull.getOwner() + "&e."));
                }
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("lcore.sign.colored")) {
            return;
        }

        for (int i = 0; i < event.getLines().length; i++) {
            event.setLine(i, CC.translate(event.getLine(i)));
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (lCore.get().getEssentials().isDonorOnly() && !Profile.getByPlayer(player).getActiveRank().hasPermission("lcore.donorjoin")) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(Locale.KICK_CLOSED_TESTING.format(player.getName()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = (event.getMessage().startsWith("/") ? "" : "/") + event.getMessage();

        for (String blockedCommand : BLOCKED_COMMANDS) {
            if (message.split("[:( )]")[0].toLowerCase().equals(blockedCommand) && !event.getPlayer().isOp()) {
                player.sendMessage(CC.WHITE + "Unknown command. Type \"/help\" for help.");
                event.setCancelled(true);
                return;
            }
        }
    }

}
