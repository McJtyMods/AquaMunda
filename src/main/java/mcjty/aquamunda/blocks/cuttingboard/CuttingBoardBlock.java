package mcjty.aquamunda.blocks.cuttingboard;

import mcjty.aquamunda.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.lib.tools.ItemStackTools;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CuttingBoardBlock extends GenericBlockWithTE<CuttingBoardTE> {

    public static final AxisAlignedBB BOARD_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.05D, 0.90D, 0.1D, 0.95D);

    public CuttingBoardBlock() {
        super(Material.IRON, "cutting_board", CuttingBoardTE.class);
        setHardness(2.0f);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("pickaxe", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(CuttingBoardTE.class, new CuttingBoardTESR());
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOARD_AABB;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof CuttingBoardTE) {
            CuttingBoardTE cookerTE = (CuttingBoardTE) te;

            // @todo make more general? Also used in immcraft
            IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(cookerTE, this);
            if (selectedHandle != null) {
                ItemStack currentStack = selectedHandle.getCurrentStack(te);
                if (ItemStackTools.isValid(currentStack)) {
                    probeInfo.text(TextFormatting.GREEN + currentStack.getDisplayName() + " (" + ItemStackTools.getStackSize(currentStack) + ")");
                }
            }
        }
    }

    @Override
    protected boolean clOnBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            CuttingBoardTE cuttingBoardTE = getTE(world, pos);

            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            if (ItemStackTools.isValid(heldItem) && heldItem.getItem() == Items.BOWL) {
                return true;
            }
        }
        if (super.clOnBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ)) {
            return true;
        }
        return true;
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
}

