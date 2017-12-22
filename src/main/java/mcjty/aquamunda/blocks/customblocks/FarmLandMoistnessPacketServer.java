package mcjty.aquamunda.blocks.customblocks;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.environment.EnvironmentData;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.network.InfoPacketServer;
import mcjty.lib.network.NetworkTools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

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
        pos = NetworkTools.readPos(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writePos(buf, pos);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        boolean freshwater = CustomFarmLand.freshWaterNearby(player.getEntityWorld(), pos);
        if (!freshwater) {
            return Optional.of(new FarmLandMoistnessPacketClient(-1));
        }
        EnvironmentData environment = EnvironmentData.getEnvironmentData(player.getEntityWorld());
        byte level = environment.getData().get(player.getEntityWorld().provider.getDimension(), pos);
        return Optional.of(new FarmLandMoistnessPacketClient(level));
    }
}
