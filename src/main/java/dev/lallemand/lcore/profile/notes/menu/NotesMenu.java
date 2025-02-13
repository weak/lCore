package dev.lallemand.lcore.profile.notes.menu;

import com.google.common.collect.Maps;
import dev.lallemand.lcore.profile.notes.Note;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.item.ItemBuilder;
import dev.lallemand.lcore.util.menu.Button;
import dev.lallemand.lcore.util.menu.pagination.PaginatedMenu;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
public class NotesMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Notes of " + profile.getName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        for (Note note : profile.getNotes()) {
            buttons.put(buttons.size(), new NoteButton(note));
        }
        return buttons;
    }

    @AllArgsConstructor
    private class NoteButton extends Button {

        private Note note;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                .name("&eNote ID&7:&f " + note.getId())
                .lore("&eCreate by&7:&f " + note.getCreateBy())
                .lore("&eNote&7:&f " + note.getNote())
                .lore("&eCreate at&7:&f " + TimeUtil.dateToString(new Date(note.getCreateAt()), "&7"))
                .build();
        }
    }
}
