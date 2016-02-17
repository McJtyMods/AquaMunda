package mcjty.aquamunda.blocks.customblocks;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.environment.EnvironmentData;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.network.InfoPacketServer;
import mcjty.aquamunda.varia.BlockPosTools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;

import java.util.Optional;

public class FarmLandMoistnessPacketServer implements InfoPacketServer {

    private BlockPos pos;

    public FarmLandMoistnessPacketServer() {
    }

    public FarmLandMoistnessPacketServer(BlockPos pos) {
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
        EnvironmentData environment = EnvironmentData.getEnvironmentData(player.worldObj);
        byte level = environment.getData().get(player.worldObj.provider.getDimensionId(), pos);
        System.out.println("level = " + level);
        return Optional.of(new FarmLandMoistnessPacketClient(level));
    }
}
