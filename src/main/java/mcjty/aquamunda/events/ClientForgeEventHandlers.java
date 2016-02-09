package mcjty.aquamunda.events;


import mcjty.aquamunda.blocks.tank.TankISBM;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientForgeEventHandlers {

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        Object object =  event.modelRegistry.getObject(TankISBM.modelResourceLocation);
        if (object != null) {
            TankISBM customModel = new TankISBM();
            event.modelRegistry.putObject(TankISBM.modelResourceLocation, customModel);
        }
    }
}
