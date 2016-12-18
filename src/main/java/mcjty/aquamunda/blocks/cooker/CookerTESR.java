package mcjty.aquamunda.blocks.cooker;


import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.rendering.HandleTESR;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class CookerTESR extends HandleTESR<CookerTE> {

    private IModel lidModel;
    private IBakedModel bakedLidModel;

    public CookerTESR() {
        super(ModBlocks.cookerBlock);
        textOffset = new Vec3d(0, 0, -.2);
    }

    @Nonnull
    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraftHandler.immersiveCraft;
    }

    @Override
    protected void renderExtra(CookerTE tileEntity) {
    }
}
