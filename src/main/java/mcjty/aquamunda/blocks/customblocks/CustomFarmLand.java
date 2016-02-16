package mcjty.aquamunda.blocks.customblocks;

import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.sprinkler.SprinklerTE;
import mcjty.aquamunda.chunkdata.GameData;
import mcjty.aquamunda.environment.EnvironmentData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class CustomFarmLand extends BlockFarmland {

    public CustomFarmLand() {
        super();
        this.setHardness(0.6F).setStepSound(soundTypeGravel).setUnlocalizedName("farmland");
        setRegistryName("farmland");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        System.out.println("CustomFarmLand.onBlockPlaced");
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        System.out.println("CustomFarmLand.onBlockAdded");
        if (!world.isRemote) {
            EnvironmentData environment = EnvironmentData.getEnvironmentData(world);
            if (environment.getData().set(world.provider.getDimensionId(), pos, (byte) 0)) {
                environment.save(world);
            }
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        System.out.println("CustomFarmLand.updateTick");
        handleRain(world, pos, random);
        handleSprinkler(world, pos, random);

        if (!freshWaterNearby(world, pos) && !world.isRainingAt(pos.up())) {
            int l = state.getBlock().getMetaFromState(state);
            if (l > 0) {
                world.setBlockState(pos, state.getBlock().getStateFromMeta(l-1), 2);
            } else if (!isASuitablePlant(world, pos)) {
                world.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
        } else {
            world.setBlockState(pos, state.getBlock().getStateFromMeta(7), 2);
        }
    }

    private void handleRain(World world, BlockPos pos, Random random) {
        if (world.isRaining()) {
            if (world.canBlockSeeSky(pos.up())) {
                EnvironmentData environmentData = EnvironmentData.getEnvironmentData(world);
                GameData data = environmentData.getData();
                byte moistness = data.get(world.provider.getDimensionId(), pos);
                if (moistness < SprinklerTE.MAX_MOISTNESS) {
                    moistness += 2;
                    if (moistness > SprinklerTE.MAX_MOISTNESS) {
                        moistness = SprinklerTE.MAX_MOISTNESS;
                    }
                    data.set(world.provider.getDimensionId(), pos, moistness);
                    environmentData.save(world);
                }
            }
        }
    }

    private void handleSprinkler(World world, BlockPos pos, Random random) {
        EnvironmentData environmentData = EnvironmentData.getEnvironmentData(world);
        GameData data = environmentData.getData();
        byte moistness = data.get(world.provider.getDimensionId(), pos);
        if (moistness == 0) {
            if (random.nextInt(4) == 2) {
                killPlant(world, pos);
            }
        } else {
            moistness--;
            data.set(world.provider.getDimensionId(), pos, moistness);
            environmentData.save(world);
        }
    }

    private void killPlant(World world, BlockPos pos) {
        Block block = world.getBlockState(pos.up()).getBlock();
        if (block == Blocks.carrots) {
            world.setBlockState(pos.up(), ModBlocks.deadCarrot.getDefaultState(), 3);
        } else if (block == Blocks.wheat) {
            world.setBlockState(pos.up(), ModBlocks.deadWheat.getDefaultState(), 3);
        }
    }

    private boolean isASuitablePlant(World world, BlockPos pos) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return block instanceof IPlantable && canSustainPlant(world, pos, EnumFacing.UP, (IPlantable) block);
    }

    private boolean freshWaterNearby(World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int l = x - 4; l <= x + 4; ++l) {
            for (int i1 = y; i1 <= y + 1; ++i1) {
                for (int j1 = z - 4; j1 <= z + 4; ++j1) {
                    if (world.getBlockState(new BlockPos(l, i1, j1)).getBlock() == ModBlocks.blockFreshWater) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
