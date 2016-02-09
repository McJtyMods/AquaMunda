package mcjty.aquamunda.events;


import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.blocks.bundle.BundleISBM;
import mcjty.aquamunda.blocks.bundle.BundleTE;
import mcjty.aquamunda.blocks.tank.TankISBM;
import mcjty.aquamunda.cables.CableRenderer;
import mcjty.aquamunda.cables.CableSection;
import mcjty.aquamunda.varia.BlockTools;
import mcjty.immcraft.api.util.Vector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientForgeEventHandlers {
    @SubscribeEvent
    public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
        EntityPlayer p = event.player;
        World world = p.worldObj;
        BlockPos pos = event.target.getBlockPos();
        if (pos == null) {
            return;
        }
        IBlockState state = world.getBlockState(pos);
        if (state == null) {
            return;
        }
        Block block = state.getBlock();
        if (block == ModBlocks.bundleBlock) {
            float time = event.partialTicks;
            double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * time;
            double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * time;
            double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * time;
            Vector player = new Vector((float) doubleX, (float) doubleY, (float) doubleZ);
            Vector hitVec = new Vector((float) event.target.hitVec.xCoord, (float) event.target.hitVec.yCoord, (float) event.target.hitVec.zCoord);

            BundleTE bundleTE = BlockTools.getTE(BundleTE.class, world, pos).get();
            CableSection closestSection = CableRenderer.findSelectedCable(player, hitVec, bundleTE);

            if (closestSection != null) {
                CableRenderer.renderHilightedCable(player, closestSection);
            }

//            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        Object object =  event.modelRegistry.getObject(TankISBM.modelResourceLocation);
        if (object != null) {
            TankISBM customModel = new TankISBM();
            event.modelRegistry.putObject(TankISBM.modelResourceLocation, customModel);
        }

        object =  event.modelRegistry.getObject(BundleISBM.modelResourceLocation);
        if (object != null) {
            BundleISBM customModel = new BundleISBM();
            event.modelRegistry.putObject(BundleISBM.modelResourceLocation, customModel);
        }
    }
}
