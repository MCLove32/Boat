package azisaba.net.boat;

import azisaba.net.mmoutils.MMOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static azisaba.net.boat.PlayerUtil.getBoat;

public class BoatListener implements Listener {

    public static final Random ran = new Random();
    private static final Set<String> ct = new HashSet<>();
    private static final boolean needType = azisaba.net.boat.Boat.get().getConfig().getBoolean("Boat.boat-type", true);
    private static final Boat.Type BoatType = Boat.Type.valueOf(azisaba.net.boat.Boat.get().getConfig().getString("Boat.boat-type-set", "OAK"));

    @EventHandler
    public void onInteract(@NotNull PlayerInteractEvent e) {

        Block b = e.getClickedBlock();
        if (b == null) return;
        if (!(PlayerUtil.isRoad(b.getType()))) return;

        Player p = e.getPlayer();
        if (!p.isSneaking() || e.getAction().isLeftClick()) return;
        if (ct.contains(p.getName())) return;
        ct.add(p.getName());
        Bukkit.getScheduler().runTaskLaterAsynchronously(azisaba.net.boat.Boat.get(), ()-> ct.remove(p.getName()), 10L);

        float yaw = p.getBodyYaw();
        Boat.Type type = getBoat(ran.nextInt(6));

        if (!(needType)) type = BoatType;

        Entity entity = p.getWorld().spawnEntity(b.getRelative(e.getBlockFace()).getLocation(), EntityType.BOAT);
        if (entity instanceof Boat boat) {

            boat.setBoatType(type);
            Location loc = boat.getLocation();
            boat.teleport(new Location(boat.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY(), loc.getBlockZ() + 0.5, yaw, 90));
        }
    }

    @EventHandler
    public void onExit(@NotNull VehicleExitEvent e) {

        if (e.getExited() instanceof Player && e.getVehicle() instanceof Boat boat) {
            boat.remove();
        }
    }

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e) {

        MMOUtils.getUtils().packetSetUP(e.getPlayer(), azisaba.net.boat.Boat.id, new PacketListener(e.getPlayer()));
    }
}
