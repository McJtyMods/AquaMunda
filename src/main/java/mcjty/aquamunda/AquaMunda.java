package mcjty.aquamunda;


import mcjty.aquamunda.api.IAquaMunda;
import mcjty.aquamunda.apiimpl.AquaMundaImp;
import mcjty.aquamunda.setup.CommonSetup;
import mcjty.lib.base.ModBase;
import mcjty.lib.proxy.IProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
    public static final String VERSION = "0.5.0";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";
    public static final String MIN_MCJTYLIB_VER = "3.1.0";

    @SidedProxy(clientSide = "mcjty.aquamunda.setup.ClientProxy", serverSide = "mcjty.aquamunda.setup.ServerProxy")
    public static IProxy proxy;
    public static CommonSetup setup = new CommonSetup();

    @Mod.Instance
    public static AquaMunda instance;

    public static AquaMundaImp aquaMundaImp = new AquaMundaImp();

    public AquaMunda() {
        // This has to be done VERY early
        FluidRegistry.enableUniversalBucket();
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        setup.preInit(event);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        setup.init(e);
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        setup.postInit(e);
        proxy.postInit(e);
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
