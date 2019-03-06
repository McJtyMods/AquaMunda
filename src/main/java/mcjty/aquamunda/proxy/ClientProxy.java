package mcjty.aquamunda.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.tank.TankModelLoader;
import mcjty.aquamunda.events.ClientForgeEventHandlers;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.lib.McJtyLibClient;
import mcjty.lib.setup.DefaultClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.concurrent.Callable;

public class ClientProxy extends DefaultClientProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        McJtyLibClient.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
        OBJLoader.INSTANCE.addDomain(AquaMunda.MODID);
        ModelLoaderRegistry.registerLoader(new TankModelLoader());
        FluidSetup.initRenderer();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ModBlocks.initItemModels();
    }
}
