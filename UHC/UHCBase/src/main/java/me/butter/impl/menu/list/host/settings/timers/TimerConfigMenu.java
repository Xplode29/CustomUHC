package me.butter.impl.menu.list.host.settings.timers;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.timer.Timer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.Icons;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TimerConfigMenu extends AbstractMenu {

    Timer timer;

    public TimerConfigMenu(Timer timer) {
        super(timer.getName(), 5 * 9, true);

        this.timer = timer;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(19, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_BIG.toItemStack("- 5min");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                timer.setMaxTimer(timer.getMaxTimer() - 5 * 60);
                if(timer.getMaxTimer() < 10) {
                    timer.setMaxTimer(10);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_MEDIUM.toItemStack("- 1min");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                timer.setMaxTimer(timer.getMaxTimer() - 60);
                if(timer.getMaxTimer() < 10) {
                    timer.setMaxTimer(10);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(21, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_SMALL.toItemStack("- 10s");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                timer.setMaxTimer(timer.getMaxTimer() - 10);
                if(timer.getMaxTimer() < 10) {
                    timer.setMaxTimer(10);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(timer.getIcon()).setName("§r" + timer.getName() + ": " + GraphicUtils.convertToAccurateTime(timer.getMaxTimer())).build();
            }
        });

        buttons.put(23, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_SMALL.toItemStack("+ 10s");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                timer.setMaxTimer(timer.getMaxTimer() + 10);
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_MEDIUM.toItemStack("+ 1min");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                timer.setMaxTimer(timer.getMaxTimer() + 60);
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(25, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_BIG.toItemStack("+ 5min");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                timer.setMaxTimer(timer.getMaxTimer() + 60 * 5);
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        return buttons;
    }
}
