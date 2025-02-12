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
public class PacketStaffSwitchServer implements Packet {

    private String playerName;
    private String fromServerName;
    private String toServerName;

    @Override
    public int id() {
        return 9;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("playerName", playerName)
            .addProperty("fromServerName", fromServerName)
            .addProperty("toServerName", toServerName)
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerName = jsonObject.get("playerName").getAsString();
        fromServerName = jsonObject.get("fromServerName").getAsString();
        toServerName = jsonObject.get("toServerName").getAsString();
    }

}
