package mcjty.aquamunda.chunkdata;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A data block for a 16x16x16 chunk
 */
public class ChunkData {
    private byte[] data = null;
    private int commonValue = 0;

    public ChunkData(byte initial) {
        commonValue = initial;
    }

    public boolean set(int x, int y, int z, byte value) {
        if (data == null) {
            if (value == commonValue) {
                return false;
            }
            allocData();
            data[x + y*16 + z*256] = value;
            return true;
        } else {
            int index = x + y * 16 + z * 256;
            if (data[index] != value) {
                data[index] = value;
                return true;
            } else {
                return false;
            }
        }
    }

    private void allocData() {
        data = new byte[4096];
        for (int i = 0 ; i < 4096 ; i++) {
            data[i] = (byte) commonValue;
        }
    }

    public byte get(int x, int y, int z) {
        if (data == null) {
            return (byte) commonValue;
        } else {
            return data[x + y * 16 + z * 256];
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setByte("b", (byte) commonValue);
        if (data != null) {
            // Compact if possible
            for (byte b : data) {
                if (b != commonValue) {
                    tagCompound.setByteArray("bytes", data);
                    return;
                }
            }
            // If we come here we only had common values and we don't have to store bytes
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        commonValue = tagCompound.getByte("b");
        if (tagCompound.hasKey("bytes")) {
            data = tagCompound.getByteArray("bytes");
        } else {
            data = null;
        }
    }
}
