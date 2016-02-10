package mcjty.aquamunda.hosemultiblock;

import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.cable.ICable;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class HoseNetwork extends WorldSavedData {

    public static final String HOSE_NETWORK = "AquaMundaHoses";
    private static HoseNetwork instance = null;
    private static HoseNetwork clientInstance = null;

    private IMultiBlockNetwork network;

    public HoseNetwork(String identifier) {
        super(identifier);
        network = ImmersiveCraftHandler.immersiveCraft.createCableNetwork(HOSE_NETWORK, ImmersiveCraftHandler.liquidType, ImmersiveCraftHandler.liquidSubtype);
    }

    public void save(World world) {
        world.getMapStorage().setData(HOSE_NETWORK, this);
        markDirty();
    }

    public static void clearInstance() {
        if (instance != null) {
            instance.network.clear();
            instance = null;
        }
    }

    // This should only be used client-side!
    public static HoseNetwork getClientSide() {
        if (clientInstance == null) {
            clientInstance = new HoseNetwork(HOSE_NETWORK);
        }
        return clientInstance;
    }

    public static HoseNetwork get() {
        return instance;
    }

    public static HoseNetwork get(World world) {
        if (world.isRemote) {
            return null;
        }
        if (instance != null) {
            return instance;
        }
        instance = (HoseNetwork) world.getMapStorage().loadData(HoseNetwork.class, HOSE_NETWORK);
        if (instance == null) {
            instance = new HoseNetwork(HOSE_NETWORK);
        }
        return instance;
    }

    public IMultiBlockNetwork getNetwork() {
        return network;
    }

    public ICable getOrCreateHose(int id) {
        return (ICable) network.getOrCreateMultiBlock(id);
    }
//
//    public Cable getHose(int id) {
//        return network.getMultiblock(id);
//    }
//
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        network.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        network.writeToNBT(tagCompound);
    }

}
