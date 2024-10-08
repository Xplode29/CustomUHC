package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.Enchant;
import me.butter.api.game.GameState;
import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.potion.CustomPotionEffect;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;

public class InventoriesEvents implements Listener {

    @EventHandler
    public void onBrewPotion(BrewEvent event) {
        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            List<ItemStack> potions = Arrays.asList(event.getContents().getItem(0), event.getContents().getItem(1), event.getContents().getItem(2)); // <--->
            for(ItemStack item : potions) {
                Potion potion = Potion.fromItemStack(item);
                if(potion == null) continue;

                PotionType potionType = potion.getType();
                if(potionType == null) continue;

                CustomPotionEffect customPotionEffect = UHCAPI.getInstance().getPotionEffectHandler().getEffect(potionType.getEffectType());
                if(customPotionEffect == null) continue;

                if(potion.getLevel() + 1 > customPotionEffect.getLevel()) {
                    event.setCancelled(true);
                }

                if(event.getContents().getIngredient().getType() == Material.SULPHUR && !customPotionEffect.canBeSplash()) {
                    event.setCancelled(true);
                }

                if(event.getContents().getIngredient().getType() == Material.REDSTONE && !customPotionEffect.canBeAmplified()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return;

        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            Role role = uhcPlayer.getRole();
            if(role != null && (uhcPlayer.getPlayer().getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING)) {
                for(Power power : role.getPowers()) {
                    if(power instanceof ItemPower) {
                        if(((ItemPower) power).getItem().isSimilar(event.getCurrentItem()) && !((ItemPower) power).canMoveItem()) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }

            if (inv instanceof AnvilInventory) {
                AnvilInventory anvil = (AnvilInventory) inv;
                InventoryView view = event.getView();
                int rawSlot = event.getRawSlot();
                if (rawSlot != view.convertSlot(rawSlot) || rawSlot != 2) {
                    return;
                }

                ItemStack book = anvil.getItem(1);
                ItemStack result = anvil.getItem(2);
                if (result == null || result.getEnchantments().values().isEmpty()) {
                    return;
                }

                if(uhcPlayer.getRole() != null) {
                    for(Power power : uhcPlayer.getRole().getPowers()) {
                        if(power instanceof EnchantBookPower) {
                            if(((EnchantBookPower) power).getItem().isSimilar(book)) {
                                return;
                            }
                        }
                    }
                }

                for (Enchant enchant : UHCAPI.getInstance().getEnchantHandler().getEnchants()) {
                    if (!result.getEnchantments().containsKey(enchant.getEnchantment())) continue;

                    if(result.getType().name().contains("DIAMOND")) {
                        if(result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getDiamondLevel()) {
                            result.removeEnchantment(enchant.getEnchantment());
                            if(enchant.getDiamondLevel() > 0)
                                result.addEnchantment(enchant.getEnchantment(), enchant.getDiamondLevel());
                            player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getDiamondLevel() + " sur les items en diamant."));
                        }
                    }
                    else {
                        if(result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getAllLevel()) {
                            result.removeEnchantment(enchant.getEnchantment());
                            if(enchant.getAllLevel() > 0)
                                result.addEnchantment(enchant.getEnchantment(), enchant.getAllLevel());
                            player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getAllLevel() + "."));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack result = event.getItem();

        if (player == null || result == null) return;

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            for (Enchant enchant : UHCAPI.getInstance().getEnchantHandler().getEnchants()) {
                if (!event.getEnchantsToAdd().containsKey(enchant.getEnchantment())) continue;
                int level = event.getEnchantsToAdd().get(enchant.getEnchantment());

                if(result.getType().name().contains("DIAMOND")) {
                    if(level > enchant.getDiamondLevel()) {
                        event.getEnchantsToAdd().remove(enchant.getEnchantment());
                        if(enchant.getDiamondLevel() > 0)
                            event.getEnchantsToAdd().put(enchant.getEnchantment(), enchant.getDiamondLevel());
                        player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getDiamondLevel() + " sur les items en diamant."));
                    }
                }
                else {
                    if(level > enchant.getAllLevel()) {
                        event.getEnchantsToAdd().remove(enchant.getEnchantment());
                        if(enchant.getAllLevel() > 0)
                            event.getEnchantsToAdd().put(enchant.getEnchantment(), enchant.getAllLevel());
                        player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getAllLevel() + "."));
                    }
                }
            }
        }
    }
}
