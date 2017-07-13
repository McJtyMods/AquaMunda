package mcjty.aquamunda.environment;

import mcjty.aquamunda.chunkdata.GameData;
import mcjty.lib.tools.WorldTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class EnvironmentData extends WorldSavedData {

    public static final String NAME = "AquaMundaEnvironment";
    private static EnvironmentData instance = null;

    private GameData environmentData = new GameData((byte) 0);

    public EnvironmentData(String name) {
        super(name);
    }

    public static EnvironmentData getEnvironmentData(World world) {
        if (world.isRemote) {
            return null;
        }
        if (instance != null) {
            return instance;
        }
        instance = WorldTools.loadData(world, EnvironmentData.class, NAME);
        if (instance == null) {
            instance = new EnvironmentData(NAME);
        }
        return instance;
    }

    public static void clearInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public void save(World world) {
        world.getMapStorage().setData(NAME, this);
        markDirty();
    }

    public GameData getData() {
        return environmentData;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        environmentData.readFromNBT(tagCompound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        environmentData.writeToNBT(tagCompound);
        return tagCompound;
    }
}
