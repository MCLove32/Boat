package azisaba.net.boat;

import org.bukkit.Material;
import org.bukkit.entity.Boat;

import java.util.*;

public class PlayerUtil {

    private static final List<String> boatList = new ArrayList<>(azisaba.net.boat.Boat.get().getConfig().getStringList("Boat.ice-placeable-block"));

    public static boolean isRoad(Material m) {

        Set<Material> set = new HashSet<>();
        for (String s: boatList) {
            try {
                Material get = Material.valueOf(s);
                set .add(get);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return set.contains(m);
    }

    public static Boat.Type getBoat(int ran) {

        int size = Boat.Type.values().length;
        if (size <= ran) ran = size - 1;

        return Arrays.stream(Boat.Type.values()).toList().get(ran);
    }
}
