package dev.lallemand.lcore.profile.option.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.profile.option.menu.ProfileOptionButton;
import dev.lallemand.lcore.util.events.BaseEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class OptionsOpenedEvent extends BaseEvent {

    private final Player player;
    private List<ProfileOptionButton> buttons = new ArrayList<>();

}
