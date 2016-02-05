package mcjty.aquamunda.blocks.generic;

import mcjty.aquamunda.varia.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class GenericTE extends TileEntity {

    public GenericBlock getBlock() {
        return (GenericBlock) getBlockType();
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    public void markDirtyClient() {
        markDirty();
        worldObj.markBlockForUpdate(getPos());
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    /**
     * Should be called server side on activation.
     * @param worldSide is the side in world space where the block is activated
     * @param side is the side in block space where the block is activated
     */
    public boolean onActivate(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3 hitVec) {
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        writeToNBT(NBTHelper.create(tagCompound));
    }

    protected void writeToNBT(NBTHelper helper) {
    }
}
