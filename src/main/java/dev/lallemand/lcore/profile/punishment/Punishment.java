package dev.lallemand.lcore.profile.punishment;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.string.CC;

import java.util.UUID;

@Getter
@Setter
public class Punishment {

    public static PunishmentJsonSerializer SERIALIZER = new PunishmentJsonSerializer();
    public static PunishmentJsonDeserializer DESERIALIZER = new PunishmentJsonDeserializer();

    @Getter private static MongoCollection<Document> collection = lCore.get().getMongoDatabase().getCollection("punishments");

    private UUID uuid = UUID.randomUUID();
    private UUID targetID;
    private final PunishmentType type;
    final private long addedAt;
    private final String addedReason;
    final private long duration;
    private UUID addedBy;
    private UUID removedBy;
    private long removedAt;
    private String removedReason;
    private boolean removed;
    private String serverName;

    public Punishment(PunishmentType type, long addedAt, String addedReason, long duration, String serverName) {
        this.type = type;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
        this.serverName = serverName;
    }

    public boolean isPermanent() {
        return type == PunishmentType.BLACKLIST || duration == Long.MAX_VALUE;
    }

    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public String getDurationText() {
        if (removed) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        return TimeUtil.millisToRoundedTime(duration);
    }

    public String getTimeRemaining() {
        if (removed) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        if (hasExpired()) {
            return "Expired";
        }

        return TimeUtil.millisToRoundedTime((addedAt + duration) - System.currentTimeMillis());
    }

    public String getContext() {
        if (!(type == PunishmentType.BAN || type == PunishmentType.MUTE)) {
            return removed ? type.getUndoContext() : type.getContext();
        }

        if (isPermanent()) {
            return (removed ? type.getUndoContext() : "permanently " + type.getContext());
        } else {
            return (removed ? type.getUndoContext() : "temporarily " + type.getContext());
        }
    }

    public void broadcast(String sender, String target, boolean silent) {
        if (silent) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.hasPermission("lcore.staff")) {
                    player.sendMessage(lCore.get().getMainConfig().getString("PUNISHMENTS.BROADCAST_SILENT")
                        .replace("{context}", getContext())
                        .replace("{target}", target)
                        .replace("{sender}", sender)
                        .replace("{serverName}", serverName));
                }
            });
        } else {
            Bukkit.broadcastMessage(lCore.get().getMainConfig().getString("PUNISHMENTS.BROADCAST")
                .replace("{context}", getContext())
                .replace("{target}", target)
                .replace("{sender}", sender)
                .replace("{serverName}", serverName));
        }
    }

    public String getKickMessage() {
        String kickMessage;
        String reason = addedReason;
        if (reason.contains(";")) {
            reason = reason.replace(";", " ");
        }

        if (type == PunishmentType.BAN) {
            kickMessage = lCore.get().getMainConfig().getString("PUNISHMENTS.BAN.KICK");
            String temporary = "";

            if (!isPermanent()) {
                temporary = lCore.get().getMainConfig().getString("PUNISHMENTS.BAN.TEMPORARY");
                temporary = temporary.replace("{time-remaining}", getTimeRemaining());
            }

            kickMessage = kickMessage.replace("{context}", getContext())
                .replace("{temporary}", temporary)
                .replace("{reason}", reason)
                .replace("{serverName}", serverName);
        } else if (type == PunishmentType.KICK) {
            kickMessage = lCore.get().getMainConfig().getString("PUNISHMENTS.KICK.KICK")
                .replace("{context}", getContext())
                .replace("{reason}", reason)
                .replace("{serverName}", serverName);
        } else if (type == PunishmentType.BLACKLIST) {
            kickMessage = lCore.get().getMainConfig().getString("PUNISHMENTS.BLACKLIST")
                .replace("{reason}", reason)
                .replace("{serverName}", serverName);
        } else if (type == PunishmentType.TEMP_BAN) {
            kickMessage = lCore.get().getMainConfig().getString("PUNISHMENTS.BAN.KICK");
            String temporary = lCore.get().getMainConfig().getString("PUNISHMENTS.BAN.TEMPORARY");
            temporary = temporary.replace("{time-remaining}", getTimeRemaining());

            kickMessage = kickMessage.replace("{context}", getContext())
                .replace("{temporary}", temporary)
                .replace("{reason}", reason)
                .replace("{serverName}", serverName);
        } else {
            kickMessage = "&cERROR";
        }

        return CC.translate(kickMessage);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Punishment && ((Punishment) object).uuid.equals(uuid);
    }

    public void save() {
        Document document = new Document();
        document.put("uuid", this.getUuid().toString());
        document.put("type", this.getType().name());
        document.put("targetID", this.getTargetID().toString());
        document.put("addedBy", this.getAddedBy() == null ? null : this.getAddedBy().toString());
        document.put("removedBy", this.getRemovedBy() == null ? null : this.getRemovedBy().toString());
        document.put("addedAt", this.getAddedAt());
        document.put("addedReason", this.getAddedReason());
        document.put("expiration", this.getDuration());
        document.put("removedAt", this.getRemovedAt());
        document.put("removedReason", this.getRemovedReason());
        document.put("removed", this.isRemoved());
        document.put("serverName", this.getServerName());

        collection.replaceOne(Filters.eq("uuid", this.uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

    public void delete() {
        collection.deleteOne(Filters.eq("uuid", this.uuid.toString()));
    }

}
