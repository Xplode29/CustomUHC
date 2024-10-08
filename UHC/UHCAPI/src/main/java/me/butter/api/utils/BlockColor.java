package me.butter.api.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class BlockColor {
    // This stores all the Chatcolors and DyeColors for each block
    private static List<BlockColorData> colorList = new ArrayList<BlockColorData>();

    //This is the instance of this class.
    private static BlockColor bc = new BlockColor();

    private BlockColor(){}

    //Add all of the materials.
    static {

        // Glass
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.BLACK.ordinal(),
                ColorData.BLACK);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.BLUE.ordinal(),
                ColorData.BLUE);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.BROWN.ordinal(),
                ColorData.BROWN);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.CYAN.ordinal(),
                ColorData.CYAN);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.GRAY.ordinal(),
                ColorData.DARK_GRAY);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.GREEN.ordinal(),
                ColorData.DARK_GREEN);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.LIGHT_BLUE.ordinal(),
                ColorData.AQUA);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.LIME.ordinal(),
                ColorData.GREEN);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.MAGENTA.ordinal(),
                ColorData.LIGHT_PURPLE);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.ORANGE.ordinal(),
                ColorData.ORANGE);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.RED.ordinal(),
                ColorData.RED);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.PINK.ordinal(),
                ColorData.PINK);
        bc.new BlockColorData(Material.STAINED_GLASS, DyeColor.SILVER.ordinal(),
                ColorData.LIGHT_GRAY);

        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.BLACK.ordinal(),
                ColorData.BLACK);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.BLUE.ordinal(),
                ColorData.BLUE);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.BROWN.ordinal(),
                ColorData.BROWN);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.CYAN.ordinal(),
                ColorData.CYAN);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.GRAY.ordinal(),
                ColorData.DARK_GRAY);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.GREEN.ordinal(),
                ColorData.DARK_GREEN);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.LIGHT_BLUE.ordinal(),
                ColorData.AQUA);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.LIME.ordinal(),
                ColorData.GREEN);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.MAGENTA.ordinal(),
                ColorData.LIGHT_PURPLE);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.ORANGE.ordinal(),
                ColorData.ORANGE);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.RED.ordinal(),
                ColorData.RED);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.PINK.ordinal(),
                ColorData.PINK);
        bc.new BlockColorData(Material.STAINED_GLASS_PANE, DyeColor.SILVER.ordinal(),
                ColorData.LIGHT_GRAY);

        // Clay
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.BLACK.ordinal(),
                ColorData.BLACK);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.BLUE.ordinal(),
                ColorData.BLUE);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.BROWN.ordinal(),
                ColorData.BROWN);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.CYAN.ordinal(),
                ColorData.CYAN);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.GRAY.ordinal(),
                ColorData.DARK_GRAY);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.GREEN.ordinal(),
                ColorData.DARK_GREEN);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.LIGHT_BLUE.ordinal(),
                ColorData.AQUA);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.LIME.ordinal(),
                ColorData.GREEN);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.MAGENTA.ordinal(),
                ColorData.LIGHT_PURPLE);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.ORANGE.ordinal(),
                ColorData.ORANGE);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.RED.ordinal(),
                ColorData.RED);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.PINK.ordinal(),
                ColorData.PINK);
        bc.new BlockColorData(Material.STAINED_CLAY, DyeColor.SILVER.ordinal(),
                ColorData.LIGHT_GRAY);

        // WoolColors
        bc.new BlockColorData(Material.WOOL, DyeColor.BLACK.ordinal(),
                ColorData.BLACK);
        bc.new BlockColorData(Material.WOOL, DyeColor.BLUE.ordinal(),
                ColorData.BLUE);
        bc.new BlockColorData(Material.WOOL, DyeColor.BROWN.ordinal(),
                ColorData.BROWN);
        bc.new BlockColorData(Material.WOOL, DyeColor.CYAN.ordinal(),
                ColorData.CYAN);
        bc.new BlockColorData(Material.WOOL, DyeColor.GRAY.ordinal(),
                ColorData.DARK_GRAY);
        bc.new BlockColorData(Material.WOOL, DyeColor.GREEN.ordinal(),
                ColorData.DARK_GREEN);
        bc.new BlockColorData(Material.WOOL, DyeColor.LIGHT_BLUE.ordinal(),
                ColorData.AQUA);
        bc.new BlockColorData(Material.WOOL, DyeColor.LIME.ordinal(),
                ColorData.GREEN);
        bc.new BlockColorData(Material.WOOL, DyeColor.MAGENTA.ordinal(),
                ColorData.LIGHT_PURPLE);
        bc.new BlockColorData(Material.WOOL, DyeColor.ORANGE.ordinal(),
                ColorData.ORANGE);
        bc.new BlockColorData(Material.WOOL, DyeColor.RED.ordinal(),
                ColorData.RED);
        bc.new BlockColorData(Material.WOOL, DyeColor.PINK.ordinal(),
                ColorData.PINK);
        bc.new BlockColorData(Material.WOOL, DyeColor.SILVER.ordinal(),
                ColorData.LIGHT_GRAY);
        bc.new BlockColorData(Material.WOOL, DyeColor.WHITE.ordinal(),
                ColorData.WHITE);
    }

    /**
     * Returns the ChatColor associated with the Type ID.
     *
     * @param id
     * @return ChatColor
     */
    public static ChatColor getChatColor(int id) {
        for (BlockColorData bcd : colorList) {
            if (bcd.typeID == id && !bcd.hasData) {
                return bcd.getChatColor();
            }
        }
        return ChatColor.BLACK;
    }

    /**
     * Returns the DyeColor associated with the Type ID.
     *
     * @param id
     * @return DyeColor
     */
    public static DyeColor getDyeColor(int id) {
        for (BlockColorData bcd : colorList) {
            if (bcd.typeID == id && !bcd.hasData) {
                return bcd.getDyeColor();
            }
        }
        return DyeColor.BLACK;
    }

    /**
     * Returns the ChatColor associated with the Type.
     *
     * @param mat
     * @return ChatColor
     */
    public static ChatColor getChatColor(Material mat) {
        for (BlockColorData bcd : colorList) {
            if (bcd.usesMaterials() && bcd.type == mat && !bcd.hasData) {
                return bcd.getChatColor();
            }
        }
        return ChatColor.BLACK;
    }

    /**
     * Returns the DyeColorColor associated with the Type.
     *
     * @param mat
     * @return DyeColor
     */
    public static DyeColor getDyeColor(Material mat) {
        for (BlockColorData bcd : colorList) {
            if (bcd.usesMaterials() && bcd.type == mat && !bcd.hasData) {
                return bcd.getDyeColor();
            }
        }
        return DyeColor.BLACK;
    }

    /**
     * Returns the ChatColor associated with the Type ID and data.
     *
     * @param id
     * @param data
     * @return ChatColor
     */
    public static ChatColor getChatColor(int id, short data) {
        for (BlockColorData bcd : colorList) {
            if (bcd.typeID == id && bcd.getData() == data) {
                return bcd.getChatColor();
            }
        }
        return ChatColor.BLACK;
    }
    /**
     * Returns the DyeColor associated with the Type ID and data.
     * @param id
     * @param data
     * @return DyeColor
     */
    public static DyeColor getDyeColor(int id, short data) {
        for (BlockColorData bcd : colorList) {
            if (bcd.typeID == id && bcd.getData() == data) {
                return bcd.getDyeColor();
            }
        }
        return DyeColor.BLACK;
    }
    /**
     * Returns the ChatColor associated with the Type  and data.
     * @param mat
     * @param data
     * @return ChatColor
     */
    public static ChatColor getChatColor(Material mat, short data) {
        for (BlockColorData bcd : colorList) {
            if (bcd.usesMaterials() && bcd.type == mat && bcd.getData() == data) {
                return bcd.getChatColor();
            }
        }
        return ChatColor.BLACK;
    }
    /**
     * Returns the DyeColorColor associated with the Type and data.
     * @param mat
     * @param data
     * @return
     */
    public static DyeColor getDyeColor(Material mat, short data) {
        for (BlockColorData bcd : colorList) {
            if (bcd.usesMaterials() && bcd.type == mat && bcd.getData() == data) {
                return bcd.getDyeColor();
            }
        }
        return DyeColor.BLACK;
    }
    /**
     * Gets the Type associated with the DyeColor
     * @param dc
     * @return Material
     */
    public static Material getMaterialWithColor(DyeColor dc) {
        for (BlockColorData bdc : colorList) {
            if (bdc.getDyeColor().equals(dc) && !bdc.hasData)
                return bdc.getType();
        }
        return Material.STONE;
    }
    /**
     * Gets the Type associated with the ChatColorColor
     * @param cc
     * @return Material
     */
    public static Material getMaterialWithColor(ChatColor cc) {
        for (BlockColorData bdc : colorList) {
            if (bdc.getChatColor().equals(cc) && !bdc.hasData)
                return bdc.getType();
        }
        return Material.STONE;
    }
    /**
     * Gets the TypeID associated with the DyeColor
     * @param dc
     * @return int
     */
    public static int getIDWithColor(DyeColor dc) {
        for (BlockColorData bdc : colorList) {
            if (bdc.getDyeColor().equals(dc) && !bdc.hasData)
                return bdc.getTypeID();
        }
        return 1;
    }
    /**
     * Gets the TypeID associated with the ChatColor
     * @param cc
     * @return int
     */
    public static int getIDWithColor(ChatColor cc) {
        for (BlockColorData bdc : colorList) {
            if (bdc.getChatColor().equals(cc) && !bdc.hasData)
                return bdc.getTypeID();
        }
        return 1;
    }

    enum ColorData {
        BLACK(ChatColor.BLACK, DyeColor.BLACK), BLUE(ChatColor.BLUE,
                DyeColor.BLUE), BROWN(ChatColor.DARK_RED, DyeColor.BROWN), CYAN(
                ChatColor.AQUA, DyeColor.CYAN), DARK_GRAY(ChatColor.DARK_GRAY,
                DyeColor.GRAY), DARK_GREEN(ChatColor.DARK_GREEN, DyeColor.GREEN), AQUA(
                ChatColor.BLUE, DyeColor.LIGHT_BLUE), GREEN(ChatColor.GREEN,
                DyeColor.LIME), LIGHT_PURPLE(ChatColor.LIGHT_PURPLE,
                DyeColor.MAGENTA), ORANGE(ChatColor.GOLD, DyeColor.ORANGE), PINK(
                ChatColor.RED, DyeColor.PINK), LIGHT_GRAY(ChatColor.GRAY,
                DyeColor.SILVER), WHITE(ChatColor.WHITE, DyeColor.WHITE), YELLOW(
                ChatColor.YELLOW, DyeColor.YELLOW), RED(ChatColor.RED,
                DyeColor.RED);

        ChatColor cC;
        DyeColor dC;

        private ColorData(ChatColor cc, DyeColor dc) {
            this.cC = cc;
            this.dC = dc;
        }

        public ChatColor getChatColor() {
            return this.cC;
        }

        public DyeColor getDyeColor() {
            return this.dC;
        }
    }

    class BlockColorData {

        boolean usesMaterial;
        boolean hasData = false;

        int typeID;
        Material type;

        ColorData c;

        short data = 0;

        public BlockColorData(int id, int data, ColorData cd) {
            this.typeID = id;
            this.usesMaterial = false;
            this.c = cd;
            this.data = (short) data;
            this.hasData = true;
            colorList.add(this);
        }

        @SuppressWarnings("deprecation")
        public BlockColorData(Material mat, int data, ColorData cd) {
            this.type = mat;
            this.typeID = mat.getId();
            this.usesMaterial = true;
            this.c = cd;
            this.data = (short) data;
            this.hasData = true;
            colorList.add(this);
        }

        public BlockColorData(int id, ColorData cd) {
            this.typeID = id;
            this.usesMaterial = false;
            this.c = cd;
            colorList.add(this);
        }

        @SuppressWarnings("deprecation")
        public BlockColorData(Material mat, ColorData cd) {
            this.type = mat;
            this.typeID = mat.getId();
            this.usesMaterial = true;
            this.c = cd;
            colorList.add(this);
        }

        public short getData() {
            return this.data;
        }

        public boolean usesMaterials() {
            return this.usesMaterial;
        }

        public int getTypeID() {
            return this.typeID;
        }

        public Material getType() {
            return this.type;
        }

        public ChatColor getChatColor() {
            return this.c.getChatColor();
        }

        public DyeColor getDyeColor() {
            return this.c.getDyeColor();
        }

        public boolean hasData() {
            return this.hasData;
        }
    }

}
