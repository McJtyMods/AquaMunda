package mcjty.aquamunda.varia;

import mcjty.aquamunda.blocks.generic.GenericAMBlock;
import mcjty.aquamunda.blocks.generic.GenericAMTE;
import mcjty.aquamunda.blocks.generic.GenericInventoryTE;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

import static net.minecraft.util.EnumFacing.*;

public class BlockTools {
    private static final Random random = new Random();

    // Use these flags if you want to support a single redstone signal and 3 bits for orientation.
    public static final int MASK_ORIENTATION = 0x7;
    public static final int MASK_REDSTONE = 0x8;

    // Use these flags if you want to support both redstone in and output and only 2 bits for orientation.
    public static final int MASK_ORIENTATION_HORIZONTAL = 0x3;          // Only two bits for orientation
    public static final int MASK_REDSTONE_IN = 0x8;                     // Redstone in
    public static final int MASK_REDSTONE_OUT = 0x4;                    // Redstone out
    public static final int MASK_STATE = 0xc;                           // If redstone is not used: state

    public static EnumFacing getOrientation(IBlockState state) {
        return ((GenericAMBlock)state.getBlock()).getFrontDirection(state);
    }

    public static EnumFacing blockToWorldSpace(EnumFacing blockSide, EnumFacing blockDirection) {
        switch (blockDirection) {
            case DOWN:
                switch (blockSide) {
                    case SOUTH: return DOWN;
                    case NORTH: return UP;
                    case UP: return NORTH;
                    case DOWN: return SOUTH;
                    case EAST: return WEST;
                    case WEST: return EAST;
                    default: return blockSide;
                }
            case UP:
                switch (blockSide) {
                    case NORTH: return DOWN;
                    case SOUTH: return UP;
                    case UP: return NORTH;
                    case DOWN: return SOUTH;
                    case WEST: return WEST;
                    case EAST: return EAST;
                    default: return blockSide;
                }
            case NORTH:
                if (blockSide == DOWN || blockSide == UP) {
                    return blockSide;
                }
                return blockSide.getOpposite();
            case SOUTH:
                return blockSide;
            case WEST:
                if (blockSide == DOWN || blockSide == UP) {
                    return blockSide;
                } else if (blockSide == SOUTH) {
                    return WEST;
                } else if (blockSide == WEST) {
                    return NORTH;
                } else if (blockSide == NORTH) {
                    return EAST;
                } else {
                    return SOUTH;
                }
            case EAST:
                if (blockSide == DOWN || blockSide == UP) {
                    return blockSide;
                } else if (blockSide == NORTH) {
                    return WEST;
                } else if (blockSide == EAST) {
                    return NORTH;
                } else if (blockSide == SOUTH) {
                    return EAST;
                } else {
                    return SOUTH;
                }
            default:
                return blockSide;
        }
    }

    public static void emptyInventoryInWorld(World world, BlockPos pos, Block block, IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            spawnItemStack(world, pos, itemstack);
            inventory.setInventorySlotContents(i, ItemStack.EMPTY);
        }

        world.updateComparatorOutputLevel(pos, block);
//        world.func_147453_f(x, y, z, block);
    }

    public static void spawnItemStack(World world, BlockPos c, ItemStack itemStack) {
        spawnItemStack(world, c.getX(), c.getY(), c.getZ(), itemStack);
    }

    public static void spawnItemStack(World world, int x, int y, int z, ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem;

            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (!itemstack.isEmpty()) {
                int j = random.nextInt(21) + 10;

                if (j > itemstack.getCount()) {
                    j = itemstack.getCount();
                }

                int amount = -j;
                itemstack.grow(amount);
                entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = ((float)random.nextGaussian() * f3);
                entityitem.motionY = ((float)random.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = ((float)random.nextGaussian() * f3);

                if (itemstack.hasTagCompound()) {
                    entityitem.getItem().setTagCompound(itemstack.getTagCompound().copy());
                }
                world.spawnEntity(entityitem);
            }
        }
    }


    public static void placeBlock(World world, BlockPos pos, GenericAMBlock block, EntityPlayer player) {
        IBlockState state = block.getDefaultState().withProperty(GenericAMBlock.FACING_HORIZ, player.getHorizontalFacing().getOpposite());
        world.setBlockState(pos, state, 2);
    }

    public static Block getBlock(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            return ((ItemBlock) stack.getItem()).getBlock();
        } else {
            return null;
        }
    }

    public static <T extends GenericAMTE> Optional<T> getTE(Class<T> clazz, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericAMTE && (clazz == null || clazz.isInstance(te))) {
            return Optional.of((T) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<GenericInventoryTE> getInventoryTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericInventoryTE) {
            return Optional.of((GenericInventoryTE) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<IInventory> getInventory(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IInventory) {
            return Optional.of((IInventory) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<GenericAMTE> castGenericTE(TileEntity te) {
        return (te instanceof GenericAMTE) ? Optional.of((GenericAMTE) te) : Optional.empty();
    }

    public static <T extends GenericAMTE> T castTE(TileEntity te) {
        return (T) te;
    }

    public static Optional<GenericAMBlock> getBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof GenericAMBlock) {
            return Optional.of((GenericAMBlock) block);
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
