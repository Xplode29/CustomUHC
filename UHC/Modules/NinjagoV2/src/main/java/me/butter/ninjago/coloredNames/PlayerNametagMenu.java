package me.butter.ninjago.coloredNames;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class PlayerNametagMenu extends PaginatedMenu {
    public PlayerNametagMenu() {
        super("Joueurs", 5 * 9);
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = super.getAllButtons();
        for(UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.SKULL_ITEM ,1, (byte) 3)
                            .setName(player.getName())
                            .setSkullOwner(player.getName())
                            .build();
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    getOpener().openMenu(new ChooseColorMenu(player), true);
                }
            });
        }
        return buttons;
    }
}
