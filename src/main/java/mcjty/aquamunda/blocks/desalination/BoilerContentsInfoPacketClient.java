package mcjty.aquamunda.blocks.desalination;

import io.netty.buffer.ByteBuf;
import mcjty.aquamunda.network.InfoPacketClient;
import mcjty.aquamunda.varia.BlockTools;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;

public class BoilerContentsInfoPacketClient implements InfoPacketClient {

    private BlockPos pos;
    private int contents;
    private float temperature;

    public BoilerContentsInfoPacketClient() {
    }

    public BoilerContentsInfoPacketClient(BlockPos pos, int contents, float temperature) {
        this.pos = pos;
        this.contents = contents;
        this.temperature = temperature;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        contents = buf.readInt();
        temperature = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(contents);
        buf.writeFloat(temperature);
    }

    @Override
    public void onMessageClient(EntityPlayerSP player) {
        BlockTools.getTE(DesalinationBoilerTE.class, MinecraftTools.getWorld(Minecraft.getMinecraft()), pos)
                .ifPresent(p -> p.setClientInfo(contents, temperature));
    }
}
