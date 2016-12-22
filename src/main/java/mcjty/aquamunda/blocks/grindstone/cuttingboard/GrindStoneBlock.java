package mcjty.aquamunda.blocks.grindstone.cuttingboard;

import mcjty.aquamunda.blocks.generic.GenericBlockWithTE;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.sound.ISoundProducer;
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

public class GrindStoneBlock extends GenericBlockWithTE<GrindStoneTE> implements ISoundProducer {

    public static final AxisAlignedBB BOARD_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.05D, 0.90D, 0.4D, 0.95D);

    public GrindStoneBlock() {
        super(Material.ROCK, "grindstone", GrindStoneTE.class);
        setHardness(2.0f);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("pickaxe", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(GrindStoneTE.class, new GrindStoneTESR());
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOARD_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof GrindStoneTE) {
            GrindStoneTE grindStoneTE = (GrindStoneTE) te;

            // @todo make more general? Also used in immcraft
            IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(grindStoneTE, this);
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
//            GrindStoneTE grindStoneTE = getTE(world, pos);

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

