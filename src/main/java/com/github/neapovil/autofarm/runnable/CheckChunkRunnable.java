package com.github.neapovil.autofarm.runnable;

import java.util.List;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.neapovil.autofarm.AutoFarm;

public final class CheckChunkRunnable extends BukkitRunnable
{
    private final AutoFarm plugin = AutoFarm.getInstance();
    private final Location location;
    private final ChunkSnapshot chunkSnapshot;
    private final String farmType;
    private final List<Location> locations = new it.unimi.dsi.fastutil.objects.ObjectArrayList<>();

    public CheckChunkRunnable(Location location, ChunkSnapshot chunkSnapshot, String farmType)
    {
        this.location = location;
        this.chunkSnapshot = chunkSnapshot;
        this.farmType = farmType;
    }

    @Override
    public void run()
    {
        if (this.farmType.equals("cactus"))
        {
            if (!this.chunkSnapshot.contains(Material.CACTUS.createBlockData()))
            {
                return;
            }

            this.cactus();

            return;
        }

        if (this.farmType.equals("wheat"))
        {
            this.wheat();
            return;
        }
    }

    private final void cactus()
    {
        for (int x = 0; x <= 15; x++)
        {
            for (int z = 0; z <= 15; z++)
            {
                final Material material = this.chunkSnapshot.getBlockType(x, this.location.getBlockY(), z);

                if (!material.equals(Material.CACTUS))
                {
                    continue;
                }

                this.locations.add(new Location(this.location.getWorld(), this.chunkSnapshot.getX() * 16 + x, this.location.getBlockY(),
                        this.chunkSnapshot.getZ() * 16 + z));
            }
        }

        this.runTask();
    }

    private final void wheat()
    {
        for (int x = 0; x <= 15; x++)
        {
            for (int z = 0; z <= 15; z++)
            {
                final Material material = this.chunkSnapshot.getBlockType(x, this.location.getBlockY(), z);

                if (!material.equals(Material.WHEAT))
                {
                    continue;
                }

                final Ageable ageable = (Ageable) this.chunkSnapshot.getBlockData(x, this.location.getBlockY(), z);

                if (ageable.getAge() != ageable.getMaximumAge())
                {
                    continue;
                }

                this.locations.add(new Location(this.location.getWorld(), this.chunkSnapshot.getX() * 16 + x, this.location.getBlockY(),
                        this.chunkSnapshot.getZ() * 16 + z));
            }
        }

        this.runTask();
    }

    private final void runTask()
    {
        if (!this.locations.isEmpty())
        {
            new FarmChunkRunnable(this.locations).runTask(this.plugin);
        }
    }
}
