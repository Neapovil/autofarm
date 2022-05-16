package com.github.neapovil.autofarm.runnable;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.neapovil.autofarm.AutoFarm;

public final class ProcessTasksRunnable extends BukkitRunnable
{
    private final AutoFarm plugin = AutoFarm.getInstance();
    private BukkitTask task;

    @Override
    public void run()
    {
        final boolean flag = task != null
                && !plugin.getServer().getScheduler().isQueued(task.getTaskId())
                && !plugin.getServer().getScheduler().isCurrentlyRunning(task.getTaskId());

        if (flag)
        {
            task.cancel();
            task = null;
        }

        if (plugin.getManager().isQueueEmpty())
        {
            return;
        }

        if (task == null)
        {
            task = plugin.getManager().getQueue().runTaskAsynchronously(plugin);
        }
    }
}
