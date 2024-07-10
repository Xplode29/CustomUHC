package me.butter.impl.menu.list.host.presets;

import me.butter.api.UHCAPI;
import me.butter.api.game.Preset;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.game.invPresets.MeetupPreset;
import me.butter.impl.game.invPresets.MiningPreset;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.timer.list.*;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PresetsMenu extends AbstractMenu {
    public PresetsMenu() {
        super("Presets", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIAMOND_PICKAXE).setName("§rMinage").build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                Preset preset = UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getPreset(MiningPreset.class);
                if (preset != null) {
                    UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingInventory(preset.getStartingInventory());
                    UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingArmor(preset.getStartingArmor());
                }

                UHCAPI.getInstance().getGameHandler().getWorldConfig().setStartingBorderSize(1000);
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(200);

                UHCAPI.getInstance().getGameHandler().getWorldConfig().setDiamondLimit(20);
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setExpBoost(50);
                UHCAPI.getInstance().getGameHandler().getGameConfig().setDayCycleActivated(true);
                UHCAPI.getInstance().getGameHandler().getGameConfig().setDayDuration(5 * 60);
                UHCAPI.getInstance().getGameHandler().getGameConfig().setEpisodeDuration(20 * 60);

                UHCAPI.getInstance().getTimerHandler().getTimer(InvincibilityTimer.class).setMaxTimer(60);
                UHCAPI.getInstance().getTimerHandler().getTimer(RoleTimer.class).setMaxTimer(20 * 60);
                UHCAPI.getInstance().getTimerHandler().getTimer(PVPTimer.class).setMaxTimer(20 * 60);
                UHCAPI.getInstance().getTimerHandler().getTimer(MeetupTimer.class).setMaxTimer(60 * 60);
                UHCAPI.getInstance().getTimerHandler().getTimer(BorderTimer.class).setMaxTimer(80 * 60);
            }
        });

        buttons.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIAMOND_SWORD).setName("§rMeetup").build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                Preset preset = UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getPreset(MeetupPreset.class);
                if (preset != null) {
                    UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingInventory(preset.getStartingInventory());
                    UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingArmor(preset.getStartingArmor());
                }

                UHCAPI.getInstance().getGameHandler().getWorldConfig().setStartingBorderSize(250);
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(100);

                UHCAPI.getInstance().getGameHandler().getWorldConfig().setDiamondLimit(0);
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setExpBoost(0);
                UHCAPI.getInstance().getGameHandler().getGameConfig().setDayCycleActivated(true);
                UHCAPI.getInstance().getGameHandler().getGameConfig().setDayDuration(60);
                UHCAPI.getInstance().getGameHandler().getGameConfig().setEpisodeDuration(2 * 60);

                UHCAPI.getInstance().getTimerHandler().getTimer(InvincibilityTimer.class).setMaxTimer(60);
                UHCAPI.getInstance().getTimerHandler().getTimer(RoleTimer.class).setMaxTimer(30);
                UHCAPI.getInstance().getTimerHandler().getTimer(PVPTimer.class).setMaxTimer(60);
                UHCAPI.getInstance().getTimerHandler().getTimer(MeetupTimer.class).setMaxTimer(60);
                UHCAPI.getInstance().getTimerHandler().getTimer(BorderTimer.class).setMaxTimer(10 * 60);
            }
        });

        return buttons;
    }
}
