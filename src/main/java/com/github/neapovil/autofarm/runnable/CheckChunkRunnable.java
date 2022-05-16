package com.github.neapovil.autofarm.runnable;

import java.util.List;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.neapovil.autofarm.AutoFarm;

public final class CheckChunkRunnable extends BukkitRunnable
{
    private final AutoFarm plugin = AutoFarm.getInstance();
    private final Location location;
    private final ChunkSnapshot chunkSnapshot;
    private final String farmType;

    public CheckChunkRunnable(Location location, ChunkSnapshot chunkSnapshot, String farmType)
    {
        this.location = location;
        this.chunkSnapshot = chunkSnapshot;
        this.farmType = farmType;
    }

    @Override
    public void run()
    {
        if (this.farmType.equalsIgnoreCase("cactus"))
        {
            if (!this.chunkSnapshot.contains(Material.CACTUS.createBlockData()))
            {
                return;
            }

            this.cactusFarm();

            return;
        }
    }

    private final void cactusFarm()
    {
        final List<Location> locations = new it.unimi.dsi.fastutil.objects.ObjectArrayList<>();

        for (int x = 0; x <= 15; x++)
        {
            for (int z = 0; z <= 15; z++)
            {
                final Material material = this.chunkSnapshot.getBlockType(x, this.location.getBlockY(), z);

                if (!material.equals(Material.CACTUS))
                {
                    continue;
                }

                locations.add(new Location(this.location.getWorld(), this.chunkSnapshot.getX() * 16 + x, this.location.getBlockY(),
                        this.chunkSnapshot.getZ() * 16 + z));
            }
        }

        if (!locations.isEmpty())
        {
            new FarmChunkRunnable(locations).runTask(plugin);
        }
    }
}
