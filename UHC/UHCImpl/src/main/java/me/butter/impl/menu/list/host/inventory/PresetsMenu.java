package me.butter.impl.menu.list.host.inventory;

import me.butter.api.UHCAPI;
import me.butter.api.game.Preset;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PresetsMenu extends PaginatedMenu {
    public PresetsMenu() {
        super("§6Presets", 5 * 9);
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();
        for(Preset preset : UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getPresetList()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(preset.getIcon()).setName("§r" + preset.getName()).toItemStack();
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingInventory(preset.getStartingInventory());
                    UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingArmor(preset.getStartingArmor());
                }
            });
        }

        return buttons;
    }
}
