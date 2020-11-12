package dev.lallemand.lcore.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import dev.lallemand.lcore.profile.punishment.Punishment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import dev.lallemand.lcore.util.json.JsonChain;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketBroadcastPunishment implements Packet {

    private Punishment punishment;
    private String staff;
    private String target;
    private UUID targetUuid;
    private boolean silent;
    private String serverName;

    @Override
    public int id() {
        return 2;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .add("punishment", Punishment.SERIALIZER.serialize(punishment))
            .addProperty("staff", staff)
            .addProperty("target", target)
            .addProperty("targetUuid", targetUuid.toString())
            .addProperty("silent", silent)
            .addProperty("serverName", serverName)
            .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        punishment = Punishment.DESERIALIZER.deserialize(object.get("punishment").getAsJsonObject());
        staff = object.get("staff").getAsString();
        target = object.get("target").getAsString();
        targetUuid = UUID.fromString(object.get("targetUuid").getAsString());
        silent = object.get("silent").getAsBoolean();
        serverName = object.get("serverName").getAsString();
    }

}
