package mcjty.aquamunda.blocks.tank;

import mcjty.aquamunda.multiblock.MultiBlockNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class TankNetwork extends WorldSavedData {

    public static final String NAME = "AquaMundaTanks";
    private static TankNetwork instance = null;
    private static TankNetwork clientInstance = null;

    private TankMultiBlockNetwork network;

    public TankNetwork(String identifier) {
        super(identifier);
        network = new TankMultiBlockNetwork();
    }

    public void save(World world) {
        world.getMapStorage().setData(NAME, this);
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
            clientInstance = new TankNetwork(NAME);
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
        instance = (TankNetwork) world.getMapStorage().loadData(TankNetwork.class, NAME);
        if (instance == null) {
            instance = new TankNetwork(NAME);
        }
        return instance;
    }

    public MultiBlockNetwork<Tank> getNetwork() {
        return network;
    }

    public Tank getOrCreateTank(int id) {
        return network.getOrCreateMultiBlock(id);
    }

    public Tank getTank(int id) {
        return network.getMultiblock(id);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        network.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        network.writeToNBT(tagCompound);
    }

}
