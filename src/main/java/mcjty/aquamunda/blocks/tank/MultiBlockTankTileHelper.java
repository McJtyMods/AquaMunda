package mcjty.aquamunda.blocks.tank;

import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import mcjty.immcraft.api.multiblock.IMultiBlockTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;

public class MultiBlockTankTileHelper {

    private static IMultiBlockTile<Tank> getTile(World world, IMultiBlockNetwork<Tank> network, BlockPos pos, EnumFacing direction) {
        BlockPos newpos;
        if (direction == null) {
            newpos = pos;
        } else {
            newpos = pos.offset(direction);
        }
        TileEntity te = world.getTileEntity(newpos);
        if (te instanceof IMultiBlockTile) {
            IMultiBlockTile mtile = (IMultiBlockTile) te;
            if (network.fromThisNetwork(mtile)) {
                return mtile;
            }
        }
        return null;
    }

    public static int addBlockToNetwork(IMultiBlockNetwork<Tank> network, int networkId, World world, BlockPos thisCoord, Fluid fluid) {
        // Find an adjacent network to connect too.
        Tank foundMb = null;
        for (EnumFacing direction : network.getDirections()) {
            IMultiBlockTile<Tank> otherBlock = getTile(world, network, thisCoord, direction);
            if (otherBlock != null) {
                Tank otherMb = otherBlock.getMultiBlock();
                if ((fluid == null || fluid == otherMb.getFluid()) && otherMb.canConnect(null, thisCoord)) {
                    foundMb = otherMb;
                    networkId = otherBlock.getID();
                    foundMb.addBlock(thisCoord);
                    break;
                }
            }
        }

        if (foundMb == null) {
            // No adjacent network. We create a new one.
            networkId = network.newMultiBlock();
            network.getOrCreateMultiBlock(networkId).addBlock(thisCoord);
        } else {
            // Now check if we can connect this to other adjacent networks and connect them one at a time.
            for (EnumFacing direction : network.getDirections()) {
                IMultiBlockTile<Tank> otherBlock = getTile(world, network, thisCoord, direction);
                if (otherBlock != null) {
                    Tank otherMb = otherBlock.getMultiBlock();
                    if (otherMb != foundMb && foundMb.canConnect(otherMb, thisCoord)) {
                        Integer otherId = otherBlock.getID();
                        foundMb.merge(world, networkId, otherMb, otherBlock.getID());
                        network.deleteMultiBlock(otherId);
                        break;
                    }
                }
            }
        }
        return networkId;
    }

    public static void removeBlockFromNetwork(IMultiBlockNetwork<Tank> network, World world, BlockPos thisCoord) {
        IMultiBlockTile<Tank> thisTile = getTile(world, network, thisCoord, null);
        Tank mb = thisTile.getMultiBlock();
        Collection<? extends IMultiBlock> multiBlocks = mb.remove(world, thisCoord);
        network.deleteMultiBlock(thisTile.getID());
        for (IMultiBlock multiBlock : multiBlocks) {
            int id = network.createMultiBlock((Tank) multiBlock);
            for (BlockPos b : multiBlock.getBlocks()) {
                TileEntity te = world.getTileEntity(b);
                if (te instanceof IMultiBlockTile) {
                    IMultiBlockTile mtile = (IMultiBlockTile) te;
                    mtile.setID(id);
                }
            }

        }
    }
}
