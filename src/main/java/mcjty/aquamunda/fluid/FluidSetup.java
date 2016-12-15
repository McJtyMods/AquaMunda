package mcjty.aquamunda.fluid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FluidSetup {
    public static final Fluid freshWater = new Fluid("fresh_water", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"));

    public static void preInitFluids() {
        FluidRegistry.registerFluid(freshWater);
        FluidRegistry.addBucketForFluid(freshWater);
    }

    public static boolean isValidFreshWaterStack(FluidStack stack){
        return getFluidFromStack(stack) == freshWater;
    }

    public static Fluid getFluidFromStack(FluidStack stack){
        return stack == null ? null : stack.getFluid();
    }

    public static String getFluidName(FluidStack stack){
        Fluid fluid = getFluidFromStack(stack);
        return getFluidName(fluid);
    }

    public static String getFluidName(Fluid fluid){
        return fluid == null ? "null" : fluid.getName();
    }

    public static int getAmount(FluidStack stack){
        return stack == null ? 0 : stack.amount;
    }

    @SideOnly(Side.CLIENT)
    public static void initRenderer() {
//        RenderingRegistry.registerEntityRenderingHandler(EntityFallingFreshWaterBlock.class, new RenderFallingFreshWaterBlock(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityFallingFreshWaterBlock.class, new IRenderFactory<EntityFallingFreshWaterBlock>() {
            @Override
            public Render<? super EntityFallingFreshWaterBlock> createRenderFor(RenderManager manager) {
                System.out.println("FluidSetup.createRenderFor");
                return new RenderFallingFreshWaterBlock(manager);
            }
        });

    }
}
