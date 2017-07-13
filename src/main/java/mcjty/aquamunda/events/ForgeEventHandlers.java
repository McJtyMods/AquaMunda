package mcjty.aquamunda.events;


import mcjty.aquamunda.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onUseHoeEvent(UseHoeEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = event.getCurrent();

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (world.isAirBlock(pos.up())) {
            if (block == Blocks.GRASS) {
                event.setCanceled(true);
                useHoe(stack, player, world, pos, ModBlocks.customFarmLand.getDefaultState());
            } else if (block == Blocks.DIRT) {
                switch (iblockstate.getValue(BlockDirt.VARIANT)) {
                    case DIRT:
                        event.setCanceled(true);
                        useHoe(stack, player, world, pos, ModBlocks.customFarmLand.getDefaultState());
                        break;
                    case COARSE_DIRT:
                        event.setCanceled(true);
                        useHoe(stack, player, world, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                        break;
                }
            }
        }
    }

    protected static boolean useHoe(ItemStack stack, EntityPlayer player, World worldIn, BlockPos target, IBlockState newState) {
        SoundType soundType = newState.getBlock().getSoundType(newState, worldIn, target, player);
        SoundEvent stepSound = soundType.getStepSound();
        worldIn.playSound(target.getX() + 0.5F, (target.getY() + 0.5F), (target.getZ() + 0.5F), stepSound, SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F, true);

        if (worldIn.isRemote) {
            return true;
        } else {
            worldIn.setBlockState(target, newState);
            stack.damageItem(1, player);
            return true;
        }
    }


}
