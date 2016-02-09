package mcjty.aquamunda;


import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.tank.TankNetwork;
import mcjty.aquamunda.config.ConfigSetup;
import mcjty.aquamunda.environment.EnvironmentData;
import mcjty.aquamunda.events.ClientForgeEventHandlers;
import mcjty.aquamunda.events.ForgeEventHandlers;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.hosemultiblock.HoseNetwork;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.network.PacketHandler;
import mcjty.aquamunda.waila.WailaCompatibility;
import mcjty.immcraft.api.IImmersiveCraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(modid = AquaMunda.MODID, name = AquaMunda.MODNAME, dependencies =
        "required-after:Forge@[11.15.0.1684" +
                ",);required-after:immcraft@["+AquaMunda.MIN_IMMCRAFT_VER+
                ",)", useMetadata = true,
        version = AquaMunda.VERSION)
public class AquaMunda {

    public static final String MODID = "aquamunda";
    public static final String MODNAME = "Aqua Munda";
    public static final String VERSION = "1.0.0";
    public static final String MIN_IMMCRAFT_VER = "1.0.3";

    @SidedProxy
    public static CommonProxy proxy;

    @Mod.Instance
    public static AquaMunda instance;

    public static CreativeTabs creativeTab;

    public static Logger logger;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        creativeTab = new CreativeTabs("immcraft") {
            @Override
            public Item getTabIconItem() {
                return Items.water_bucket;
            }
        };
        proxy.preInit(event);
        FMLInterModComms.sendFunctionMessage("immcraft", "getApi", "mcjty.aquamunda.AquaMunda$GetImmCraftApi");
        WailaCompatibility.registerWaila();
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        TankNetwork.clearInstance();
        HoseNetwork.clearInstance();
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
            OBJLoader.instance.addDomain(MODID);

            ModBlocks.initModels();
            ModItems.initModels();
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
            ImmersiveCraftHandler.setImmersiveCraft(immcraft);
            return null;
        }
    }
}
