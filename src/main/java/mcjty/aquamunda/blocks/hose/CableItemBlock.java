package mcjty.aquamunda.blocks.hose;


import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.cable.ICableItemBlockHelper;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CableItemBlock extends ItemBlock {

    private final ICableType type;
    private final ICableSubType subType;
    private ICableItemBlockHelper helper = null;

    public CableItemBlock(Block block, ICableType type, ICableSubType subType) {
        super(block);
        this.type = type;
        this.subType = subType;
    }

    private ICableItemBlockHelper getHelper() {
        if (helper == null) {
            helper = ImmersiveCraftHandler.immersiveCraft.createItemBlockHelper(type, subType);
        }
        return helper;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        // Return true to make this work all the time.
        return true;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (getHelper().onItemUse(player, world, pos)) {
            return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        return getHelper().placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ);
    }
}
