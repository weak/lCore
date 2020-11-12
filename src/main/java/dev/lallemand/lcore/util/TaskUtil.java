package dev.lallemand.lcore.util;

import org.bukkit.scheduler.BukkitRunnable;
import dev.lallemand.lcore.lCore;

public class TaskUtil {

    public static void run(Runnable runnable) {
        lCore.get().getServer().getScheduler().runTask(lCore.get(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        lCore.get().getServer().getScheduler().runTaskTimer(lCore.get(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(lCore.get(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        lCore.get().getServer().getScheduler().runTaskLater(lCore.get(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        lCore.get().getServer().getScheduler().runTaskAsynchronously(lCore.get(), runnable);
    }

}
