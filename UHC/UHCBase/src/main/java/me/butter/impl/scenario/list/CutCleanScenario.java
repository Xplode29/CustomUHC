package me.butter.impl.scenario.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.impl.events.custom.CustomBlockBreakEvent;
import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CutCleanScenario extends AbstractScenario {
    private final Map<Material, Material> cookedMats = new HashMap<>();

    public CutCleanScenario() {
        super("Cut Clean", Material.FURNACE);

        //food
        cookedMats.put(Material.PORK, Material.GRILLED_PORK);
        cookedMats.put(Material.RAW_FISH, Material.COOKED_FISH);
        cookedMats.put(Material.RAW_BEEF, Material.COOKED_BEEF);
        cookedMats.put(Material.RAW_CHICKEN, Material.COOKED_CHICKEN);
        cookedMats.put(Material.RABBIT, Material.COOKED_RABBIT);
        cookedMats.put(Material.MUTTON, Material.COOKED_MUTTON);

        //ores
        cookedMats.put(Material.IRON_ORE, Material.IRON_INGOT);
        cookedMats.put(Material.GOLD_ORE, Material.GOLD_INGOT);

        //nice stone
        cookedMats.put(Material.SANDSTONE, Material.SANDSTONE);
        cookedMats.put(Material.STONE, Material.COBBLESTONE);
        cookedMats.put(Material.COBBLESTONE, Material.COBBLESTONE);
    }

    @EventHandler
    public void onBlockBreak(CustomBlockBreakEvent event) {
        List<ItemStack> newDrops = new ArrayList<>();
        boolean modified = false;
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            for(ItemStack item : event.getDrops()) {
                if(cookedMats.containsKey(item.getType())) {
                    newDrops.add(new ItemStack(cookedMats.get(item.getType()), item.getAmount()));

                    if(cookedMats.get(item.getType()) == Material.IRON_INGOT)
                        event.setExpToDrop(event.getExpToDrop() + 5);
                    if(cookedMats.get(item.getType()) == Material.GOLD_INGOT)
                        event.setExpToDrop(event.getExpToDrop() + 8);

                    modified = true;
                } else {
                    newDrops.add(item);
                }
            }

            if(modified) {
                event.setDrops(newDrops);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(event.getEntity() == null || event.getDrops() == null) {
                return;
            }
            Collection<ItemStack> drops = event.getDrops();
            event.getDrops().clear();
            for(ItemStack item : drops) {
                if(cookedMats.containsKey(item.getType())) {
                    event.getDrops().add(new ItemStack(cookedMats.get(item.getType()), item.getAmount()));
                }
                else {
                    event.getDrops().add(item);
                }
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "La nourriture et les minerais ",
                "sont directement cuits."
        };
    }
}
