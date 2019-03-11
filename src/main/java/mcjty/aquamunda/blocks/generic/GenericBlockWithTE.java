package mcjty.aquamunda.blocks.generic;


import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.IImmersiveCraft;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class GenericBlockWithTE<T extends GenericAMTE> extends GenericAMBlock implements ITileEntityProvider {

    private final Class<? extends GenericAMTE> teClazz;

    public GenericBlockWithTE(Material material, String name, Class<? extends GenericAMTE> clazz) {
        super(material, name, clazz);
        teClazz = clazz;
    }

    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraftHandler.immersiveCraft;
    }


    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        return currenttip;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        try {
            return teClazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public T getTE(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return (T) te;
    }
}
