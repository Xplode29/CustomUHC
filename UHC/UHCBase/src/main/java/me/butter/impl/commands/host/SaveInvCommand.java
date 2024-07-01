package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.commands.AbstractCommand;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SaveInvCommand extends AbstractCommand {

    public SaveInvCommand() {
        super("save");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        List<ItemStack> armor = Arrays.asList(sender.getPlayer().getInventory().getArmorContents());
        Collections.reverse(armor);
        UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingArmor(armor);

        UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingInventory(
                Arrays.asList(sender.getPlayer().getInventory().getContents())
        );

        sender.clearInventory();

        sender.getPlayer().setGameMode(GameMode.SURVIVAL);

        UHCAPI.getInstance().getItemHandler().giveLobbyItems(sender);
    }
}
