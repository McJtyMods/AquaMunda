package mcjty.aquamunda.proxy;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.config.ConfigSetup;
import mcjty.aquamunda.events.ForgeEventHandlers;
import mcjty.aquamunda.fluid.EntityFallingFreshWaterBlock;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.network.PacketHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by jorrit on 16.12.16.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        PacketHandler.registerMessages("aquamunda");

        ConfigSetup.preInit(e);

        FluidSetup.preInitFluids();
        ModBlocks.init();
        ModItems.init();
//            WorldGen.init();
    }

    public void init(FMLInitializationEvent e) {
        ModItems.initCrafting();
        EntityRegistry.registerModEntity(new ResourceLocation(AquaMunda.MODID, "fresh_water_falling"), EntityFallingFreshWaterBlock.class, "fresh_water_falling", 1, AquaMunda.instance, 250, 5, true);

        ConfigSetup.readRecipesConfig();
    }

    public void postInit(FMLPostInitializationEvent e) {
        ConfigSetup.postInit();
        ModBlocks.postInit();
    }
}
