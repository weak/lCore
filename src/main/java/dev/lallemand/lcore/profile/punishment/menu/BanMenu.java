package dev.lallemand.lcore.profile.punishment.menu;

import com.google.common.collect.Maps;
import dev.lallemand.lcore.profile.punishment.BanReason;
import dev.lallemand.lcore.profile.punishment.Punishment;
import dev.lallemand.lcore.profile.punishment.PunishmentType;
import dev.lallemand.lcore.profile.punishment.procedure.PunishmentProcedure;
import dev.lallemand.lcore.profile.punishment.procedure.PunishmentProcedureStage;
import dev.lallemand.lcore.profile.punishment.procedure.PunishmentProcedureType;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.lallemand.lcore.lCore;
import dev.lallemand.lcore.network.packet.PacketBroadcastPunishment;
import dev.lallemand.lcore.profile.Profile;
import dev.lallemand.lcore.profile.prefix.menu.PageButton;
import dev.lallemand.lcore.util.TaskUtil;
import dev.lallemand.lcore.util.TimeUtil;
import dev.lallemand.lcore.util.duration.Duration;
import dev.lallemand.lcore.util.item.ItemBuilder;
import dev.lallemand.lcore.util.menu.Button;
import dev.lallemand.lcore.util.menu.button.DisplayButton;
import dev.lallemand.lcore.util.menu.pagination.PaginatedMenu;
import dev.lallemand.lcore.util.string.CC;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BanMenu extends PaginatedMenu {

    Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Ban Panel";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(buttons.size(), new CustomBanButton());
        List<BanReason> reasons = BanReason.getBanReasons().stream().sorted(new BanReasonComparator()).collect(Collectors.toList());
        reasons.forEach(banReason -> buttons.put(buttons.size(), new BanReasonButton(banReason)));
        return buttons;
    }

    private class BanReasonComparator implements Comparator<BanReason> {

        @Override
        public int compare(BanReason o1, BanReason o2) {
            return Integer.compare(o1.getPunishmentType().getWeight(), o2.getPunishmentType().getWeight());
        }
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 27;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        int minIndex = (int) ((double) (page - 1) * getMaxItemsPerPage(player));
        int maxIndex = (int) ((double) (page) * getMaxItemsPerPage(player));
        int topIndex = 0;

        HashMap<Integer, Button> buttons = new HashMap<>();

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();

            if (ind >= minIndex && ind < maxIndex) {
                ind -= (int) ((double) (getMaxItemsPerPage(player)) * (page - 1)) - 18;
                buttons.put(ind, entry.getValue());

                if (ind > topIndex) {
                    topIndex = ind;
                }
            }
        }

        for (int i = 9; i < 18; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 14, " "));
        }

        int highest = 0;

        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        int size = (int) (Math.ceil((highest + 1) / 9D) * 9D);

        for (int i = size; i < size + 9; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 14, " "));
        }

        Map<Integer, Button> global = getGlobalButtons(player);

        if (global != null) {
            for (Map.Entry<Integer, Button> gent : global.entrySet()) {
                buttons.put(gent.getKey(), gent.getValue());
            }
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(0, new DisplayButton(new ItemBuilder(Material.SKULL_ITEM)
            .name("&eBanning &c" + profile.getName())
            .durability(3).skullOwner(profile.getName()).build(), true));
        buttons.put(8, new BackButton());

        buttons.put(3, new PageButton(-1, this));
        buttons.put(4, new PageInfoButton(this));
        buttons.put(5, new PageButton(1, this));


        return buttons;
    }

    public class BackButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.REDSTONE).name("&cClose ban panel").build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            TaskUtil.run(player::closeInventory);
        }
    }

    @AllArgsConstructor
    public class PageInfoButton extends Button {

        private PaginatedMenu menu;

        @Override
        public ItemStack getButtonItem(Player player) {
            int pages = menu.getPages(player);

            return new ItemBuilder(Material.CARPET)
                .durability(5)
                .name("&aPage " + menu.getPage() + "&7/&a" + pages)
                .build();
        }

        @Override
        public boolean shouldCancel(Player player, ClickType clickType) {
            return true;
        }
    }

    @AllArgsConstructor
    public class BanReasonButton extends Button {

        BanReason banReason;

        @Override
        public ItemStack getButtonItem(Player player) {
            String reason = banReason.getReason();
            if (reason.contains(";")) {
                reason = reason.replace(";", " ");
            }
            if (banReason.getPunishmentType() == PunishmentType.TEMP_BAN) {
                Duration duration = Duration.fromString(banReason.getDuration());
                return new ItemBuilder(Material.BOOK)
                    .name("&e" + reason + (banReason.isAdmit() ? " &7(&cAdmitted&7)" : ""))
                    .lore("&cBan Type&f " + banReason.getPunishmentType().getReadable())
                    .lore("&9Time&f " + (duration.isPermanent() ? "Permanent" : TimeUtil.millisToRoundedTime(Duration.fromString(banReason.getDuration()).getValue())))
                    .build();
            } else if (banReason.getPunishmentType() == PunishmentType.BAN && Duration.fromString(banReason.getDuration()).isPermanent()) {
                Duration duration = Duration.fromString(banReason.getDuration());
                return new ItemBuilder(Material.ENCHANTED_BOOK)
                    .name("&e" + reason + (banReason.isAdmit() ? " &7(&cAdmitted&7)" : ""))
                    .lore("&cBan Type&f " + banReason.getPunishmentType().getReadable())
                    .lore("&9Time&f " + (duration.isPermanent() ? "Permanent" : TimeUtil.millisToRoundedTime(Duration.fromString(banReason.getDuration()).getValue())))
                    .glow()
                    .build();
            }
            return new ItemBuilder(Material.BOOK_AND_QUILL)
                .name("&e" + reason)
                .lore("&cBan Type&f " + banReason.getPunishmentType().getReadable())
                .lore("&9Time&f Permanent")
                .glow()
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            String reason = banReason.getReason();
            if (banReason.isAdmit()) {
                reason = reason + " (Admitted)";
            }
            if (reason.contains(";")) {
                reason = reason.replace(";", " ");
            }

            if (banReason.getPunishmentType() == PunishmentType.TEMP_BAN && !player.hasPermission("koru.staff.tempban")) {
                player.sendMessage(CC.RED + "No permissions.");
                player.closeInventory();
                return;
            } else if (banReason.getPunishmentType() == PunishmentType.BAN && !player.hasPermission("koru.staff.ban")) {
                player.sendMessage(CC.RED + "No permissions.");
                player.closeInventory();
                return;
            } else if (banReason.getPunishmentType() == PunishmentType.BLACKLIST && !player.hasPermission("koru.staff.blacklist")) {
                player.sendMessage(CC.RED + "No permissions.");
                player.closeInventory();
                return;
            }

            Punishment punishment = new Punishment(banReason.getPunishmentType(), System.currentTimeMillis(),
                reason, Duration.fromString(banReason.getDuration()).getValue(), Bukkit.getServerName());

            punishment.setAddedBy(player.getUniqueId());

            punishment.setTargetID(profile.getUuid());
            punishment.save();
            profile.getPunishments().add(punishment);
            profile.save();

            lCore.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, player.getName(),
                profile.getColoredUsername(), profile.getUuid(), true, Bukkit.getServerName()));

            Player target = profile.getPlayer();

            if (target != null) {
                TaskUtil.run(() -> target.kickPlayer(punishment.getKickMessage()));
            }

            TaskUtil.run(player::closeInventory);
        }
    }

    public class CustomBanButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                .name("&eCustom Ban")
                .lore("&7After clicking this, type on chat the reason for the permanent ban")
                .lore("&7if you want to cancel process, type /cancelban instead")
                .glow()
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            new PunishmentProcedure(player, profile, PunishmentProcedureType.ADD, PunishmentProcedureStage.REQUIRE_TEXT);

            player.sendMessage(CC.GREEN + "Enter a reason for this punishment.");
            player.closeInventory();
        }
    }
}
