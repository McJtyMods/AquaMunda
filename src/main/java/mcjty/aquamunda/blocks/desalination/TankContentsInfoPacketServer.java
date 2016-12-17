package mcjty.aquamunda.blocks.desalination;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.network.InfoPacketServer;
import mcjty.aquamunda.varia.BlockTools;
import mcjty.immcraft.api.helpers.BlockPosTools;
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
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        BlockPosTools.toBytes(pos, buf);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        return BlockTools.getTE(DesalinationTankTE.class, player.getEntityWorld(), pos)
                .map(p -> new TankContentsInfoPacketClient(pos, p.getContents()));
    }
}
