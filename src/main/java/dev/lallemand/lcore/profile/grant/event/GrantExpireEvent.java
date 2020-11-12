package dev.lallemand.lcore.profile.grant.event;

import dev.lallemand.lcore.profile.grant.Grant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.events.BaseEvent;

@AllArgsConstructor
@Getter
public class GrantExpireEvent extends BaseEvent {

    private Player player;
    private Grant grant;

}
