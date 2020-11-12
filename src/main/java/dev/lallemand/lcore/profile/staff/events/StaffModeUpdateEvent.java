package dev.lallemand.lcore.profile.staff.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.events.BaseEvent;

@AllArgsConstructor
@Getter
public class StaffModeUpdateEvent extends BaseEvent {

    Player player;
    boolean toggle;
}
