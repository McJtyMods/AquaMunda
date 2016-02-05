package mcjty.aquamunda.hosemultiblock;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.cables.Cable;
import mcjty.aquamunda.network.InfoPacketClient;
import net.minecraft.client.entity.EntityPlayerSP;

public class HoseInfoPacketClient implements InfoPacketClient {

    private int id;
    private int refcount;

    public HoseInfoPacketClient() {
    }

    public HoseInfoPacketClient(int id, int refcount) {
        this.id = id;
        this.refcount = refcount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        refcount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(refcount);
    }

    @Override
    public void onMessageClient(EntityPlayerSP player) {
        HoseNetwork tankNetwork = HoseNetwork.getClientSide();
        Cable hose = tankNetwork.getOrCreateHose(id);
        hose.setClientBlockCount(refcount);
    }
}
