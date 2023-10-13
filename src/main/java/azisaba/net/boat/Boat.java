package azisaba.net.boat;

import azisaba.net.mmoutils.MMOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Boat extends JavaPlugin {

    private static Boat boat;
    public static final String id = "boat";

    @Override
    public void onEnable() {

        saveDefaultConfig();
        boat = this;
        // Plugin startup logic
        PluginManager pm = getServer().getPluginManager();
        Objects.requireNonNull(getCommand("boat")).setExecutor(new BoatCommand());
        pm.registerEvents(new BoatListener(), this);

        Bukkit.getOnlinePlayers().forEach(player -> MMOUtils.getUtils().packetSetUP(player, id, new PacketListener(player)));
    }

    public static Boat get() {return boat;}

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> MMOUtils.getUtils().packetRemove(player, id));
    }
}
