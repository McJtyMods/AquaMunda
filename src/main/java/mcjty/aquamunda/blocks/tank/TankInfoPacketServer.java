package mcjty.aquamunda.blocks.tank;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.network.InfoPacketServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.Optional;

public class TankInfoPacketServer implements InfoPacketServer {

    private int tankId;

    public TankInfoPacketServer() {
    }

    public TankInfoPacketServer(int tankId) {
        this.tankId = tankId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tankId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(tankId);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        World world = player.worldObj;
        TankNetwork tankNetwork = TankNetwork.get(world);
        Tank tank = tankNetwork.getOrCreateTank(tankId);
        if (tank == null) {
            return Optional.empty();
        } else {
            return Optional.of(new TankInfoPacketClient(tankId, tank.getContents(), tank.getBlockCount(), tank.getFluidName()));
        }
    }
}
