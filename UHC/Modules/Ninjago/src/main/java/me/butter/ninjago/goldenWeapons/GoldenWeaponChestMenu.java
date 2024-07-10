package me.butter.ninjago.goldenWeapons;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GoldenWeaponChestMenu extends AbstractMenu {

    StructChestHolder chest;

    public GoldenWeaponChestMenu(StructChestHolder chest) {
        super("Arme d'or", 5 * 9, true);

        this.chest = chest;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = super.getButtons();

        if(chest.getWeapon() == null) return buttons;

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return chest.getWeapon().getItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(chest.getWeapon().getHolder() != null) {
                    closeMenu();
                    return;
                }

                chest.getWeapon().setHolder(player);
                chest.getWeapon().onPickup();

                player.giveItem(chest.getWeapon().getItemStack(), true);
                chest.clearChest();
                closeMenu();
            }
        });

        return buttons;
    }
}
