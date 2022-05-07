package com.github.neapovil.autofarm.runnable;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public final class FarmChunkRunnable extends BukkitRunnable
{
    private final List<Location> locations = new it.unimi.dsi.fastutil.objects.ObjectArrayList<>();

    public FarmChunkRunnable(List<Location> locations)
    {
        this.locations.addAll(locations);
    }

    @Override
    public void run()
    {
        for (Location i : this.locations)
        {
            i.getBlock().breakNaturally();
        }
    }
}
