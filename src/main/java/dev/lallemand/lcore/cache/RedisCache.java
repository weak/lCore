package dev.lallemand.lcore.cache;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import dev.lallemand.lcore.lCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class RedisCache {

    private lCore lcore;

    public RedisCache(lCore lcore) {
        this.lcore = lcore;
    }

    /**
     * Get the UUID of a username
     *
     * @param name the name of a Minecraft player.
     * @return The {@link UUID} of the from the Mojang API.
     * @throws IOException if the request was unsuccessful
     */
    private static UUID getFromMojang(String name) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        String[] id = line.split(",");

        String part = id[0];
        part = part.substring(7, 39);

        return UUID.fromString(part.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

    /**
     * Gets the UUID of a username
     *
     * @param name the name of a player.
     * @return The {@link UUID} of the player if found in the Cache or fetched successfully from the Mojang API. Null otherwise.
     */
    public UUID getUuid(String name) {
        if (lcore.getServer().isPrimaryThread()) {
            throw new IllegalStateException("Cannot query on main thread (Redis profile cache)");
        }

        try (Jedis jedis = lcore.getJedisPool().getResource()) {
            String uuid = jedis.hget("uuid-cache:name-to-uuid", name.toLowerCase());

            if (uuid != null) {
                return UUID.fromString(uuid);
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not connect to redis");
            e.printStackTrace();
        }

        try {
            UUID uuid = getFromMojang(name);

            if (uuid != null) {
                updateNameAndUUID(name, uuid);
                return uuid;
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not fetch from Mojang API");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the username and UUID of a player in the cache
     *
     * @param name The username of the player
     * @param uuid The {@link UUID} of the player
     * @throws IllegalStateException if executed in the server primary {@link Thread}
     */
    public void updateNameAndUUID(String name, UUID uuid) {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Cannot query redis on main thread!");
        }

        try (Jedis jedis = lcore.getJedisPool().getResource()) {
            jedis.hset("uuid-cache:name-to-uuid", name.toLowerCase(), uuid.toString());
            jedis.hset("uuid-cache:uuid-to-name", uuid.toString(), name);
        }
    }

    /**
     * Gets the {@link RedisPlayerData} data of a player
     *
     * @param uuid The {@link UUID} of the player
     * @return The {@link RedisPlayerData} of the player
     * @throws IllegalStateException if executed in the server primary {@link Thread}
     */
    public RedisPlayerData getPlayerData(UUID uuid) {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Cannot query redis on main thread!");
        }

        try (Jedis jedis = lcore.getJedisPool().getResource()) {
            String data = jedis.hget("player-data", uuid.toString());

            if (data == null) {
                return null;
            }

            try {
                JsonObject dataJson = new JsonParser().parse(data).getAsJsonObject();
                return new RedisPlayerData(dataJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Updates the {@link RedisPlayerData} of a player
     *
     * @param playerData the {@link RedisPlayerData} of the player
     */
    public void updatePlayerData(RedisPlayerData playerData) {
        try (Jedis jedis = lcore.getJedisPool().getResource()) {
            jedis.hset("player-data", playerData.getUuid().toString(), playerData.getJson().toString());
        }
    }

}
