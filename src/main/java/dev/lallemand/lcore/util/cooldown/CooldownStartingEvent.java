package dev.lallemand.lcore.util.cooldown;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.events.PlayerBase;

@Getter
public class CooldownStartingEvent extends PlayerBase {

    private Cooldown cooldown;
    @Setter
    private String reason;

    public CooldownStartingEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }

}