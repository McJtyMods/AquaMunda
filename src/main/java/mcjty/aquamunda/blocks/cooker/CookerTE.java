package mcjty.aquamunda.blocks.cooker;

import mcjty.aquamunda.blocks.generic.GenericInventoryTE;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.hosemultiblock.IHoseConnector;
import mcjty.aquamunda.varia.BlockTools;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.api.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class CookerTE extends GenericInventoryTE implements IHoseConnector, ITickable {

    public static final int INPUT_PER_TICK = 3;
    public static final int MAX_AMOUNT = 2000;
    public static final int TICKS_PER_OPERATION = 20;

    public static final int SLOT_INPUT = 0;

    private int amount = 0;
    private float temperature = 20;
    private int counter = 0;

    public CookerTE() {
        super(1);
        int i = SLOT_INPUT;

        float boundsdx = .25f;
        float boundsdy = .33f;
        double renderdx = 0.19;
        double renderdz = 0.29;
        int y = 1;
        int x = 1;
        addInterfaceHandle(new InputInterfaceHandle().slot(i++).side(EnumFacing.UP).
                bounds(boundsdx * x, boundsdy * y, boundsdx * (x + 1), boundsdy * (y + 1)).
                renderOffset(new Vec3d(renderdx * (x - 1) - renderdx / 2.0, 0.9, renderdz * (y - 1) - .02)).
                scale(.60f));
    }

    private Set<EnumFacing> connections = EnumSet.noneOf(EnumFacing.class);

    @Override
    public boolean canConnect(EnumFacing blockSide) {
        if (blockSide == EnumFacing.UP || blockSide == EnumFacing.DOWN) {
            return false;
        }
        return !connections.contains(blockSide);
    }

    @Override
    public int connect(EnumFacing blockSide, int networkId, ICableSubType subType) {
        markDirty();
        if (!connections.contains(blockSide)) {
            connections.add(blockSide);
            return blockSide.ordinal();
        }
        return -1;
    }

    @Override
    public Vec3d getConnectorLocation(int connectorId, EnumFacing rotation) {
        EnumFacing side = EnumFacing.values()[connectorId];
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        switch (side) {
            case DOWN:
                return new Vec3d(xCoord+.5f, yCoord, zCoord+.5f);
            case UP:
                return new Vec3d(xCoord+.5f, yCoord+1, zCoord+.5f);
            case NORTH:
                return new Vec3d(xCoord+.5f, yCoord+.1f, zCoord);
            case SOUTH:
                return new Vec3d(xCoord+.5f, yCoord+.1f, zCoord+1);
            case WEST:
                return new Vec3d(xCoord, yCoord+.1f, zCoord+.5f);
            case EAST:
                return new Vec3d(xCoord+1, yCoord+.1f, zCoord+.5f);
            default:
                return new Vec3d(xCoord, yCoord, zCoord);
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        markDirtyClient();
    }

    public float getTemperature() {
        return temperature;
    }

    @Override
    public void disconnect(int connectorId) {
        EnumFacing side = EnumFacing.values()[connectorId];
        connections.remove(side);
        markDirty();
    }

    @Override
    public int extract(int amount) {
        return 0;
    }

    @Override
    public Fluid getSupportedFluid() {
        return FluidSetup.freshWater;
    }

    @Override
    public int getMaxExtractPerTick() {
        return 0;
    }

    @Override
    public int getMaxInsertPerTick() {
        return INPUT_PER_TICK;
    }

    @Override
    public int getEmptyLiquidLeft() {
        return MAX_AMOUNT - amount;
    }

    @Override
    public int insert(Fluid fluid, int a) {
        int inserted = Math.min(MAX_AMOUNT - amount, a);
        amount += inserted;
        return inserted;
    }

    @Override
    public float getFilledPercentage() {
        return 100.0f * amount / MAX_AMOUNT;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        EnumBoiling oldBoiling = getBoilingState();
        EnumContents oldContents = getContentsState();

        super.onDataPacket(net, packet);
        if (getWorld().isRemote) {
            // If needed send a render update.
            EnumBoiling newBoiling = getBoilingState();
            EnumContents newContents = getContentsState();
            if ((!newBoiling.equals(oldBoiling)) || (!newContents.equals(oldContents))) {
                getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
            }
        }
    }



    public EnumBoiling getBoilingState() {
        if (temperature < 70) {
            return EnumBoiling.COLD;
        } else if (temperature < 97) {
            return EnumBoiling.HOT;
        } else {
            return EnumBoiling.BOILING;
        }
    }

    public EnumContents getContentsState() {
        float filled = getFilledPercentage();
        if (filled < 1) {
            return EnumContents.EMPTY;
        } else if (filled < 40) {
            return EnumContents.LOW;
        } else if (filled < 80) {
            return EnumContents.MEDIUM;
        } else {
            return EnumContents.FULL;
        }
    }

    private void changeTemperature(float newtemp) {
        if (temperature == newtemp) {
            return;
        }
        EnumBoiling oldBoiling = getBoilingState();
        temperature = newtemp;
        EnumBoiling newBoiling = getBoilingState();
        if (oldBoiling != newBoiling) {
            markDirtyClient();
        } else {
            markDirty();
        }
    }


    @Override
    public void update() {
        if (!getWorld().isRemote) {
            if (amount <= 0) {
                // We have no liquid so we cool
                if (temperature > 20) {
                    changeTemperature(temperature-1);
                    markDirty();
                }
                return;
            }
            counter--;
            if (counter <= 0) {
                counter = TICKS_PER_OPERATION;

                if (isHot()) {
                    if (temperature < 100) {
                        changeTemperature(temperature + (125.0f - getFilledPercentage()) / 50.0f);
                    }
                } else {
                    if (temperature > 20) {
                        changeTemperature(temperature - (125.0f - getFilledPercentage()) / 50.0f);
                    }
                }
            }
            markDirty();
        }
    }

    private boolean isHot() {
        return BlockTools.isHot(getWorld(), getPos().down());
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    private static Random random = new Random();

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        amount = tagCompound.getInteger("amount");
        temperature = tagCompound.getFloat("temperature");
        counter = tagCompound.getInteger("counter");
        connections.clear();
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (tagCompound.hasKey("c" + direction.ordinal())) {
                connections.add(direction);
            }
        }
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper
                .set("amount", amount)
                .set("temperature", temperature)
                .set("counter", counter);
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (connections.contains(direction)) {
                helper.set("c" + direction.ordinal(), true);
            }
        }
    }
}
