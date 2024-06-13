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
import java.util.*;

public abstract class AbstractTab implements CustomTab {

    private final List<String> header;
    private final List<String> footer;

    List<UUID> playersToSend;

    public AbstractTab(List<String> header, List<String> footer) {
        this.header = header;
        this.footer = footer;

        playersToSend = new ArrayList<>();
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
    public void updateTab() {
        if(header.isEmpty() && footer.isEmpty()) return;

        for(UUID uuid : playersToSend) {
            UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(uuid);
            if(uhcPlayer == null) continue;
            Player player = uhcPlayer.getPlayer();
            if(player == null) continue;

            sendTabToPlayer(uhcPlayer, modifyHeader(uhcPlayer, header), modifyFooter(uhcPlayer, footer));
        }
    }

    @Override
    public List<String> modifyHeader(UHCPlayer uhcPlayer,List<String> header) {
        return header;
    }

    @Override
    public List<String> modifyFooter(UHCPlayer uhcPlayer,List<String> footer) {
        return footer;
    }

    private void sendTabToPlayer(UHCPlayer uhcPlayer, List<String> header, List<String> footer) {
        try {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, new ChatComponentText(Strings.join(header, "\n")));

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, new ChatComponentText(Strings.join(footer, "\n")));

            ((CraftPlayer) uhcPlayer.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        }
        catch (Exception ignored) {}
    }

//    @Override
//    public void setHeaderLine(UUID playerId, int line, List<String> lineUpdates) {
//        List<String> playerHeaderVariations = header.get(playerId);
//
//        List<List<String>> listOfVariations = new ArrayList<>();
//        if(playerHeaderVariations.isEmpty()) playerHeaderVariations.add("");
//        int lines = playerHeaderVariations.get(0).split("\n").length;
//
//        for(int i = 0; i < lines; i++) {
//            List<String> lineVariation = new ArrayList<>();
//            for(String variation : playerHeaderVariations) {
//                lineVariation.add(variation.split("\n")[i]);
//            }
//            listOfVariations.add(lineVariation);
//        }
//
//        if(listOfVariations.size() <= line) {
//            for(int i = listOfVariations.size(); i <= line; i++) {
//                listOfVariations.add(Collections.singletonList(""));
//            }
//        }
//
//        listOfVariations.set(line, lineUpdates);
//        playerHeaderVariations = convertToVariationList(listOfVariations);
//
//        header.put(playerId, playerHeaderVariations);
//    }

//    @Override
//    public void setFooterLine(UUID playerId, int line, List<String> lineUpdates) {
//        List<String> playerFooterVariations = footer.get(playerId);
//        List<List<String>> listOfVariations = new ArrayList<>();
//        if(playerFooterVariations.isEmpty()) playerFooterVariations.add("");
//        int lines = playerFooterVariations.get(0).split("\n").length;
//
//        for(int i = 0; i < lines; i++) {
//            List<String> lineVariation = new ArrayList<>();
//            for(String variation : playerFooterVariations) {
//                lineVariation.add(variation.split("\n")[i]);
//            }
//            listOfVariations.add(lineVariation);
//        }
//
//        if(listOfVariations.size() <= line) {
//            for(int i = listOfVariations.size(); i <= line; i++) {
//                listOfVariations.add(Collections.singletonList(""));
//            }
//        }
//
//        listOfVariations.set(line, lineUpdates);
//        playerFooterVariations = convertToVariationList(listOfVariations);
//
//        footer.replace(playerId, playerFooterVariations);
//    }

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
