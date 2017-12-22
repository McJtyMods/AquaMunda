package mcjty.aquamunda.varia;

import mcjty.aquamunda.blocks.generic.GenericAMTE;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class BlockTools {
    private static final Random random = new Random();

    public static <T extends GenericAMTE> Optional<T> getTE(Class<T> clazz, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericAMTE && (clazz == null || clazz.isInstance(te))) {
            return Optional.of((T) te);
        } else {
            return Optional.empty();
        }
    }

    public static boolean isHot(World w, BlockPos p) {
        IBlockState state = w.getBlockState(p);
        Block block = state.getBlock();
        if (block == Blocks.FIRE) {
            return true;
        } else if (block.isBurning(w, p)) {
            return true;
        }
        return false;
    }
}
