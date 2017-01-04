package mcjty.aquamunda.blocks.generic;


import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.compat.top.TOPInfoProvider;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.aquamunda.waila.WailaProvider;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class GenericAMBlock extends GenericBlock implements WailaProvider, TOPInfoProvider {

    public GenericAMBlock(Material material, String name) {
        this(material, name, null, null);
    }

    public GenericAMBlock(Material material, String name, Class<? extends GenericAMTE> clazz) {
        this(material, name, clazz, null);
    }

    public GenericAMBlock(Material material, String name, Class<? extends GenericAMTE> clazz, Class<? extends ItemBlock> itemBlockClass) {
        super(material, AquaMunda.MODID, name, clazz, itemBlockClass);
        this.setCreativeTab(AquaMunda.creativeTab);
    }

    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraftHandler.immersiveCraft;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }
}
