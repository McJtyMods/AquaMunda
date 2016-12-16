package mcjty.aquamunda.blocks.cooker;

import mcjty.aquamunda.blocks.generic.GenericBlockWithTE;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CookerBlock extends GenericBlockWithTE<CookerTE> {

    public static final PropertyEnum<EnumBoiling> BOILING = PropertyEnum.create("boiling", EnumBoiling.class, EnumBoiling.values());
    public static final PropertyEnum<EnumContents> CONTENTS = PropertyEnum.create("contents", EnumContents.class, EnumContents.values());

    public CookerBlock() {
        super(Material.IRON, "cooker", CookerTE.class);
        setHardness(2.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public MetaUsage getMetaUsage() {
        return MetaUsage.NONE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof CookerTE) {
            CookerTE cookerTE = (CookerTE) te;
            float temperature = cookerTE.getTemperature();
            if (temperature < 70) {
                state = state.withProperty(BOILING, EnumBoiling.COLD);
            } else if (temperature < 97) {
                state = state.withProperty(BOILING, EnumBoiling.HOT);
            } else {
                state = state.withProperty(BOILING, EnumBoiling.BOILING);
            }

            float filled = cookerTE.getFilledPercentage();
            if (filled < 1) {
                state = state.withProperty(CONTENTS, EnumContents.EMPTY);
            } else if (filled < 40) {
                state = state.withProperty(CONTENTS, EnumContents.LOW);
            } else if (filled < 80) {
                state = state.withProperty(CONTENTS, EnumContents.MEDIUM);
            } else {
                state = state.withProperty(CONTENTS, EnumContents.FULL);
            }
        }
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BOILING, CONTENTS);
    }
}

