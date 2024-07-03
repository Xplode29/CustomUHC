package me.butter.ninjago.coloredNames;

import com.mojang.authlib.GameProfile;
import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.BlockColor;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import me.butter.ninjago.NinjagoV2;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

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
                    NinjagoV2.getInstance().getNametagManager().setPlayerToTeam(
                            getOpener().getPlayer(),
                            NinjagoV2.getInstance().getNametagManager().getTeam(
                                    getOpener().getPlayer(),
                                    color
                            ),
                            selectedPlayer.getName()
                    );
                    closeMenu();
                }
            });
        }

        return buttons;
    }
}
