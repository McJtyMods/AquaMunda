package mcjty.aquamunda.blocks.desalination;

import mcjty.aquamunda.blocks.generic.GenericTE;
import mcjty.aquamunda.cables.CableSubType;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.hosemultiblock.IHoseConnector;
import mcjty.aquamunda.varia.NBTHelper;
import mcjty.aquamunda.varia.Vector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;

import java.util.HashMap;
import java.util.Map;

public class DesalinationTankTE extends GenericTE implements IHoseConnector {

    public static final int MAX_CONTENTS = 10000;       // 10 buckets

    private int contents = 0;

    private Map<EnumFacing,boolean[]> connections = new HashMap<>();

    public int getContents() {
        return contents;
    }

    public void setContents(int contents) {
        this.contents = contents;
        markDirty();
    }

    @Override
    public boolean canConnect(EnumFacing blockSide) {
        if (blockSide == EnumFacing.SOUTH || blockSide == EnumFacing.UP) {
            return false;
        }
        if (connections.containsKey(blockSide)) {
            boolean[] c = connections.get(blockSide);
            return !c[0] || !c[1] || !c[2];
        }
        return true;
    }

    @Override
    public int connect(EnumFacing blockSide, int networkId, CableSubType subType) {
        markDirty();
        if (!connections.containsKey(blockSide)) {
            connections.put(blockSide, new boolean[] { false, false, false });
        }

        boolean[] c = connections.get(blockSide);
        for (int i = 0 ; i < 3 ; i++) {
            if (!c[i]) {
                c[i] = true;
                return blockSide.ordinal() * 4 + i;
            }
        }
        return -1;
    }

    @Override
    public Vector getConnectorLocation(int connectorId, EnumFacing rotation) {
        float dx = 0;
        switch (connectorId & 3) {
            case 0: dx = .5f; break;
            case 1: dx = .3f; break;
            case 2: dx = .8f; break;
        }
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        switch (side) {
            case DOWN:
                return new Vector(xCoord+dx, yCoord, zCoord+.5f);
            case UP:
                return new Vector(xCoord+dx, yCoord+1, zCoord+.5f);
            case NORTH:
                return new Vector(xCoord+dx, yCoord+.5f, zCoord+.1f);
            case SOUTH:
                return new Vector(xCoord+dx, yCoord+.5f, zCoord+0.9f);
            case WEST:
                return new Vector(xCoord+.1f, yCoord+.5f, zCoord+dx);
            case EAST:
                return new Vector(xCoord+0.9f, yCoord+.5f, zCoord+dx);
            default:
                return new Vector(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void disconnect(int connectorId) {
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        connections.get(side)[connectorId & 3] = false;
        markDirty();;
    }

    @Override
    public Fluid getSupportedFluid() {
        return FluidSetup.freshWater;
    }

    @Override
    public int getMaxExtractPerTick() {
        return 100;
    }

    @Override
    public int extract(int amount) {
        int toext = Math.min(amount, contents);
        if (toext > 0) {
            contents -= toext;
            markDirty();
        }
        return toext;
    }

    @Override
    public int getMaxInsertPerTick() {
        return 0;
    }

    @Override
    public int getEmptyLiquidLeft() {
        return MAX_CONTENTS - contents;
    }

    @Override
    public int insert(Fluid fluid, int amount) {
        return 0;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }


    @Override
    public float getFilledPercentage() {
        return contents * 100.0f / MAX_CONTENTS;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        contents = tagCompound.getInteger("contents");
        connections.clear();
        for (EnumFacing direction : EnumFacing.VALUES) {
            String keyBase = "c" + direction.ordinal();
            if (tagCompound.hasKey(keyBase + "_0")) {
                boolean[] c = new boolean[] {
                        tagCompound.getBoolean(keyBase+"_0"),
                        tagCompound.getBoolean(keyBase+"_1"),
                        tagCompound.getBoolean(keyBase+"_2")
                };
                connections.put(direction, c);
            }
        }
    }

    @Override
    protected void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper.set("contents", contents);
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (connections.containsKey(direction)) {
                boolean[] c = connections.get(direction);
                String keyBase = "c" + direction.ordinal();
                helper.set(keyBase + "_0", c[0]);
                helper.set(keyBase + "_1", c[1]);
                helper.set(keyBase + "_2", c[2]);
            }
        }

    }
}
