package mcjty.aquamunda.hosemultiblock;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.cables.Cable;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.network.InfoPacketServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.Optional;

public class HoseInfoPacketServer implements InfoPacketServer {

    private int hoseId;

    public HoseInfoPacketServer() {
    }

    public HoseInfoPacketServer(int hoseId) {
        this.hoseId = hoseId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        hoseId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(hoseId);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        World world = player.worldObj;
        HoseNetwork hoseNetwork = HoseNetwork.get(world);
        Cable hose = hoseNetwork.getOrCreateHose(hoseId);
        if (hose == null) {
            return Optional.empty();
        } else {
            return Optional.of(new HoseInfoPacketClient(hoseId, hose.getBlockCount()));
        }
    }
}
