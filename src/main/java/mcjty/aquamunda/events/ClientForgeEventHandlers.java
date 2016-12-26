package mcjty.aquamunda.events;


import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientForgeEventHandlers {


    @SubscribeEvent
    public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
        RayTraceResult target = e.getTarget();
        String id = null;
        if (target.hitInfo instanceof HandleSelector) {
            id = ((HandleSelector) target.hitInfo).getId();
        }
        BlockRenderHelper.renderHandleBoxes(id, e.getPlayer(), e.getPartialTicks(), target.getBlockPos());
    }

}
