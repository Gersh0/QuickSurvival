package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import org.bukkit.NamespacedKey;

public class Keys {
    public static final NamespacedKey PERCENTAGE = new NamespacedKey(QuickSurvival.getInstance(), "Percentage");
    public static final NamespacedKey WAYSTONE = new NamespacedKey(QuickSurvival.getInstance(), "Waystone");
    public static final NamespacedKey BACKPACKLV1 = new NamespacedKey(QuickSurvival.getInstance(), "BackpackLv1");
    public static final NamespacedKey BACKPACKLV2 = new NamespacedKey(QuickSurvival.getInstance(), "BackpackLv2");
    public static final NamespacedKey BACKPACKLV3 = new NamespacedKey(QuickSurvival.getInstance(), "BackpackLv3");
    public static final NamespacedKey BACKPACKLV4 = new NamespacedKey(QuickSurvival.getInstance(), "BackpackLv4");

    public static final NamespacedKey BACKPACKLV5 = new NamespacedKey(QuickSurvival.getInstance(), "BackpackLv5");
    public static final NamespacedKey BACKPACK_CODE = new NamespacedKey(QuickSurvival.getInstance(), "backpack_code");

    public static final NamespacedKey ENDER_GEM = new NamespacedKey(QuickSurvival.getInstance(), "EnderGem");

    public static final NamespacedKey WARP_SCROLL = new NamespacedKey(QuickSurvival.getInstance(), "WarpScroll");

    public static final NamespacedKey MENUOBJECT = new NamespacedKey(QuickSurvival.getInstance(), "MenuObject");

    public static NamespacedKey valueOf(String s) {
        return new NamespacedKey(QuickSurvival.getInstance(), s);
    }
}
