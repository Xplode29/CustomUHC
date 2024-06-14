package me.butter.impl.menu.list.player;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FullMenu extends PaginatedMenu {
    public FullMenu() {
        super("Full", 5 * 9);
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();

        for(ItemStack itemStack : getOpener().getStash()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return itemStack;
                }

                @Override
                public boolean doesUpdateButton() {
                    return true;
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    if(player.getPlayer().getInventory().firstEmpty() > -1 || player.getPlayer().getInventory().contains(itemStack)) {
                        player.giveItem(itemStack, false);
                        player.removeItemFromStash(itemStack);
                    }
                    else {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Votre inventaire est plein !"));
                    }
                }
            });
        }

        return buttons;
    }
}
