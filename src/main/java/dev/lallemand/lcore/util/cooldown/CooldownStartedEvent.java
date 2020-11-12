package dev.lallemand.lcore.util.cooldown;

import lombok.Getter;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.events.PlayerBase;

public class CooldownStartedEvent extends PlayerBase {

    @Getter
    private Cooldown cooldown;

    public CooldownStartedEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }
}