package mcjty.aquamunda.blocks.desalination;

import mcjty.aquamunda.blocks.generic.GenericBlockWithTE;
import mcjty.aquamunda.network.PacketGetInfoFromServer;
import mcjty.aquamunda.network.AMPacketHandler;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.List;

public class DesalinationBoilerBlock extends GenericBlockWithTE<DesalinationBoilerTE> {

    public DesalinationBoilerBlock() {
        super(Material.IRON, "boiler", DesalinationBoilerTE.class);
        setHardness(2.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
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

    private static long lastUpdateTime = 0;

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        DesalinationBoilerTE te = (DesalinationBoilerTE) accessor.getTileEntity();

        long time = System.currentTimeMillis();
        if ((time - lastUpdateTime) > 200) {
            lastUpdateTime = time;
            AMPacketHandler.INSTANCE.sendToServer(new PacketGetInfoFromServer(new BoilerContentsInfoPacketServer(te.getPos())));
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        currenttip.add(TextFormatting.GREEN + "Liquid: " + FluidRegistry.getFluidName(te.getSupportedFluid()));
        currenttip.add(TextFormatting.GREEN + "Contents: " + te.getContents() + " (" + DesalinationBoilerTE.MAX_CONTENTS + ")");
        currenttip.add(TextFormatting.GREEN + "Temperature: " + decimalFormat.format(te.getTemperature()));
        return currenttip;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);

        TileEntity tileEntity = world.getTileEntity(data.getPos());
        if (tileEntity instanceof DesalinationBoilerTE) {
            DesalinationBoilerTE te = (DesalinationBoilerTE) tileEntity;
            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            probeInfo.text(TextFormatting.GREEN + "Liquid: " + FluidRegistry.getFluidName(te.getSupportedFluid()));
            probeInfo.text(TextFormatting.GREEN + "Contents: " + te.getContents() + " (" + DesalinationBoilerTE.MAX_CONTENTS + ")");
            probeInfo.text(TextFormatting.GREEN + "Temperature: " + decimalFormat.format(te.getTemperature()));
        }
    }
}
