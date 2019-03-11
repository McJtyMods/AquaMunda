package mcjty.aquamunda.setup;


import mcjty.aquamunda.AquaMunda;
import mcjty.lib.McJtyRegister;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Registration {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        McJtyRegister.registerBlocks(AquaMunda.instance, event.getRegistry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        McJtyRegister.registerItems(AquaMunda.instance, event.getRegistry());
    }


}
