package me.butter.impl.menu.list.host.worldconfig;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DayCycleMenu extends AbstractMenu {
    public DayCycleMenu() {
        super("Cycle jour-nuit", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER).setName("§rCycle jour-nuit " + (UHCAPI.getInstance().getGameHandler().getGameConfig().isDayCycleActivated() ? "§aActivé" : "§cDésactivé")).build();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getGameConfig().setDayCycleActivated(!UHCAPI.getInstance().getGameHandler().getGameConfig().isDayCycleActivated());
            }
        });

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DAYLIGHT_DETECTOR)
                        .setName("§rDurée d'une journée: " + GraphicUtils.convertToAccurateTime(
                                UHCAPI.getInstance().getGameHandler().getGameConfig().getDayDuration()
                        )).build();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT) {
                    UHCAPI.getInstance().getGameHandler().getGameConfig().setDayDuration(UHCAPI.getInstance().getGameHandler().getGameConfig().getDayDuration() + 60);
                    if(UHCAPI.getInstance().getGameHandler().getGameConfig().getDayDuration() > 20 * 60) {
                        UHCAPI.getInstance().getGameHandler().getGameConfig().setDayDuration(20 * 60);
                    }
                } else if(clickType == ClickType.RIGHT) {
                    UHCAPI.getInstance().getGameHandler().getGameConfig().setDayDuration(UHCAPI.getInstance().getGameHandler().getGameConfig().getDayDuration() - 60);
                    if(UHCAPI.getInstance().getGameHandler().getGameConfig().getDayDuration() < 2 * 60) {
                        UHCAPI.getInstance().getGameHandler().getGameConfig().setDayDuration(2 * 60);
                    }
                }
            }
        });

        buttons.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.WATCH)
                        .setName("§rDurée d'un episode: " + GraphicUtils.convertToAccurateTime(
                                UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration()
                        )).build();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT) {
                    UHCAPI.getInstance().getGameHandler().getGameConfig().setEpisodeDuration(UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration() + 2 * 60);
                    if(UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration() > 40 * 60) {
                        UHCAPI.getInstance().getGameHandler().getGameConfig().setEpisodeDuration(40 * 60);
                    }
                } else if(clickType == ClickType.RIGHT) {
                    UHCAPI.getInstance().getGameHandler().getGameConfig().setEpisodeDuration(UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration() - 2 * 60);
                    if(UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration() < 4 * 60) {
                        UHCAPI.getInstance().getGameHandler().getGameConfig().setEpisodeDuration(4 * 60);
                    }
                }
            }
        });

        return buttons;
    }
}
