package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.module.Module;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.UHCImpl;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.task.LaunchGameTask;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class StopCommand extends AbstractCommand {
    public StopCommand() {
        super("stop");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(!UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("La partie n'a pas commence !"));
        }

        stopGame();
    }

    public static void stopGame() {
        Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Arret de la partie..."));
        Bukkit.getScheduler().cancelAllTasks();

        Module module = UHCImpl.getInstance().getModuleHandler().getModule();
        if(module != null) {
            HandlerList.unregisterAll(module.getPlugin());
        }
        HandlerList.unregisterAll(UHCImpl.getInstance());

        UHCImpl.getInstance().onEnable();
        if(module != null) {
            UHCImpl.getInstance().getModuleHandler().setModule(module);
            module.getPlugin().onEnable();
        }
    }
}
