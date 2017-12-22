package mcjty.aquamunda.blocks.tank;

import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import mcjty.immcraft.api.multiblock.IMultiBlockTile;
import mcjty.lib.varia.BlockPosTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.*;

public class Tank implements IMultiBlock {
    private Set<BlockPos> tankBlocks = new HashSet<>();

    private int contents = 0;
    private Fluid fluid = null;

    @Override
    public int getBlockCount() {
        return tankBlocks.size();
    }

    @Override
    public IMultiBlockClientInfo getClientInfo() {
        return new TankClientInfo(tankBlocks.size(), contents, fluid);
    }

    @Override
    public Collection<BlockPos> getBlocks() {
        return tankBlocks;
    }

    public int getMaxContents() {
        return getBlockCount() * TankTE.MAX_CONTENTS;
    }

    @Override
    public void addBlock(BlockPos coordinate) {
        tankBlocks.add(coordinate);
    }

    public int getContents() {
        return contents;
    }

    public void setContents(int contents) {
        this.contents = contents;
        if (contents <= 0) {
            fluid = null;
        }
    }

    public String getFluidName() {
        return getFluidName(fluid);
    }

    public static String getFluidName(Fluid fluid) {
        if (fluid == null) {
            return null;
        } else {
            return FluidRegistry.getFluidName(fluid);
        }
    }

    public Fluid getFluid() {
        return fluid;
    }

    public void setFluid(Fluid fluid) {
        this.fluid = fluid;
    }

    public void setFluidByName(String name) {
        if (name == null || name.isEmpty()) {
            fluid = null;
        } else {
            fluid = FluidRegistry.getFluid(name);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("contents", contents);
        if (fluid != null) {
            tagCompound.setString("fluid", FluidRegistry.getFluidName(fluid));
        }
        int idx = 0;
        for (BlockPos c : tankBlocks) {
            BlockPosTools.writeToNBT(tagCompound, "b" + idx, c);
            idx++;
        }
        tagCompound.setInteger("tankSize", idx);

    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        this.contents = tagCompound.getInteger("contents");
        setFluidByName(tagCompound.getString("fluid"));
        int tankSize = tagCompound.getInteger("tankSize");
        tankBlocks = new HashSet<>(tankSize);
        for (int i = 0 ; i < tankSize ; i++) {
            tankBlocks.add(BlockPosTools.readFromNBT(tagCompound, "b" + i));
        }
    }

    @Override
    public boolean canConnect(IMultiBlock other, BlockPos pos) {
        if (other == null) {
            // We can connect to a null tank
            return true;
        }
        if (!(other instanceof Tank)) {
            return false;
        }
        Tank otherTank = (Tank) other;
        if (fluid == null || otherTank.fluid == null) {
            return true;
        }
        return fluid == otherTank.fluid;
    }

    @Override
    public void merge(World world, int networkId, IMultiBlock other, int otherId) {
        // We assume the other multiblock is of the correct type.
        contents += ((Tank) other).contents;

        for (BlockPos c : ((Tank) other).tankBlocks) {
            TileEntity te = world.getTileEntity(c);
            if (te instanceof IMultiBlockTile) {
                IMultiBlockTile mtile = (IMultiBlockTile) te;
                mtile.setID(networkId);
            }
        }

        tankBlocks.addAll(((Tank) other).tankBlocks);
    }

    private void extractTank(BlockPos c, Set<BlockPos> blocks, Set<BlockPos> newTank) {
        blocks.remove(c);
        newTank.add(c);
        for (EnumFacing dir : EnumFacing.HORIZONTALS) {
            BlockPos cAdjacent = c.offset(dir);
            if (blocks.contains(cAdjacent)) {
                extractTank(cAdjacent, blocks, newTank);
            }
        }
    }

    @Override
    public Collection<? extends IMultiBlock> remove(World world, BlockPos c) {
        // We assume here that the relevant portion of the tank contents has already been removed
        // before this function is called.
        int contentsPerBlock = contents / tankBlocks.size();
        int remainderForFirst = contents % tankBlocks.size();

        List<Tank> multiBlocks = new ArrayList<>();
        tankBlocks.remove(c);
        for (EnumFacing dir : EnumFacing.HORIZONTALS) {
            BlockPos cAdjacent = c.offset(dir);
            if (tankBlocks.contains(cAdjacent)) {
                Set<BlockPos> newTank = new HashSet<>();
                extractTank(cAdjacent, tankBlocks, newTank);
                Tank tank = new Tank();
                tank.tankBlocks = newTank;
                tank.contents = contentsPerBlock * newTank.size() + remainderForFirst;
                remainderForFirst = 0;
                tank.fluid = fluid;
                multiBlocks.add(tank);
            }
        }

        return multiBlocks;
    }
}
