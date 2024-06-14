package me.butter.impl.menu.list.host.settings;

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

public class AuthorizedItemsMenu extends PaginatedMenu {
    public AuthorizedItemsMenu() {
        super("Règles", 5 * 9);
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = super.getAllButtons();

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BOW).setName("Arc" + (UHCAPI.getInstance().getGameHandler().getItemConfig().isBow() ? " - §aActivé" : " - §cDésactivé")).toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
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
                return new ItemBuilder(Material.ENDER_PEARL).setName("Ender Pearl" + (UHCAPI.getInstance().getGameHandler().getItemConfig().isEnderPearl() ? " - §aActivé" : " - §cDésactivé")).toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
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
                return new ItemBuilder(Material.FISHING_ROD).setName("Canne à pèche" + (UHCAPI.getInstance().getGameHandler().getItemConfig().isRod() ? " - §aActivé" : " - §cDésactivé")).toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
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
                return new ItemBuilder(Material.SNOW_BALL).setName("Projectiles" + (UHCAPI.getInstance().getGameHandler().getItemConfig().isProjectile() ? " - §aActivé" : " - §cDésactivé")).toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
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
                return new ItemBuilder(Material.LAVA_BUCKET).setName("Seau de lave" + (UHCAPI.getInstance().getGameHandler().getItemConfig().isLavaBucket() ? " - §aActivé" : " - §cDésactivé")).toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
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
                return new ItemBuilder(Material.FLINT_AND_STEEL).setName("Briquet" + (UHCAPI.getInstance().getGameHandler().getItemConfig().isFlintAndSteel() ? " - §aActivé" : " - §cDésactivé")).toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
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
