package mcjty.aquamunda.fluid;


import mcjty.aquamunda.AquaMunda;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFreshWater extends BlockFluidClassic {

    public BlockFreshWater(Fluid fluid, Material material) {
        super(fluid, material);
        setCreativeTab(AquaMunda.creativeTab);
        setUnlocalizedName("fresh_water");
        GameRegistry.registerBlock(this, "fresh_water");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelResourceLocation fluidLocation = new ModelResourceLocation(getRegistryName(), "fluid");

        StateMapperBase customState = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return fluidLocation;
            }
        };
        ModelLoader.setCustomStateMapper(this, customState);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos) {
        return !world.getBlockState(pos).getBlock().getMaterial().isLiquid() && super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos) {
        return !world.getBlockState(pos).getBlock().getMaterial().isLiquid() && super.displaceIfPossible(world, pos);
    }


    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        world.scheduleUpdate(pos, this, tickRate);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        world.scheduleUpdate(pos, this, tickRate);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int meta = state.getBlock().getMetaFromState(state);
        if (meta != 0 || !canFallInto(world, pos.down())) {
            super.updateTick(world, pos, state, rand);
        }
        if (!world.isRemote) {
            // Only the source block falls down
            if (meta == 0) {
                this.handleFalling(world, pos);
            }
        }
    }

    private void handleFalling(World world, BlockPos pos) {
        if (canFallInto(world, pos.down()) && pos.getY() >= 0) {
            byte b0 = 32;

            if (!BlockFalling.fallInstantly && world.isAreaLoaded(pos, b0)) {
                if (!world.isRemote) {
                    IBlockState state = world.getBlockState(pos);
                    EntityFallingFreshWaterBlock entityfallingblock = new EntityFallingFreshWaterBlock(world, (pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), state);
                    world.spawnEntityInWorld(entityfallingblock);
                }
            } else {
                world.setBlockToAir(pos);

                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                while (canFallInto(world, new BlockPos(x, y-1, z)) && y > 0) {
                    --y;
                }

                if (y > 0) {
                    world.setBlockState(new BlockPos(x, y, z), getDefaultState());
                    world.scheduleUpdate(new BlockPos(x, y, z), this, this.tickRate(world));
                }
            }
        }
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World world) {
        return 2;
    }

    public static boolean canFallInto(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();

        if (block.isAir(world, pos)) {
            return true;
        } else if (block == Blocks.fire) {
            return true;
        } else {
            //TODO: King, take a look here when doing liquids!
            Material material = block.getMaterial();
            return material == Material.water ? true : material == Material.lava;
        }
    }
}
