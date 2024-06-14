package me.butter.impl.menu;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PaginatedMenu extends AbstractMenu {
    private int page = 0;
    private int buttonsPerPage;

    public PaginatedMenu(String title, int size) {
        super(title, size, true);
        buttonsPerPage = ((size / 9) - 2) * 7;
    }

    public List<Button> getAllButtons() {
        return new ArrayList<>();
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> map = new HashMap<>();
        for (int i = 0; i < getAllButtons().size(); i++) {
            if(page * buttonsPerPage <= i && i < (page + 1) * buttonsPerPage) {
                int line = (i - page * buttonsPerPage) / 7;
                int colon = (i - page * buttonsPerPage) % 7;

                map.put((colon + 1) + (line + 1) * 9, getAllButtons().get(i));
            }
        }

        if(page > 0)
            map.put(size - 6, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.ARROW).setName("§rPage Précédente").toItemStack();
                }

                @Override
                public boolean doesUpdateButton() {
                    return true;
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    page--;
                }
            });

        if(getAllButtons().size() > (page + 1) * buttonsPerPage)
            map.put(size - 4, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.ARROW).setName("§rPage Suivante").toItemStack();
                }

                @Override
                public boolean doesUpdateButton() {
                    return true;
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    page++;
                }
            });

        return map;
    }
}
