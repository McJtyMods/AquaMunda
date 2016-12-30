package mcjty.aquamunda.blocks.tank;

import mcjty.aquamunda.blocks.generic.GenericAMTE;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.hosemultiblock.IHoseConnector;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.immcraft.api.multiblock.IMultiBlockTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.HashMap;
import java.util.Map;

public class TankTE extends GenericAMTE implements IHoseConnector, IMultiBlockTile<Tank> {

    public static final int MAX_CONTENTS = 10000;       // 10 buckets
    public static final int ADD_RAIN = 200;             // How much to add when it is raining
    public static final int EVAPORATE_AMOUNT = 5;       // How much to remove on evaporation

    private int networkId = -1;

    public TankTE() {
    }

    private Map<EnumFacing,boolean[]> connections = new HashMap<>();

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        int oldId = networkId;
        super.onDataPacket(net, packet);
        if (oldId != networkId) {
            // Make sure the tank is re-rendered when the ID changes
            getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }

    @Override
    public boolean canConnect(EnumFacing blockSide) {
        if (connections.containsKey(blockSide)) {
            boolean[] c = connections.get(blockSide);
            return !c[0] || !c[1] || !c[2];
        }
        return true;
    }

    @Override
    public int connect(EnumFacing blockSide, int networkId, ICableSubType subType) {
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
    public Vec3d getConnectorLocation(int connectorId, EnumFacing rotation) {
        float dx = 0;
        switch (connectorId & 3) {
            case 0: dx = .5f; break;
            case 1: dx = .3f; break;
            case 2: dx = .8f; break;
        }
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        switch (side) {
            case DOWN:
                return new Vec3d(xCoord+dx, yCoord, zCoord+.5f);
            case UP:
                return new Vec3d(xCoord+dx, yCoord+1, zCoord+.5f);
            case NORTH:
                return new Vec3d(xCoord+dx, yCoord+.5f, zCoord);
            case SOUTH:
                return new Vec3d(xCoord+dx, yCoord+.5f, zCoord+1);
            case WEST:
                return new Vec3d(xCoord, yCoord+.5f, zCoord+dx);
            case EAST:
                return new Vec3d(xCoord+1, yCoord+.5f, zCoord+dx);
            default:
                return new Vec3d(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void disconnect(int connectorId) {
        EnumFacing side = EnumFacing.values()[connectorId >> 2];
        connections.get(side)[connectorId & 3] = false;
        markDirty();
    }

    @Override
    public int extract(int amount) {
        Tank tank = getTank();
        if (tank == null) {
            return 0;
        }
        int contents = tank.getContents();
        int extracted = Math.min(contents, amount);
        tank.setContents(contents - extracted);
        return extracted;
    }

    @Override
    public Fluid getSupportedFluid() {
        Tank tank = getTank();
        if (tank == null) {
            return null;
        }
        return tank.getFluid();
    }

    @Override
    public int getMaxExtractPerTick() {
        return 100;
    }

    @Override
    public int getMaxInsertPerTick() {
        return 100;
    }

    @Override
    public int getEmptyLiquidLeft() {
        Tank tank = getTank();
        if (tank == null) {
            return 0;
        }
        return tank.getMaxContents() - tank.getContents();
    }

    @Override
    public int insert(Fluid fluid, int amount) {
        Tank tank = getTank();
        if (tank == null) {
            return 0;
        }
        if (tank.getFluid() != null && tank.getFluid() != fluid) {
            return 0;
        }
        tank.setFluid(fluid);
        int inserted = Math.min(amount, tank.getMaxContents() - tank.getContents());
        tank.setContents(tank.getContents() + inserted);
        return inserted;
    }

    @Override
    public float getFilledPercentage() {
        Tank tank = getTank();
        if (tank == null) {
            return 0;
        }
        return tank.getContents() * 100.0f / tank.getMaxContents();
    }

    public void checkRainAndSun() {
        if (networkId == -1) {
            return;
        }
        if (getWorld().isRaining()) {
            checkRain();
        } else {
            checkEvaporation();
        }
    }

    @Override
    public Tank getMultiBlock() {
        return ImmersiveCraftHandler.tankNetwork.getOrCreateMultiBlock(networkId);
    }

    @Override
    public Integer getID() {
        return networkId;
    }

    @Override
    public void setID(Integer id) {
        networkId = id;
        markDirtyClient();
    }

    private void checkEvaporation() {
        float angle = getWorld().getCelestialAngle(1.0f);
        int minutes = (int) (angle * ((24 * 60) - 0.1f));
        int hours = minutes / 60;
        if (hours >= 21 || hours <= 3) {
            if (getWorld().canBlockSeeSky(getPos())) {
                Tank tank = getMultiBlock();
                if (tank.getFluid() == FluidSetup.freshWater || tank.getFluid() == FluidRegistry.WATER) {
                    int newContents = tank.getContents() - EVAPORATE_AMOUNT;
                    if (newContents < 0) {
                        newContents = 0;
                    }
                    tank.setContents(newContents);
                    ImmersiveCraftHandler.tankNetwork.save(getWorld());
                }
            }
        }
    }

    private void checkRain() {
        if (getWorld().canBlockSeeSky(getPos())) {
            Tank tank = getMultiBlock();
            if (tank.getFluid() == null) {
                tank.setFluid(FluidSetup.freshWater);
            }
            if (tank.getFluid() == FluidSetup.freshWater || tank.getFluid() == FluidRegistry.WATER) {
                int newContents = tank.getContents() + ADD_RAIN;
                if (newContents > tank.getMaxContents()) {
                    newContents = tank.getMaxContents();
                }
                tank.setContents(newContents);
                ImmersiveCraftHandler.tankNetwork.save(getWorld());
            }
        }
    }

    public void addBlockToNetwork(Fluid fluid) {
        networkId = MultiBlockTankTileHelper.addBlockToNetwork(ImmersiveCraftHandler.tankNetwork, getID(), getWorld(), getPos(), fluid);
        ImmersiveCraftHandler.tankNetwork.save(getWorld());
        markDirty();
    }

    public void removeBlockFromNetwork() {
        MultiBlockTankTileHelper.removeBlockFromNetwork(ImmersiveCraftHandler.tankNetwork, getWorld(), getPos());
        ImmersiveCraftHandler.tankNetwork.save(getWorld());
        markDirty();
    }

    public Tank getTank() {
        if (networkId == -1) {
            return null;
        }
        return getMultiBlock();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        networkId = tagCompound.getInteger("networkId");
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
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper.set("networkId", networkId);
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
