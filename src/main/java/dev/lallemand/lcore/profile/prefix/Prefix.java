package dev.lallemand.lcore.profile.prefix;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.network.packet.PacketPrefixDelete;
import dev.lallemand.lcore.network.packet.PacketPrefixUpdate;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.string.CC;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Prefix {
    @Getter private static MongoCollection<Document> collection = lCore.get().getMongoDatabase().getCollection("prefixes");

    @Getter
    private static List<Prefix> prefixes = Lists.newArrayList();

    private String name;
    @Setter private String prefix = "";
    @Setter private Category category;

    public Prefix(String name, Category category) {
        this.name = name;
        this.category = category;
        prefixes.add(this);
    }

    public static Prefix getPrefixByName(String name) {
        for (final Prefix prefix : prefixes) {
            if (prefix.getName().equalsIgnoreCase(name)) {
                return prefix;
            }
        }
        return null;
    }


    public static void load() {
        try (final MongoCursor<Document> cursor = collection.find().iterator()) {
            cursor.forEachRemaining(document -> {
                Prefix prefix = new Prefix(document.getString("name"), Category.valueOf(document.getString("category")));
                prefix.setPrefix(CC.translate(document.getString("prefix")));
            });
        }
    }

    public String getPermission() {
        return "lcore.prefix." + this.name;
    }

    public void save() {
        Document document = new Document();
        document.put("name", this.name);
        document.put("prefix", this.prefix);
        document.put("category", this.category.name());
        collection.replaceOne(Filters.eq("name", this.name), document, new ReplaceOptions().upsert(true));
    }

    public void update() {
        final Document document = collection.find(Filters.eq("name", this.name)).first();
        if (document == null) {
            Prefix.prefixes.remove(this);
            return;
        }
        this.prefix = CC.translate(document.getString("prefix"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getPrefix() != null && profile.getPrefix().equals(this)) {
                profile.updateDisplayName();
                player.sendMessage(CC.GREEN + "Your prefix has been updated.");
            }
        }
        lCore.get().getPidgin().sendPacket(new PacketPrefixUpdate(this.getName()));
    }

    public void delete() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getPrefix() != null && profile.getPrefix().equals(this)) {
                profile.setPrefix(null);
                profile.updateDisplayName();
                player.sendMessage(CC.GREEN + "Your prefix has been reset as it was deleted.");
            }
        }
        getPrefixes().remove(this);
        collection.deleteOne(Filters.eq("name", this.name));
        lCore.get().getPidgin().sendPacket(new PacketPrefixDelete(this.getName()));
    }

    public static List<Prefix> getPrefixesByCategory(Category category) {
        return prefixes.stream().filter(prefix1 -> prefix1.getCategory() == category).collect(Collectors.toList());
    }
}
