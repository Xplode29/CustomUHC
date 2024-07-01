package me.butter.impl.menu.list.host.worldconfig;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WorldSelectMenu extends PaginatedMenu {
    public WorldSelectMenu() {
        super("Mondes", 5 * 9);
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();

        for(World world : Bukkit.getWorlds()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.GRASS).setName("§r" + world.getName()).toItemStack();
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    closeMenu();
                    player.getPlayer().teleport(new Location(world, 0, 100, 0));
                }
            });
        }

        return buttons;
    }
}
