package me.butter.impl.tab;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.tab.CustomTab;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class AbstractTab implements CustomTab {

    private List<String> headerVariations;
    private List<String> footerVariations;

    private PacketPlayOutPlayerListHeaderFooter packet;
    private int headerCount;
    private int footerCount;

    List<UUID> playersToSend;

    public AbstractTab() {
        headerVariations = new ArrayList<>(); headerVariations.add("");
        footerVariations = new ArrayList<>(); footerVariations.add("");

        headerCount = 0;
        footerCount = 0;

        playersToSend = new ArrayList<>();

        packet = new PacketPlayOutPlayerListHeaderFooter();
    }

    @Override
    public void updateTab() {
        if(headerVariations.isEmpty() && footerVariations.isEmpty()) return;

        for(UUID uuid : playersToSend) {
            UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(uuid);
            if(uhcPlayer == null) continue;
            Player player = uhcPlayer.getPlayer();
            if(player == null) continue;

            updatePlayerTab(uhcPlayer);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    protected void updatePlayerTab(UHCPlayer uhcPlayer) {
        try {
            Field header = packet.getClass().getDeclaredField("a");
            header.setAccessible(true);
            if (headerCount >= headerVariations.size()) headerCount = 0;
            header.set(packet, new ChatComponentText(headerVariations.get(headerCount)));
            headerCount++;

            Field footer = packet.getClass().getDeclaredField("b");
            footer.setAccessible(true);
            if (footerCount >= footerVariations.size()) footerCount = 0;
            footer.set(packet, new ChatComponentText(footerVariations.get(footerCount)));
            footerCount++;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPlayer(UHCPlayer uhcPlayer) {
        if(!playersToSend.contains(uhcPlayer.getUniqueId()))
            playersToSend.add(uhcPlayer.getUniqueId());
    }

    @Override
    public void removePlayer(UHCPlayer uhcPlayer) {
        playersToSend.remove(uhcPlayer.getUniqueId());
    }

    @Override
    public void setHeaderLine(int line, String text) {
        setHeaderLine(line, Collections.singletonList(text));
    }

    @Override
    public void setHeaderLine(int line, List<String> lineUpdates) {
        List<List<String>> listOfVariations = new ArrayList<>();
        if(headerVariations.isEmpty()) headerVariations.add("");
        int lines = headerVariations.get(0).split("\n").length;

        for(int i = 0; i < lines; i++) {
            List<String> lineVariation = new ArrayList<>();
            for(String variation : headerVariations) {
                lineVariation.add(variation.split("\n")[i]);
            }
            listOfVariations.add(lineVariation);
        }

        if(listOfVariations.size() <= line) {
            for(int i = listOfVariations.size(); i <= line; i++) {
                listOfVariations.add(Collections.singletonList(""));
            }
        }

        listOfVariations.set(line, lineUpdates);
        headerVariations = convertToVariationList(listOfVariations);
    }

    @Override
    public void setFooterLine(int line, String text) {
        setFooterLine(line, Collections.singletonList(text));
    }

    @Override
    public void setFooterLine(int line, List<String> lineUpdates) {
        List<List<String>> listOfVariations = new ArrayList<>();
        if(footerVariations.isEmpty()) footerVariations.add("");
        int lines = footerVariations.get(0).split("\n").length;

        for(int i = 0; i < lines; i++) {
            List<String> lineVariation = new ArrayList<>();
            for(String variation : footerVariations) {
                lineVariation.add(variation.split("\n")[i]);
            }
            listOfVariations.add(lineVariation);
        }

        if(listOfVariations.size() <= line) {
            for(int i = listOfVariations.size(); i <= line; i++) {
                listOfVariations.add(Collections.singletonList(""));
            }
        }

        listOfVariations.set(line, lineUpdates);
        footerVariations = convertToVariationList(listOfVariations);
    }

    private List<String> convertToVariationList(List<List<String>> listOfVariations) {
        int maxVariations = 0;

        for(List<String> line : listOfVariations) {
            if(line.size() > maxVariations) maxVariations = line.size();
        }

        List<String> variations = new ArrayList<>();
        for(int i = 0; i < maxVariations; i++) {
            List<String> variation = new ArrayList<>();
            for (List<String> listOfVariation : listOfVariations) {
                int index = i % listOfVariation.size();
                variation.add("§r" + listOfVariation.get(index));
            }
            variations.add(Strings.join(variation, "\n"));
        }

        return variations;
    }
}
