package mcjty.aquamunda.blocks.desalination;

import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.generic.GenericAMTE;
import mcjty.aquamunda.hosemultiblock.IHoseConnector;
import mcjty.aquamunda.varia.BlockTools;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.helpers.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.EnumSet;
import java.util.Set;

public class DesalinationBoilerTE extends GenericAMTE implements IHoseConnector, ITickable {

    public static final int MAX_CONTENTS = 10000;       // 10 buckets

    private int contents = 0;
    private float temperature = 20;
    private int counter = 0;

    public static final int TICKS_PER_OPERATION = 20;
    public static final int AMOUNT_PER_OPERATION = 20;

    private Set<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);

    @Override
    public boolean canConnect(EnumFacing blockSide) {
        if (blockSide == EnumFacing.SOUTH || blockSide == EnumFacing.UP || blockSide == EnumFacing.DOWN) {
            return false;
        }
        return !connections.contains(blockSide);
    }

    @Override
    public int connect(EnumFacing blockSide, int networkId, ICableSubType subType) {
        markDirty();
        if (!connections.contains(blockSide)) {
            connections.add(blockSide);
            return blockSide.ordinal() * 4;
        }
        return -1;
    }

    @Override
    public Vec3d getConnectorLocation(int connectorId, EnumFacing rotation) {
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        side = BlockTools.blockToWorldSpace(side, rotation);
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        switch (side) {
            case DOWN:
                return new Vec3d(xCoord+.5f, yCoord, zCoord+.5f);
            case UP:
                return new Vec3d(xCoord+.5f, yCoord+1, zCoord+.5f);
            case NORTH:
                return new Vec3d(xCoord+.5f, yCoord+.5f, zCoord);
            case SOUTH:
                return new Vec3d(xCoord+.5f, yCoord+.5f, zCoord+1);
            case WEST:
                return new Vec3d(xCoord, yCoord+.5f, zCoord+.5f);
            case EAST:
                return new Vec3d(xCoord+1, yCoord+.5f, zCoord+.5f);
            default:
                return new Vec3d(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void disconnect(int connectorId) {
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        connections.remove(side);
        markDirty();;
    }


    // Meant for client-side
    public void setClientInfo(int contents, float temperature) {
        this.contents = contents;
        this.temperature = temperature;
    }

    public int getContents() {
        return contents;
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public void update() {
        if (contents <= 0) {
            // We have no liquid so we cool
            if (temperature > 20) {
                temperature--;
                markDirty();
            }
            return;
        }

        if (!getWorld().isRemote) {
            BlockTools.getTE(DesalinationTankTE.class, getWorld(), getPos().offset(ModBlocks.desalinationBoilerBlock.getRightDirection(getWorld().getBlockState(getPos())))).ifPresent(this::process);
        }
    }

    private void process(DesalinationTankTE te) {
        if (te.getBlockMetadata() != getBlockMetadata()) {
            // Not the same orientation!
            return;
        }

        markDirty();
        counter--;
        if (counter <= 0) {
            counter = TICKS_PER_OPERATION;

            if (isHot()) {
                if (temperature < 100) {
                    temperature += (125.0f - getFilledPercentage()) / 50.0f;
                }
            } else {
                if (temperature > 20) {
                    temperature -= (125.0f - getFilledPercentage()) / 50.0f;
                }
            }

            int amount = (int) (AMOUNT_PER_OPERATION * (temperature - 10) / 90);
            int process = Math.min(amount, contents);
            process = Math.min(process, te.getEmptyLiquidLeft());
            if (process > 0) {
                contents -= process;
                te.setContents(te.getContents() + process);
            }
        }
    }

    private boolean isHot() {
        return BlockTools.isHot(getWorld(), getPos().down());
    }

    @Override
    public Fluid getSupportedFluid() {
        return FluidRegistry.WATER;
    }

    @Override
    public int getMaxExtractPerTick() {
        return 0;
    }

    @Override
    public int extract(int amount) {
        return 0;
    }

    @Override
    public int getMaxInsertPerTick() {
        return 100;
    }

    @Override
    public int getEmptyLiquidLeft() {
        return MAX_CONTENTS - contents;
    }

    @Override
    public int insert(Fluid fluid, int amount) {
        int toadd = Math.min(amount, getEmptyLiquidLeft());
        if (toadd > 0) {
            contents += toadd;
            markDirty();
        }
        return toadd;
    }

    @Override
    public float getFilledPercentage() {
        return contents * 100.0f / MAX_CONTENTS;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        contents = tagCompound.getInteger("contents");
        counter = tagCompound.getInteger("counter");
        temperature = tagCompound.getFloat("temperature");
        connections.clear();
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (tagCompound.hasKey("c" + direction.ordinal())) {
                connections.add(direction);
            }
        }
    }

    @Override
    protected void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper
                .set("contents", contents)
                .set("counter", counter)
                .set("temperature", temperature);
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (connections.contains(direction)) {
                helper.set("c" + direction.ordinal(), true);
            }
        }
    }
}
