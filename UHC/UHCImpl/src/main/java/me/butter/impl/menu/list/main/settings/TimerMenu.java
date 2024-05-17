package me.butter.impl.menu.list.main.settings;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.Enchant;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.timer.Timer;
import me.butter.api.utils.ChatUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.StringUtils;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TimerMenu extends PaginatedMenu {
    List<Button> buttons;

    public TimerMenu() {
        super("Timers", 5 * 9);

        buttons = new ArrayList<>();

        for(Timer timer : UHCAPI.get().getTimerHandler().getTimers()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(timer.getIcon()).setName("§r" + timer.getName()).toItemStack();
                }

                @Override
                public boolean doesUpdateGui() {
                    return true;
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    player.openMenu(new TimerConfigMenu(timer), true);
                }
            });
        }
    }

    @Override
    public List<Button> getAllButtons() {
        return buttons;
    }
}
