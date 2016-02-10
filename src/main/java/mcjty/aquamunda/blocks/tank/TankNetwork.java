package mcjty.aquamunda.blocks.tank;

import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import mcjty.immcraft.api.multiblock.IMultiBlockFactory;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class TankNetwork extends WorldSavedData {

    public static final String TANK_NETWORK = "AquaMundaTanks";
    private static TankNetwork instance = null;
    private static TankNetwork clientInstance = null;

    private IMultiBlockNetwork<Tank> network;

    public TankNetwork(String identifier) {
        super(identifier);

        network = ImmersiveCraftHandler.immersiveCraft.createMultiBlockNetwork(TANK_NETWORK, new IMultiBlockFactory<Tank>() {
            @Override
            public Tank create() {
                return new Tank();
            }

            @Override
            public boolean isSameType(IMultiBlock mb) {
                return mb instanceof Tank;
            }

            @Override
            public IMultiBlockClientInfo createClientInfo() {
                return new TankClientInfo(0, 0, null);
            }
        }, EnumFacing.HORIZONTALS);
    }

    public void save(World world) {
        world.getMapStorage().setData(TANK_NETWORK, this);
        markDirty();
    }

    public static void clearInstance() {
        if (instance != null) {
            instance.network.clear();
            instance = null;
        }
    }

    // This should only be used client-side!
    public static TankNetwork getClientSide() {
        if (clientInstance == null) {
            clientInstance = new TankNetwork(TANK_NETWORK);
        }
        return clientInstance;
    }

    public static TankNetwork get() {
        return instance;
    }

    public static TankNetwork get(World world) {
        if (world.isRemote) {
            return null;
        }
        if (instance != null) {
            return instance;
        }
        instance = (TankNetwork) world.getMapStorage().loadData(TankNetwork.class, TANK_NETWORK);
        if (instance == null) {
            instance = new TankNetwork(TANK_NETWORK);
        }
        return instance;
    }

    public IMultiBlockNetwork<Tank> getNetwork() {
        return network;
    }

    public Tank getOrCreateTank(int id) {
        return network.getOrCreateMultiBlock(id);
    }
//
//    public Tank getTank(int id) {
//        return network.getMultiblock(id);
//    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        network.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        network.writeToNBT(tagCompound);
    }

}
