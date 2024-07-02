package me.butter.impl.commands.host;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import org.bukkit.Bukkit;

import java.util.List;

public class FinalHealCommand extends AbstractCommand {
    public FinalHealCommand() {
        super("finalheal", "fh");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        for(UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
            if(player.getPlayer() == null) continue;
            player.getPlayer().setHealth(player.getPlayer().getMaxHealth());
        }
        Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Final heal !"));
    }
}
