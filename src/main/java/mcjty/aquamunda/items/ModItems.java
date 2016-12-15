package mcjty.aquamunda.items;

import mcjty.aquamunda.fluid.ItemFreshWaterBucket;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static ItemFreshWaterBucket freshWaterBucket;

    public static void init() {
        freshWaterBucket = new ItemFreshWaterBucket();
        ForgeRegistries.ITEMS.register(freshWaterBucket);
        // @todo CHECK COMPAT 1.10
//        FluidContainerRegistry.registerFluidContainer(FluidSetup.freshWater, new ItemStack(freshWaterBucket), new ItemStack(Items.bucket));
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        freshWaterBucket.initModel();
    }
}
