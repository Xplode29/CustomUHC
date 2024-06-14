package me.butter.impl.menu.list.host.settings.inventory;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryMenu extends AbstractMenu {
    public InventoryMenu() {
        super("Inventaire", 6 * 9, false);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        for(int i = 0; i < UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingInventory().size(); i++) {
            ItemStack itemStack = UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingInventory().get(i);
            buttons.put((i < 9 ? i + 27 : i - 9), new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return itemStack;
                }
            });
        }

        for(int i = 36; i < 45; i++) {
            buttons.put(i, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").toItemStack();
                }
            });
        }

        for (int i = 51; i < 53; i++) {
            buttons.put(i, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").toItemStack();
                }
            });
        }

        for(int i = 0; i < UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingArmor().size(); i++) {
            ItemStack itemStack = UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingArmor().get(i);
            buttons.put(i + 45, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return itemStack;
                }
            });
        }

        buttons.put(50, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ANVIL).setName("§rModifier l'inventaire").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                UHCAPI.getInstance().getItemHandler().removeLobbyItems(uhcPlayer);
                uhcPlayer.getPlayer().setGameMode(GameMode.CREATIVE);
                closeMenu();

                uhcPlayer.setInventory(UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingInventory());
                uhcPlayer.setArmor(UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingArmor());

                uhcPlayer.sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Entrez /h save pour sauvegarder !"));
            }
        });

        buttons.put(53, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.REDSTONE_BLOCK).setName("§rPresets").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                uhcPlayer.openMenu(new InvPresetsMenu(), true);
            }
        });

        return buttons;
    }
}
