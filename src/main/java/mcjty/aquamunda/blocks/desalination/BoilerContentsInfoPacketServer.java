package mcjty.aquamunda.blocks.desalination;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.network.InfoPacketServer;
import mcjty.aquamunda.varia.BlockPosTools;
import mcjty.aquamunda.varia.BlockTools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BoilerContentsInfoPacketServer implements InfoPacketServer {

    private BlockPos pos;

    public BoilerContentsInfoPacketServer() {
    }

    public BoilerContentsInfoPacketServer(BlockPos pos) {
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
        return BlockTools.getTE(DesalinationBoilerTE.class, player.worldObj, pos)
                .map(p -> new BoilerContentsInfoPacketClient(pos, p.getContents(), p.getTemperature()));
    }
}
