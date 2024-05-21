package me.butter.impl.menu.list.host;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RulesMenu extends PaginatedMenu {
    public RulesMenu() {
        super("Règles", 5 * 9);
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = super.getAllButtons();

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BOW).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isBow() ? "§a" : "§c") + "Arc").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getItemConfig().setBow(!UHCAPI.getInstance().getGameHandler().getItemConfig().isBow());
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENDER_PEARL).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isEnderPearl() ? "§a" : "§c") + "Ender Pearl").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getItemConfig().setEnderPearl(!UHCAPI.getInstance().getGameHandler().getItemConfig().isEnderPearl());
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FISHING_ROD).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isRod() ? "§a" : "§c") + "Canne à pèche").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getItemConfig().setRod(!UHCAPI.getInstance().getGameHandler().getItemConfig().isRod());
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.SNOW_BALL).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isProjectile() ? "§a" : "§c") + "Projectiles").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getItemConfig().setProjectile(!UHCAPI.getInstance().getGameHandler().getItemConfig().isProjectile());
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.LAVA_BUCKET).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isLavaBucket() ? "§a" : "§c") + "Seau de lave").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getItemConfig().setLavaBucket(!UHCAPI.getInstance().getGameHandler().getItemConfig().isLavaBucket());
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FLINT_AND_STEEL).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isFlintAndSteel() ? "§a" : "§c") + "Briquet").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getItemConfig().setFlintAndSteel(!UHCAPI.getInstance().getGameHandler().getItemConfig().isFlintAndSteel());
            }
        });

        return buttons;
    }
}
