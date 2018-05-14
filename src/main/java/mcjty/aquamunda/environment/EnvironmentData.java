package mcjty.aquamunda.environment;

import mcjty.aquamunda.chunkdata.GameData;
import mcjty.lib.worlddata.AbstractWorldData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EnvironmentData extends AbstractWorldData<EnvironmentData> {

    private static final String NAME = "AquaMundaEnvironment";

    private GameData environmentData = new GameData((byte) 0);

    public EnvironmentData(String name) {
        super(name);
    }

    @Override
    public void clear() {
        environmentData = new GameData((byte) 0);
    }

    public static EnvironmentData getEnvironmentData(World world) {
        return getData(world, EnvironmentData.class, NAME);
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
