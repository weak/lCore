package dev.lallemand.lcore.profile.staff.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.events.BaseEvent;

import java.util.List;

@AllArgsConstructor
@Getter
public class PlayerVisibilityChangeEvent extends BaseEvent {
    private boolean hide;
    private Profile profile;
    private List<Player> players;
}
