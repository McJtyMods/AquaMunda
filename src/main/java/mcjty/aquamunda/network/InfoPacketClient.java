package mcjty.aquamunda.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public interface InfoPacketClient {

    void fromBytes(ByteBuf buf);
    void toBytes(ByteBuf buf);

    void onMessageClient(EntityPlayer player);
}
