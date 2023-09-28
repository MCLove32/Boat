package azisaba.net.boat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Boat extends JavaPlugin {

    private static Boat boat;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        boat = this;
        // Plugin startup logic
        PluginManager pm = getServer().getPluginManager();
        Objects.requireNonNull(getCommand("boat")).setExecutor(new BoatCommand());
        pm.registerEvents(new BoatListener(), this);

        for (Player p: Bukkit.getOnlinePlayers()) {
            try {
                Channel channel = PlayerUtil.getChannel(p);
                channel.pipeline().addBefore("packet_handler", "boat", new PacketListener(p));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                getLogger().warning("Failed to inject channel handler to " + p.getName() + ": catch :" + e);
            }
        }
    }

    public static Boat get() {return boat;}

    @Override
    public void onDisable() {

        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                Channel channel = PlayerUtil.getChannel(p);
                ChannelPipeline pipeline = channel.pipeline();
                if (pipeline.get("boat") != null) {
                    pipeline.remove("boat");
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
