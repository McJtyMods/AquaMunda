package mcjty.aquamunda.blocks.desalination;

import mcjty.aquamunda.blocks.generic.GenericBlockWithTE;
import mcjty.aquamunda.network.PacketGetInfoFromServer;
import mcjty.aquamunda.network.PacketHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class DesalinationTankBlock extends GenericBlockWithTE<DesalinationTankTE> {

    public DesalinationTankBlock() {
        super(Material.iron, "evaporator", DesalinationTankTE.class);
        setHardness(2.0f);
        setStepSound(soundTypeMetal);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    private static long lastUpdateTime = 0;

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        DesalinationTankTE te = (DesalinationTankTE) accessor.getTileEntity();

        long time = System.currentTimeMillis();
        if ((time - lastUpdateTime) > 200) {
            lastUpdateTime = time;
            PacketHandler.INSTANCE.sendToServer(new PacketGetInfoFromServer(new TankContentsInfoPacketServer(te.getPos())));
        }

        currenttip.add(EnumChatFormatting.GREEN + "Liquid: " + FluidRegistry.getFluidName(te.getSupportedFluid()));
        currenttip.add(EnumChatFormatting.GREEN + "Contents: " + te.getContents() + " (" + DesalinationTankTE.MAX_CONTENTS + ")");
        return currenttip;
    }
}
