package dev.lallemand.lcore.config.impl;

import dev.lallemand.lcore.config.ConfigConversion;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigConversion1 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 1);
        fileConfiguration.set("CHAT.FORMAT", "%1$s&r: %2$s");
        fileConfiguration.set("CHAT.CLEAR_CHAT_BROADCAST", "&eThe chat has been cleared by &r{0}");
        fileConfiguration.set("CHAT.CLEAR_CHAT_FOR_STAFF", false);
        fileConfiguration.set("CHAT.MUTE_CHAT_BROADCAST", "&eThe chat has been {0} by &r{1}");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
