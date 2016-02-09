package mcjty.aquamunda.items;

import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.fluid.BucketHandler;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.fluid.ItemFreshWaterBucket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static ItemFreshWaterBucket freshWaterBucket;

    public static void init() {
        freshWaterBucket = new ItemFreshWaterBucket();
        GameRegistry.registerItem(freshWaterBucket, "fresh_water_bucket");
        FluidContainerRegistry.registerFluidContainer(FluidSetup.freshWater, new ItemStack(freshWaterBucket), new ItemStack(Items.bucket));
        BucketHandler.INSTANCE.buckets.put(ModBlocks.blockFreshWater, freshWaterBucket);
        MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        freshWaterBucket.initModel();
    }
}