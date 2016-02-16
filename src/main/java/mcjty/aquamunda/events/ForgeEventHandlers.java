package mcjty.aquamunda.events;


import mcjty.aquamunda.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onUseHoeEvent(UseHoeEvent event) {
        event.setCanceled(true);

        System.out.println("ForgeEventHandlers.onUseHoeEvent");

        World world = event.world;
        BlockPos pos = event.pos;
        EntityPlayer player = event.entityPlayer;
        ItemStack stack = event.current;

        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (world.isAirBlock(pos.up())) {
            if (block == Blocks.grass) {
                useHoe(stack, player, world, pos, ModBlocks.customFarmLand.getDefaultState());
            } else if (block == Blocks.dirt) {
                switch (iblockstate.getValue(BlockDirt.VARIANT)) {
                    case DIRT:
                        useHoe(stack, player, world, pos, ModBlocks.customFarmLand.getDefaultState());
                        break;
                    case COARSE_DIRT:
                        useHoe(stack, player, world, pos, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                        break;
                }
            }
        }
    }

    protected static boolean useHoe(ItemStack stack, EntityPlayer player, World worldIn, BlockPos target, IBlockState newState) {
        worldIn.playSoundEffect(target.getX() + 0.5F, (target.getY() + 0.5F), (target.getZ() + 0.5F), newState.getBlock().stepSound.getStepSound(), (newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F, newState.getBlock().stepSound.getFrequency() * 0.8F);

        if (worldIn.isRemote) {
            return true;
        } else {
            worldIn.setBlockState(target, newState);
            stack.damageItem(1, player);
            return true;
        }
    }


}
