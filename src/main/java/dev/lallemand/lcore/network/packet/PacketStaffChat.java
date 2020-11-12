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
public class PacketStaffChat implements Packet {

    private String playerName;
    private String serverName;
    private String chatMessage;

    public int id() {
        return 6;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("playerName", playerName)
            .addProperty("serverName", serverName)
            .addProperty("chatMessage", chatMessage)
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerName = jsonObject.get("playerName").getAsString();
        serverName = jsonObject.get("serverName").getAsString();
        chatMessage = jsonObject.get("chatMessage").getAsString();
    }

}
