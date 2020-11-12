package dev.lallemand.lcore.util.menu.pagination;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import dev.lallemand.lcore.util.menu.Button;
import dev.lallemand.lcore.util.menu.Menu;
import dev.lallemand.lcore.util.menu.button.BackButton;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ViewAllPagesMenu extends Menu {

    @NonNull
    @Getter
    PaginatedMenu menu;

    @Override
    public String getTitle(Player player) {
        return "Jump to page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(menu));

        int index = 10;

        for (int i = 1; i <= menu.getPages(player); i++) {
            buttons.put(index++, new JumpToPageButton(i, menu, menu.getPage() == i));

            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

}
