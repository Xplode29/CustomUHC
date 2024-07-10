package me.butter.impl.menu.list.player.color;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerNametagMenu extends PaginatedMenu {

    List<UHCPlayer> players;

    public PlayerNametagMenu() {
        super("Joueurs", 5 * 9);

        players = UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().stream().filter(uhcPlayer -> !uhcPlayer.isDisconnected()).sorted(Comparator.comparing(UHCPlayer::getName)).collect(Collectors.toList());
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = super.getAllButtons();
        for(UHCPlayer p : players) {
            String name;

            if(getOpener().getColoredNametags().containsKey(p)) name = getOpener().getColoredNametags().get(p) + p.getName();
            else name = "§r" + p.getName();

            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.SKULL_ITEM ,1, (byte) 3)
                            .setName(name)
                            .setSkullOwner(p.getName())
                            .build();
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    player.openMenu(new ChooseColorMenu(p), true);
                }
            });
        }
        return buttons;
    }
}
