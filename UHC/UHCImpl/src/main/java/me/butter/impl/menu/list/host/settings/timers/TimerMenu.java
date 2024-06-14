package me.butter.impl.menu.list.host.settings.timers;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.timer.Timer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TimerMenu extends PaginatedMenu {
    List<Button> buttons;

    public TimerMenu() {
        super("Timers", 5 * 9);

        buttons = new ArrayList<>();

        for(Timer timer : UHCAPI.getInstance().getTimerHandler().getTimers()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(timer.getIcon()).setName("§r" + timer.getName() + ": " + GraphicUtils.convertToAccurateTime(timer.getMaxTimer())).toItemStack();
                }

                @Override
                public boolean doesUpdateButton() {
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
