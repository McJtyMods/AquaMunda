package mcjty.aquamunda.blocks.hose;


import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.cable.ICableItemBlockHelper;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CableItemBlock extends ItemBlock {

    private final ICableItemBlockHelper helper;

    public CableItemBlock(Block block, ICableType type, ICableSubType subType) {
        super(block);
        helper = ImmersiveCraftHandler.immersiveCraft.createItemBlockHelper(type, subType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        // Return true to make this work all the time.
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (helper.onItemUse(player, world, pos)) {
            return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
        } else {
            return false;
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        return helper.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ);
    }
}
