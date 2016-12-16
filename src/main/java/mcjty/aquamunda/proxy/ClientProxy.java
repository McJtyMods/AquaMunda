package mcjty.aquamunda.proxy;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.tank.TankModelLoader;
import mcjty.aquamunda.events.ClientForgeEventHandlers;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.items.ModItems;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by jorrit on 16.12.16.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
        OBJLoader.INSTANCE.addDomain(AquaMunda.MODID);
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
