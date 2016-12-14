package mcjty.aquamunda.chunkdata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class WorldData {
    private byte initialValue;
    private Map<DataChunkCoordinate,ChunkData> chunkDataMap = new HashMap<>();

    public WorldData(byte initialValue) {
        this.initialValue = initialValue;
    }

    public boolean set(BlockPos pos, byte value) {
        int x = pos.getX();
        int dx = x >> 4;
        int y = pos.getY();
        int dy = y >> 4;
        int z = pos.getZ();
        int dz = z >> 4;
        DataChunkCoordinate coordinate = new DataChunkCoordinate(dx, dy, dz);
        if (chunkDataMap.containsKey(coordinate)) {
            return chunkDataMap.get(coordinate).set(x & 15, y & 15, z & 15, value);
        } else if (value != initialValue) {
            ChunkData chunkData = new ChunkData(initialValue);
            chunkDataMap.put(coordinate, chunkData);
            return chunkData.set(x & 15, y & 15, z & 15, value);
        } else {
            return false;
        }
    }

    public byte get(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int dx = x >> 4;
        int dy = y >> 4;
        int dz = z >> 4;
        DataChunkCoordinate coordinate = new DataChunkCoordinate(dx, dy, dz);
        if (chunkDataMap.containsKey(coordinate)) {
            return chunkDataMap.get(coordinate).get(x & 15, y & 15, z & 15);
        } else {
            return initialValue;
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setByte("b", initialValue);
        int cnt = 0;
        for (Map.Entry<DataChunkCoordinate, ChunkData> entry : chunkDataMap.entrySet()) {
            NBTTagCompound tc = new NBTTagCompound();
            entry.getValue().writeToNBT(tc);
            tc.setInteger("x", entry.getKey().getDx());
            tc.setInteger("y", entry.getKey().getDy());
            tc.setInteger("z", entry.getKey().getDz());
            tagCompound.setTag("t" + cnt, tc);
            cnt++;
        }
        tagCompound.setInteger("cnt", cnt);
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        chunkDataMap.clear();
        initialValue = tagCompound.getByte("b");
        int cnt = tagCompound.getInteger("cnt");
        for (int i = 0 ; i < cnt ; i++) {
            NBTTagCompound tc = (NBTTagCompound) tagCompound.getTag("t" + i);
            DataChunkCoordinate coordinate = new DataChunkCoordinate(tc.getInteger("x"), tc.getInteger("y"), tc.getInteger("z"));
            ChunkData chunkData = new ChunkData(initialValue);
            chunkData.readFromNBT(tc);
            chunkDataMap.put(coordinate, chunkData);
        }
    }

}
