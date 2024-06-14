package me.butter.ninjago.menu;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.RoleEnum;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RoleMenu extends PaginatedMenu {
    CampEnum camp;

    public RoleMenu(CampEnum camp) {
        super("Roles", 5 * 9);

        this.camp = camp;
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();

        for(RoleEnum role : RoleEnum.values()) {
            if(role.getCampEnum() != camp) continue;
            buttons.add(new ButtonImpl() {
                int amount = role.getAmount();

                @Override
                public ItemStack getIcon() {
                    if(amount > 0) {
                        return new ItemBuilder(role.getIcon())
                                .setName("§r" + role.getName() + " §7(" + amount + ")")
                                .addEnchant(Enchantment.DIG_SPEED, 1)
                                .hideItemFlags()
                                .toItemStack();
                    }
                    else {
                        return new ItemBuilder(role.getIcon()).setName("§r" + role.getName() + " §7(" + amount + ")").toItemStack();
                    }
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    if(clickType == ClickType.LEFT) {
                        amount++;
                    }
                    else if(clickType == ClickType.RIGHT) {
                        amount --;
                        if(amount < 0) amount = 0;
                    }
                    role.setAmount(amount);
                }

                @Override
                public boolean doesUpdateButton() {
                    return true;
                }
            });
        }

        return buttons;
    }
}
