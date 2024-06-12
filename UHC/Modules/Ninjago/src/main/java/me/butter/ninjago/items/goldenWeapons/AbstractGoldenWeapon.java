package me.butter.ninjago.items.goldenWeapons;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class AbstractGoldenWeapon {

    UHCPlayer holder;
    String name;
    Material material;

    int cooldown, lastTimeUsed;

    public AbstractGoldenWeapon(String name, Material material, int cooldown) {
        this.name = "§l§6" + name;
        this.material = material;

        this.cooldown = cooldown;
        this.lastTimeUsed = -cooldown;
    }

    public void onUse() {
        if(lastTimeUsed + cooldown > UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) {
            getHolder().sendMessage(ChatUtils.ERROR.getMessage("Vous devez attendre " +
                    GraphicUtils.convertToAccurateTime(cooldown + lastTimeUsed - UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) +
                    " avant d'utiliser ce pouvoir."));
            return;
        }
        if(onEnable()) {
            lastTimeUsed = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer();
        }
    }

    public boolean onEnable() {
        return true;
    }

    public void onPickup() {

    }

    public void onDrop() {

    }

    public ItemStack getItemStack() {
        return new ItemBuilder(material)
                .setName(name)
                .addEnchant(Enchantment.DURABILITY, 1)
                .hideEnchants()
                .setUnbreakable()
                .toItemStack();
    }

    public UHCPlayer getHolder() {
        return holder;
    }

    public void setHolder(UHCPlayer holder) {
        this.holder = holder;
    }
}
