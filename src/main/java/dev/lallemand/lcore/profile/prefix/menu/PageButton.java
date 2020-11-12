package dev.lallemand.lcore.profile.prefix.menu;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.util.item.ItemBuilder;
import dev.lallemand.lcore.util.menu.Button;
import dev.lallemand.lcore.util.menu.pagination.PaginatedMenu;

import java.util.Arrays;

@AllArgsConstructor
public class PageButton extends Button {

    private int mod;
    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.GREEN + "Next Page")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "Click here to jump",
                        ChatColor.YELLOW + "to the next page."
                    ))
                    .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.RED + "Next Page")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "There is no available",
                        ChatColor.YELLOW + "next page."
                    ))
                    .build();
            }
        } else {
            if (hasPrevious(player)) {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.GREEN + "Previous Page")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "Click here to jump",
                        ChatColor.YELLOW + "to the previous page."
                    ))
                    .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.RED + "Previous Page")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "There is no available",
                        ChatColor.YELLOW + "previous page."
                    ))
                    .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod);
            }
        } else {
            if (hasPrevious(player)) {
                this.menu.modPage(player, this.mod);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return this.menu.getPages(player) >= pg;
    }

    private boolean hasPrevious(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0;
    }

}