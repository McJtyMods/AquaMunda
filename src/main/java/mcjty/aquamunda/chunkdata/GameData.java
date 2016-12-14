package mcjty.aquamunda.chunkdata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameData {
    private byte initialValue;
    private Map<Integer, WorldData> worldDataMap = new HashMap<>();

    public GameData(byte initialValue) {
        this.initialValue = initialValue;
    }

    public boolean set(int dimension, BlockPos pos, byte value) {
        if (worldDataMap.containsKey(dimension)) {
            return worldDataMap.get(dimension).set(pos, value);
        } else if (value != initialValue) {
            WorldData worldData = new WorldData(initialValue);
            worldDataMap.put(dimension, worldData);
            return worldData.set(pos, value);
        } else {
            return false;
        }
    }

    public byte get(int dimension, BlockPos pos) {
        if (worldDataMap.containsKey(dimension)) {
            return worldDataMap.get(dimension).get(pos);
        } else {
            return initialValue;
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setByte("b", initialValue);
        for (Map.Entry<Integer, WorldData> entry : worldDataMap.entrySet()) {
            NBTTagCompound tc = new NBTTagCompound();
            entry.getValue().writeToNBT(tc);
            tagCompound.setTag("t" + entry.getKey(), tc);
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        initialValue = tagCompound.getByte("b");
        Set keys = tagCompound.getKeySet();
        for (Object key : keys) {
            String k = (String) key;
            if (k.startsWith("t")) {
                int dimension = Integer.parseInt(k.substring(1));
                NBTTagCompound tc = (NBTTagCompound) tagCompound.getTag("t" + dimension);
                WorldData worldData = new WorldData(initialValue);
                worldData.readFromNBT(tc);
                worldDataMap.put(dimension, worldData);
            }
        }

    }

}
