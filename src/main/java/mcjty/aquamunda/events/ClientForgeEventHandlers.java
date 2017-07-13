package mcjty.aquamunda.events;


import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.sound.SoundController;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientForgeEventHandlers {

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModBlocks.initModels();
        ModItems.initModels();
    }

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> sounds) {
        SoundController.init(sounds);
    }


}
