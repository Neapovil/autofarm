package com.github.neapovil.autofarm;

import java.io.File;
import java.time.Duration;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.github.neapovil.autofarm.command.AutoFarmCommand;
import com.github.neapovil.autofarm.manager.Manager;
import com.github.neapovil.autofarm.runnable.ProcessTasksRunnable;

public final class AutoFarm extends JavaPlugin
{
    private static AutoFarm instance;
    private Manager manager;
    private FileConfig config;

    @Override
    public void onEnable()
    {
        instance = this;

        this.saveResource("config.json", false);

        this.config = FileConfig.builder(new File(this.getDataFolder(), "config.json"))
                .autoreload()
                .autosave()
                .build();

        this.config.load();

        this.manager = new Manager();

        new ProcessTasksRunnable().runTaskTimerAsynchronously(this, 0, 20);
        
        AutoFarmCommand.register();
    }

    @Override
    public void onDisable()
    {
    }

    public static AutoFarm getInstance()
    {
        return instance;
    }

    public Manager getManager()
    {
        return this.manager;
    }

    public NamespacedKey getNamespacedKey(Location location)
    {
        final long coordinates = location.getBlockY() + location.getChunk().getChunkKey();

        return new NamespacedKey(this, "" + coordinates);
    }

    public long getConfigCooldown()
    {
        final String s = this.config.get("config.cooldown");
        return Duration.parse(s).toSeconds();
    }
}
