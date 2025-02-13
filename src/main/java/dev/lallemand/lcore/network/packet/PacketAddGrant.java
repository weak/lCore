package dev.lallemand.lcore.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import dev.lallemand.lcore.profile.grant.Grant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import dev.lallemand.lcore.util.json.JsonChain;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketAddGrant implements Packet {

    private UUID playerUuid;
    private Grant grant;

    @Override
    public int id() {
        return 1;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("playerUuid", playerUuid.toString())
            .add("grant", Grant.SERIALIZER.serialize(grant))
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerUuid = UUID.fromString(jsonObject.get("playerUuid").getAsString());
        grant = Grant.DESERIALIZER.deserialize(jsonObject.get("grant").getAsJsonObject());
    }

}
