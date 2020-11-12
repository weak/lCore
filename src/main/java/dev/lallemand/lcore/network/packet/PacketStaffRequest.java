package dev.lallemand.lcore.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import dev.lallemand.lcore.util.json.JsonChain;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketStaffRequest implements Packet {

    private String sentBy;
    private String reason;
    private String serverName;

    @Override
    public int id() {
        return 12;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("sentBy", sentBy)
            .addProperty("reason", reason)
            .addProperty("serverName", serverName)
            .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        sentBy = object.get("sentBy").getAsString();
        reason = object.get("reason").getAsString();
        serverName = object.get("serverName").getAsString();
    }

}
