package azisaba.net.boat;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayInVehicleMove;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PacketListener extends ChannelDuplexHandler {

    private final Player p;
    public PacketListener(Player p) {
        this.p = p;
    }
    private static final int range = Boat.get().getConfig().getInt("Boat.ice-range", 8);
    private static final Material ice = Material.valueOf(Boat.get().getConfig().getString("Boat.ice-place", "PACKED_ICE"));

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof PacketPlayInVehicleMove) {

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

    public static void createPacket(@NotNull Player p, int x, int y, int z) {

        BlockPosition position = new BlockPosition(x, y, z);
        IBlockAccess access = ((CraftWorld) p.getWorld()).getHandle();
        IBlockData iBlockData = access.a_(position);

        Material m = CraftBlockData.fromData(iBlockData).getMaterial();
        if (!(PlayerUtil.isRoad(m))) return;

        IBlockData data = ((CraftBlockData) ice.createBlockData()).getState();
        PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(position, data);

        PlayerUtil.sendPacketPlayer(p, packet);

    }
}
