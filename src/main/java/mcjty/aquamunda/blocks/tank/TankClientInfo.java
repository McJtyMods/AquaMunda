package mcjty.aquamunda.blocks.tank;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import mcjty.lib.network.NetworkTools;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TankClientInfo implements IMultiBlockClientInfo {
    private int blockCount = -1;
    private int contents = 0;
    private Fluid fluid = null;

    public TankClientInfo(int blockCount, int contents, Fluid fluid) {
        this.blockCount = blockCount;
        this.contents = contents;
        this.fluid = fluid;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public int getContents() {
        return contents;
    }

    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public void readFromBuf(ByteBuf buf) {
        boolean hasFluid = buf.readBoolean();
        if (hasFluid) {
            fluid = FluidRegistry.getFluid(NetworkTools.readString(buf));
        } else {
            fluid = null;
        }
        contents = buf.readInt();
        blockCount = buf.readInt();
    }

    @Override
    public void writeToBuf(ByteBuf buf) {
        if (fluid != null) {
            buf.writeBoolean(true);
            NetworkTools.writeString(buf, FluidRegistry.getFluidName(fluid));
        } else {
            buf.writeBoolean(false);
        }
        buf.writeInt(contents);
        buf.writeInt(blockCount);
    }

    @Override
    public String toString() {
        return "TankClientInfo{" +
                "blockCount=" + blockCount +
                ", contents=" + contents +
                ", fluid=" + fluid +
                '}';
    }
}
