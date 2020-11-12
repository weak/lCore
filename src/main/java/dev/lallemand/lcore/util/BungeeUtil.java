package dev.lallemand.lcore.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;

public class BungeeUtil {

    /**
     * Connects a player to said subserver.
     *
     * @param player     the player you want to teleport.
     * @param serverName the name of server to connect to, as defined in BungeeCord config.yml.
     */
    public static void connect(Player player, String serverName) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);
        player.sendPluginMessage(lCore.get(), "BungeeCord", output.toByteArray());
    }

}
