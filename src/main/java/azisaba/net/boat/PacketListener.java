package azisaba.net.boat;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayInVehicleMove;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static azisaba.net.boat.PlayerUtil.isRoad;

public class PacketListener extends azisaba.net.mmoutils.PacketListener {

    private static final int range = Boat.get().getConfig().getInt("Boat.ice-range", 8);
    private static final Material ice = Material.valueOf(Boat.get().getConfig().getString("Boat.ice-place", "PACKED_ICE"));
    private static final Set<String> worlds = new HashSet<>(Boat.get().getConfig().getStringList("Boat.enable-worlds"));
    private static final boolean isAgainst = Boat.get().getConfig().getBoolean("Boat.disable", false);

    private final Player p;

    public PacketListener(Player p) {
        super(p);
        this.p = p;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof PacketPlayInVehicleMove) {

            if (isAgainst) {
                if (!(worlds.contains(p.getWorld().getName()))) {
                    super.channelRead(ctx, msg);
                    return;
                }

            } else {
                if (worlds.contains(p.getWorld().getName())) {
                    super.channelRead(ctx, msg);
                    return;
                }
            }

            int x = p.getLocation().getBlockX();
            int y = (int) (p.getLocation().getY());
            int z = p.getLocation().getBlockZ();

            double dy = p.getLocation().getY();
            if (dy < y) y = (int) (dy - 0.65);

            int rangeM = range * -1;
            for (int ax = rangeM; ax <= range; ax++) {
                for (int az = rangeM; az <= range; az++) {

                    createPacket(p, ax + x, y, az + z);
                }
            }
        }
        super.channelRead(ctx, msg);
    }

    public static void createPacket(@NotNull Player p, int x, int y, int z) throws NoSuchFieldException, IllegalAccessException {

        BlockPosition position = new BlockPosition(x, y, z);
        IBlockAccess access = ((CraftWorld) p.getWorld()).getHandle();
        IBlockData iBlockData = access.a_(position);

        Material m = CraftBlockData.fromData(iBlockData).getMaterial();
        if (!(isRoad(m))) return;

        IBlockData data = ((CraftBlockData) ice.createBlockData()).getState();
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(position, data);

        azisaba.net.mmoutils.utils.PlayerUtil.sendPacketPlayer(p, packet);

    }
}
