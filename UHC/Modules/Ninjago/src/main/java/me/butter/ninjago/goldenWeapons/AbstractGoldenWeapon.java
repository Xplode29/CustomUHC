package me.butter.ninjago.goldenWeapons;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.item.AbstractItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class AbstractGoldenWeapon extends AbstractItem {
    UHCPlayer holder;
    String name;
    String[] lore;
    Material material;

    int cooldown, lastTimeUsed;

    public AbstractGoldenWeapon(
            String name,
            String[] lore,
            Material material,
            int cooldown
    ) {
        super(material, "§6§l" + name);
        this.name = "§6§l" + name;
        this.material = material;
        this.lore = lore;

        this.cooldown = cooldown;
        this.lastTimeUsed = -cooldown;
    }

    @Override
    public void onClick(UHCPlayer uhcPlayer) {
        if(!uhcPlayer.equals(getHolder())) {
            uhcPlayer.getPlayer().getInventory().remove(getItemStack());
            return;
        }
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

    @Override
    public boolean isDroppable() {
        return true;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(material)
                .setName(name)
                .setLore(lore)
                .addEnchant(Enchantment.DURABILITY, 1)
                .hideEnchants()
                .setUnbreakable()
                .build();
    }

    public UHCPlayer getHolder() {
        return holder;
    }

    public void setHolder(UHCPlayer holder) {
        this.holder = holder;
    }
}
