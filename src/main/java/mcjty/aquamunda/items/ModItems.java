package mcjty.aquamunda.items;

import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.fluid.ItemFreshWaterBucket;
import mezz.jei.plugins.vanilla.ingredients.FluidStackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static ItemFreshWaterBucket freshWaterBucket;

    public static void init() {
        freshWaterBucket = new ItemFreshWaterBucket();
        freshWaterBucket.setRegistryName("fresh_water_bucket");
        ForgeRegistries.ITEMS.register(freshWaterBucket);
        // @todo CHECK COMPAT 1.10
//        FluidContainerRegistry.registerFluidContainer(FluidSetup.freshWater, new ItemStack(freshWaterBucket), new ItemStack(Items.bucket));
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        freshWaterBucket.initModel();
    }
}
