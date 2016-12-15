package mcjty.aquamunda.items;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static void init() {
        // @todo CHECK COMPAT 1.10
//        FluidContainerRegistry.registerFluidContainer(FluidSetup.freshWater, new ItemStack(freshWaterBucket), new ItemStack(Items.bucket));
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {

    }
}
