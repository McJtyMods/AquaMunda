package mcjty.aquamunda.blocks.desalination;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.network.InfoPacketServer;
import mcjty.aquamunda.varia.BlockTools;
import mcjty.lib.network.NetworkTools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class TankContentsInfoPacketServer implements InfoPacketServer {

    private BlockPos pos;

    public TankContentsInfoPacketServer() {
    }

    public TankContentsInfoPacketServer(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = NetworkTools.readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writePos(buf, pos);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        return BlockTools.getTE(DesalinationTankTE.class, player.getEntityWorld(), pos)
                .map(p -> new TankContentsInfoPacketClient(pos, p.getContents()));
    }
}
