package dev.lallemand.lcore.util.cooldown;

import lombok.Getter;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.events.BaseEvent;
import dev.lallemand.lcore.util.events.PlayerBase;

@Getter
public class CooldownExpiredEvent extends PlayerBase {

    private Cooldown cooldown;
    private boolean forced;

    CooldownExpiredEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }

    public BaseEvent setForced(boolean forced) {
        this.forced = forced;
        return this;
    }
}