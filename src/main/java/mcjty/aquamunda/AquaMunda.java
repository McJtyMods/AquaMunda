package mcjty.aquamunda;


import mcjty.aquamunda.api.IAquaMunda;
import mcjty.aquamunda.apiimpl.AquaMundaImp;
import mcjty.aquamunda.compat.MainCompatHandler;
import mcjty.aquamunda.environment.EnvironmentData;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.aquamunda.proxy.CommonProxy;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.lib.base.ModBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;


@Mod(modid = AquaMunda.MODID, name = AquaMunda.MODNAME,
        dependencies =
                "required-after:mcjtylib_ng@[" + AquaMunda.MIN_MCJTYLIB_VER + ",);" +
                "required-after:immcraft@[" + AquaMunda.MIN_IMMCRAFT_VER + ",);" +
                "after:forge@[" + AquaMunda.MIN_FORGE11_VER + ",)",
        acceptedMinecraftVersions = "[1.12,1.13)",
        version = AquaMunda.VERSION)
public class AquaMunda implements ModBase {
    public static final String MODID = "aquamunda";
    public static final String MODNAME = "Aqua Munda";
    public static final String MIN_IMMCRAFT_VER = "1.5.0";
    public static final String VERSION = "0.4.0";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";
    public static final String MIN_MCJTYLIB_VER = "3.0.0";

    @SidedProxy(clientSide = "mcjty.aquamunda.proxy.ClientProxy", serverSide = "mcjty.aquamunda.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static AquaMunda instance;

    public static AquaMundaImp aquaMundaImp = new AquaMundaImp();

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
        creativeTab = new CreativeTabs("aquamunda") {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(Items.WATER_BUCKET);
            }
        };
        proxy.preInit(event);
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
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

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if ("getapi".equalsIgnoreCase(message.key)) {
                Optional<Function<IAquaMunda, Void>> value = message.getFunctionValue(IAquaMunda.class, Void.class);
                value.get().apply(aquaMundaImp);
            }
        }
    }

    @Override
    public String getModId() {
        return AquaMunda.MODID;
    }

    @Override
    public void openManual(EntityPlayer player, int bookindex, String page) {
        // @todo
    }
}
