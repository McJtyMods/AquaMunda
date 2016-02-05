package mcjty.aquamunda;


import mcjty.aquamunda.config.ConfigSetup;
import mcjty.aquamunda.events.ClientForgeEventHandlers;
import mcjty.aquamunda.events.ForgeEventHandlers;
import mcjty.aquamunda.network.PacketHandler;
import mcjty.aquamunda.waila.WailaCompatibility;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = AquaMunda.MODID, name = AquaMunda.MODNAME, dependencies = "required-after:Forge@[11.15.0.1684,)", useMetadata = true,
        version = AquaMunda.VERSION)
public class AquaMunda {

    public static final String MODID = "aquamunda";
    public static final String MODNAME = "Aqua Munda";
    public static final String VERSION = "1.0.0";

    @SidedProxy
    public static CommonProxy proxy;

    @Mod.Instance
    public static AquaMunda instance;

//    public static CreativeTabs creativeTab;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
//        creativeTab = new CreativeTabs("immcraft") {
//            @Override
//            public Item getTabIconItem() {
//                return Item.getItemFromBlock(ModBlocks.rockBlock);
//            }
//        };
        proxy.preInit(event);
        WailaCompatibility.registerWaila();
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
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
            PacketHandler.registerMessages("immcraft");

            ConfigSetup.preInit(e);
//            ModBlocks.init();
//            ModItems.init();
//            WorldGen.init();
        }

        public void init(FMLInitializationEvent e) {
            MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
//            ModBlocks.initCrafting();
//            ModItems.initCrafting();
        }

        public void postInit(FMLPostInitializationEvent e) {
            ConfigSetup.postInit();
        }
    }


    public static class ClientProxy extends CommonProxy {
        @Override
        public void preInit(FMLPreInitializationEvent e) {
            super.preInit(e);

            MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
//            OBJLoader.instance.addDomain(MODID);

//            ModBlocks.initModels();
//            ModItems.initModels();
        }

        @Override
        public void init(FMLInitializationEvent e) {
            super.init(e);
        }
    }

    public static class ServerProxy extends CommonProxy {

    }
}
