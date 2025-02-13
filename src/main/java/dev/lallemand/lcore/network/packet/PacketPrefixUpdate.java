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
public class PacketPrefixUpdate implements Packet {

    private String name;

    @Override
    public int id() {
        return 20;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("name", name)
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        name = jsonObject.get("name").getAsString();
    }
}
