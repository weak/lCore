package dev.lallemand.lcore.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public interface ConfigConversion {

    void convert(File file, FileConfiguration fileConfiguration);

}
