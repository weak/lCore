package dev.lallemand.lcore.profile.prefix.menu;

import com.google.common.collect.Maps;
import dev.lallemand.lcore.profile.prefix.Category;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.util.item.ItemBuilder;
import dev.lallemand.lcore.util.menu.Button;
import dev.lallemand.lcore.util.menu.Menu;
import dev.lallemand.lcore.util.menu.button.DisplayButton;
import dev.lallemand.lcore.util.string.CC;

import java.util.Map;

public class PrefixMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Chat tags";
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        for (int i = 0; i < 27; i++) {
            buttons.put(i, new DisplayButton(
                new ItemBuilder(Material.STAINED_GLASS_PANE).durability(0).build(),
                true));
        }

        int i = 8;
        for (Category category : Category.values()) {
            buttons.put(i += 2, new CategoryButton(category));
        }

        return buttons;
    }


    @AllArgsConstructor
    public class CategoryButton extends Button {

        private Category category;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.NAME_TAG).name(CC.PINK + category.getName() + " Prefixes").build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            new CategoryMenu(category).openMenu(player);
        }
    }
}
