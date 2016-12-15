package mcjty.aquamunda;


import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.tank.TankModelLoader;
import mcjty.aquamunda.config.ConfigSetup;
import mcjty.aquamunda.environment.EnvironmentData;
import mcjty.aquamunda.events.ClientForgeEventHandlers;
import mcjty.aquamunda.events.ForgeEventHandlers;
import mcjty.aquamunda.fluid.EntityFallingFreshWaterBlock;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.network.PacketHandler;
import mcjty.aquamunda.waila.WailaCompatibility;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.lib.compat.CompatCreativeTabs;
import mcjty.lib.tools.EntityTools;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;


@Mod(modid = AquaMunda.MODID, name = AquaMunda.MODNAME,
        dependencies =
                "required-after:compatlayer@[" + AquaMunda.COMPATLAYER_VER + ",);" +
                "after:Forge@[" + AquaMunda.MIN_FORGE10_VER + ",);" +
                "after:forge@[" + AquaMunda.MIN_FORGE11_VER + ",)",
        version = AquaMunda.VERSION,
        acceptedMinecraftVersions = "[1.10,1.12)")
public class AquaMunda {
    public static final String MODID = "aquamunda";
    public static final String MODNAME = "Aqua Munda";
    public static final String VERSION = "1.0.0";
    public static final String MIN_IMMCRAFT_VER = "1.0.3";
    public static final String MIN_FORGE10_VER = "12.18.1.2082";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";
    public static final String COMPATLAYER_VER = "0.1.4";

    @SidedProxy
    public static CommonProxy proxy;

    @Mod.Instance
    public static AquaMunda instance;

    public static CreativeTabs creativeTab;

    public static Logger logger;

    public AquaMunda() {
        // This has to be done VERY early
        FluidRegistry.enableUniversalBucket();
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        FMLInterModComms.sendFunctionMessage("immcraft", "getApi", "mcjty.aquamunda.AquaMunda$GetImmCraftApi");

        logger = event.getModLog();
        creativeTab = new CompatCreativeTabs("Aqua Munda") {

            @Override
            protected Item getItem() {
                return Items.WATER_BUCKET;
            }
        };
        proxy.preInit(event);
        WailaCompatibility.registerWaila();
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        EnvironmentData.clearInstance();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    public static class CommonProxy {
        public void preInit(FMLPreInitializationEvent e) {
            PacketHandler.registerMessages("aquamunda");

            ConfigSetup.preInit(e);
            FluidSetup.preInitFluids();
            ModBlocks.init();
            ModItems.init();
//            WorldGen.init();
        }

        public void init(FMLInitializationEvent e) {
            MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
            ModBlocks.initCrafting();
//            ModItems.initCrafting();
            EntityTools.registerModEntity(new ResourceLocation(AquaMunda.MODID, "fresh_water_falling"), EntityFallingFreshWaterBlock.class, "fresh_water_falling", 1, AquaMunda.instance, 250, 5, true);
        }

        public void postInit(FMLPostInitializationEvent e) {
            ConfigSetup.postInit();
            ModBlocks.postInit();
        }
    }


    public static class ClientProxy extends CommonProxy {
        @Override
        public void preInit(FMLPreInitializationEvent e) {
            super.preInit(e);

            MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
            OBJLoader.INSTANCE.addDomain(MODID);
            ModelLoaderRegistry.registerLoader(new TankModelLoader());


            ModBlocks.initModels();
            ModItems.initModels();

            FluidSetup.initRenderer();
        }

        @Override
        public void init(FMLInitializationEvent e) {
            super.init(e);
            ModBlocks.initItemModels();
        }
    }

    public static class ServerProxy extends CommonProxy {

    }

    public static class GetImmCraftApi implements com.google.common.base.Function<IImmersiveCraft, Void> {
        @Nullable
        @Override
        public Void apply(IImmersiveCraft immcraft) {
            System.out.println("##############################################");
            System.out.println("immcraft = " + immcraft);
            ImmersiveCraftHandler.setImmersiveCraft(immcraft);
            return null;
        }
    }
}
