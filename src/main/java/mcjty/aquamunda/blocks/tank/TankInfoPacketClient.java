package mcjty.aquamunda.blocks.tank;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.network.NetworkTools;
import net.minecraft.client.entity.EntityPlayerSP;

public class TankInfoPacketClient implements InfoPacketClient {

    private int id;
    private int contents;
    private int blockCount;
    private String fluidName;

    public TankInfoPacketClient() {
    }

    public TankInfoPacketClient(int id, int contents, int blockCount, String fluidName) {
        this.contents = contents;
        this.fluidName = fluidName;
        this.blockCount = blockCount;
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        contents = buf.readInt();
        blockCount = buf.readInt();
        fluidName = NetworkTools.readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(contents);
        buf.writeInt(blockCount);
        NetworkTools.writeString(buf, fluidName);
    }

    @Override
    public void onMessageClient(EntityPlayerSP player) {
        TankNetwork tankNetwork = TankNetwork.getClientSide();
        Tank tank = tankNetwork.getOrCreateTank(id);
        tank.setContents(contents);
        tank.setClientBlockCount(blockCount);
        tank.setFluidByName(fluidName);

//        tank.getBlocks().stream().forEach(pos -> Minecraft.getMinecraft().theWorld.markBlockRangeForRenderUpdate(pos, pos));
    }
}
