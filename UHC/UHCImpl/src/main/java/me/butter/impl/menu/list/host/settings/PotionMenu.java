package me.butter.impl.menu.list.host.settings;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.Enchant;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.potion.CustomPotionEffect;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PotionMenu extends PaginatedMenu {
    List<Button> buttons;

    public PotionMenu() {
        super("Potions", 5 * 9);

        buttons = new ArrayList<>();

        for(CustomPotionEffect potionEffect : UHCAPI.getInstance().getPotionEffectHandler().getPotionEffects()) {
            buttons.add(new PotionButton(potionEffect));
        }
    }

    @Override
    public List<Button> getAllButtons() {
        return buttons;
    }

    private static class PotionButton extends ButtonImpl {
        public CustomPotionEffect potionEffect;
        int selected = 0;

        public PotionButton(CustomPotionEffect potionEffect) {
            this.potionEffect = potionEffect;
        }

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder(Material.POTION)
                    .setName("§r" + potionEffect.getName())
                    .addLoreLine((selected == 0 ? "§e" : "§7") + "Level: " + potionEffect.getLevel() + " / " + potionEffect.getMaxLevel())
                    .addLoreLine((selected == 1 ? "§e" : "§7") + "Splash: " + (potionEffect.canBeSplash() ? "§aON" : "§cOFF"))
                    .addLoreLine((selected == 2 ? "§e" : "§7") + "Amplified: " + (potionEffect.canBeAmplified() ? "§aON" : "§cOFF"))
                    .toItemStack();
        }

        @Override
        public boolean doesUpdateButton() {
            return true;
        }

        @Override
        public void onClick(UHCPlayer player, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                if(selected == 0) {
                    if(potionEffect.getLevel() < potionEffect.getMaxLevel()) {
                        potionEffect.setLevel(potionEffect.getLevel() + 1);
                    }
                    else {
                        potionEffect.setLevel(0);
                    }
                }
                else if(selected == 1) {
                    potionEffect.setSplash(!potionEffect.canBeSplash());
                }
                else if(selected == 2) {
                    potionEffect.setAmplified(!potionEffect.canBeAmplified());
                }
            }
            else if(clickType == ClickType.RIGHT)  {
                selected++;
                if(selected > 2) selected = 0;
            }
        }
    }
}
