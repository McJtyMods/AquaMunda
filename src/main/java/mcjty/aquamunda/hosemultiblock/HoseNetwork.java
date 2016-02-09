package mcjty.aquamunda.hosemultiblock;

import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.cable.ICable;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class HoseNetwork extends WorldSavedData {

    public static final String NAME = "AquaMundaHoses";
    private static HoseNetwork instance = null;
    private static HoseNetwork clientInstance = null;

    private IMultiBlockNetwork network;

    public HoseNetwork(String identifier) {
        super(identifier);
        network = ImmersiveCraftHandler.immersiveCraft.createCableNetwork(ImmersiveCraftHandler.liquidType, ImmersiveCraftHandler.liquidSubtype);
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
    public static HoseNetwork getClientSide() {
        if (clientInstance == null) {
            clientInstance = new HoseNetwork(NAME);
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
        instance = (HoseNetwork) world.getMapStorage().loadData(HoseNetwork.class, NAME);
        if (instance == null) {
            instance = new HoseNetwork(NAME);
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
