package com.github.neapovil.autofarm.command;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.neapovil.autofarm.AutoFarm;
import com.github.neapovil.autofarm.runnable.CheckChunkRunnable;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;

public final class AutoFarmCommand
{
    private static final AutoFarm plugin = AutoFarm.getInstance();

    public static final void register()
    {
        new CommandAPICommand("autofarm")
                .withPermission("autofarm.command.user")
                .withArguments(new LiteralArgument("break"))
                .withArguments(new MultiLiteralArgument("cactus", "wheat"))
                .executesPlayer((player, args) -> {
                    final String farmtype = (String) args[0];

                    if (plugin.getManager().hasCooldown(player.getLocation()))
                    {
                        throw CommandAPI.fail("This farm is in cooldown");
                    }

                    final BukkitRunnable runnable = new CheckChunkRunnable(player.getLocation(), player.getChunk().getChunkSnapshot(), farmtype);

                    plugin.getManager().addQueue(player.getLocation(), runnable);

                    player.sendMessage("Farm added to queue!");
                })
                .register();
    }
}
