package mcjty.aquamunda.blocks.cuttingboard;


import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.rendering.HandleTESR;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class CuttingBoardTESR extends HandleTESR<CuttingBoardTE> {

    public CuttingBoardTESR() {
        super(ModBlocks.cuttingBoardBlock);
        textOffset = new Vec3d(0, 0, -.2);
    }

    @Nonnull
    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraftHandler.immersiveCraft;
    }

    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(CuttingBoardTE.class, new CuttingBoardTESR());
    }
}
