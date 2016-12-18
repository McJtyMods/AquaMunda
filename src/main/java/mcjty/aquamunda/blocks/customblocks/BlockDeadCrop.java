package mcjty.aquamunda.blocks.customblocks;


import mcjty.aquamunda.blocks.generic.GenericAMBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class BlockDeadCrop extends GenericAMBlock {

    public BlockDeadCrop(String blockName) {
        super(Material.PLANTS, blockName, null);
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
        disableStats();
    }

    // @todo CHECK?
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
//    @Override
//    public int getRenderType(IBlockState state) {
//        return 6;
//    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        float f = 0.5F;
        return new AxisAlignedBB(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }
}
