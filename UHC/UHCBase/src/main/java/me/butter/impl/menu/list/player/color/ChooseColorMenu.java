package me.butter.impl.menu.list.player.color;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.BlockColor;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChooseColorMenu extends PaginatedMenu {

    UHCPlayer selectedPlayer;

    public ChooseColorMenu(UHCPlayer selectedPlayer) {
        super("Choisi une couleur", 5 * 9);

        this.selectedPlayer = selectedPlayer;
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = super.getAllButtons();

        for(int i = 0; i < 16; i++) {
            ChatColor color = BlockColor.getChatColor(Material.WOOL, (byte) i);
            if(color == ChatColor.BLACK && i != 15) continue;
            ItemStack coloredWool = new ItemBuilder(Material.WOOL, 1, (byte) i)
                    .setName(color + selectedPlayer.getName())
                    .build();

            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return coloredWool;
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    UHCAPI.getInstance().getNametagColorHandler().setPlayerToTeam(
                            getOpener(),
                            color,
                            selectedPlayer
                    );
                }
            });
        }

        return buttons;
    }
}
