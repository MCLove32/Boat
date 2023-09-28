package azisaba.net.boat;

import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

public class PlayerUtil {

    @NotNull
    public static Channel getChannel(@NotNull Player player) throws IllegalAccessException, NoSuchFieldException {

        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        Field manager = connection.getClass().getDeclaredField("h");
        manager.setAccessible(true);
        NetworkManager object = (NetworkManager) manager.get(connection);

        return object.m;
    }

    public static void sendPacketPlayer(Player p, Packet<?> packet) {

        ((CraftPlayer) p).getHandle().b.a(packet);
    }

    public static boolean isRoad(Material m) {
        return m == Material.WHITE_CONCRETE || m == Material.LIGHT_GRAY_CONCRETE || m == Material.GRAY_CONCRETE || m == Material.BLACK_CONCRETE;
    }

    public static Boat.Type getBoat(int ran) {

        int size = Boat.Type.values().length;
        if (size <= ran) ran = size - 1;

        return Arrays.stream(Boat.Type.values()).toList().get(ran);
    }
}
