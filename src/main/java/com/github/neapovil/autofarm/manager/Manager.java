package com.github.neapovil.autofarm.manager;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.neapovil.autofarm.AutoFarm;

public final class Manager
{
    private final AutoFarm plugin = AutoFarm.getInstance();
    private final ConcurrentLinkedQueue<BukkitRunnable> queue = new ConcurrentLinkedQueue<>();

    public boolean hasCooldown(Location location)
    {
        final long ms = location.getChunk()
                .getPersistentDataContainer()
                .getOrDefault(plugin.getNamespacedKey(location), PersistentDataType.LONG, 0L);

        if (ms == 0)
        {
            return false;
        }

        if (Instant.now().isAfter(Instant.ofEpochMilli(ms)))
        {
            location.getChunk()
                    .getPersistentDataContainer()
                    .remove(plugin.getNamespacedKey(location));
            return false;
        }

        return true;
    }

    public void addCooldown(Location location)
    {
        final long time = Instant.now().plusSeconds(plugin.getConfigCooldown()).toEpochMilli();

        location.getChunk()
                .getPersistentDataContainer()
                .set(plugin.getNamespacedKey(location), PersistentDataType.LONG, time);
    }

    public void removeCooldown(Location location)
    {
        location.getChunk().getPersistentDataContainer().remove(plugin.getNamespacedKey(location));
    }

    public void addQueue(Location location, BukkitRunnable runnable)
    {
        this.queue.offer(runnable);

        this.addCooldown(location);
    }

    public boolean isQueueEmpty()
    {
        return this.queue.peek() == null;
    }

    public BukkitRunnable getQueue()
    {
        return this.queue.poll();
    }
}
