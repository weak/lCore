package dev.lallemand.lcore.profile.punishment.command;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCursor;
import com.qrakn.honcho.command.CommandMeta;
import dev.lallemand.lcore.profile.punishment.Punishment;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.hastebin.HasteBin;
import dev.lallemand.lcore.util.string.CC;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@CommandMeta(label = "banlist", permission = "lcore.staff.banlist", async = true)
public class BanListCommand {

    public void execute(CommandSender sender) {
        Map<UUID, Punishment> punishments = Maps.newHashMap();

        try (MongoCursor<Document> cursor = Profile.getCollection().find().iterator()) {
            cursor.forEachRemaining(document -> {
                UUID uuid = UUID.fromString(document.getString("uuid"));
                if (document.getString("punishments") != null) {
                    JsonArray punishmentList = new JsonParser().parse(document.getString("punishments")).getAsJsonArray();

                    for (JsonElement punishmentData : punishmentList) {
                        // Transform into a Grant object
                        Punishment punishment = Punishment.DESERIALIZER.deserialize(punishmentData.getAsJsonObject());

                        if (punishment != null && !punishment.isRemoved() && !punishment.hasExpired()) {
                            punishments.put(uuid, punishment);
                        }
                    }
                }
            });
        }

        if (punishments.isEmpty()) {
            sender.sendMessage(CC.translate("&cNo punishments found."));
            return;
        }
        StringBuilder string = new StringBuilder("Ban list (" + punishments.size() + "): \n");
        punishments.forEach((uuid, punishment) -> {
            Profile profile = Profile.getByUuid(uuid);

            String addedBy = "Console";

            if (punishment.getAddedBy() != null) {
                try {
                    Profile addedByProfile = Profile.getByUuid(punishment.getAddedBy());
                    addedBy = addedByProfile.getName();
                } catch (Exception e) {
                    addedBy = "Could not fetch...";
                }
            }

            string.append("Name: ").append(profile.getName())
                .append(", Type: ").append(punishment.getType())
                .append(", Duration: ").append(punishment.getDurationText())
                .append(", Issued by: ").append(addedBy)
                .append(", Reason: ").append(punishment.getAddedReason())
                .append(", Added At: ").append(TimeUtil.dateToString(new Date(punishment.getAddedAt()), null))
                .append(", Server: ").append(Bukkit.getServerName())
                .append("\n");
        });

        for (String endpoint : HasteBin.ENDPOINTS) {
            try {
                String pasteURL = HasteBin.paste(string.toString(), endpoint);
                sender.sendMessage(CC.translate("&eBan List URL:&7 " + pasteURL));
                return;
            } catch (IOException e) {
                continue;
            }
        }
        sender.sendMessage(CC.translate("&c(Uploads failed)"));
    }
}
