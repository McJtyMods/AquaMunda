package mcjty.aquamunda.blocks.cooker;


import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.rendering.HandleTESR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class CookerTESR extends HandleTESR<CookerTE> {

    public static ResourceLocation particles = new ResourceLocation(AquaMunda.MODID, "textures/effects/particles.png");


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
    public void renderTileEntityAt(CookerTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(tileEntity, x, y, z, partialTicks, destroyStage);

        renderCookingState(tileEntity);
    }

    private void renderCookingState(CookerTE tileEntity) {
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        this.bindTexture(particles);

        GlStateManager.disableAlpha();

        GlStateManager.pushMatrix();
        int x = tileEntity.getPos().getX();
        int y = tileEntity.getPos().getY();
        int z = tileEntity.getPos().getZ();
        GlStateManager.translate(x + 0.5F, y + 0.5F, z + 0.5F);

        rotateToPlayer();

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        int brightness = 240;
        int b1 = brightness >> 16 & 65535;
        int b2 = brightness & 65535;

        double ox = 0;
        double oy = 0.8;
        double oz = 0;
        double scale = .4;
        double u1 = 0.0;
        double v1 = 0.0;
        double u2 = 1.0/16;
        double v2 = 1.0/16;

        buffer.pos(ox - scale, oy-scale, oz).tex(u1, v1).lightmap(b1, b2).color(255, 255, 255, 255).endVertex();
        buffer.pos(ox - scale, oy+scale, oz).tex(u1, v2).lightmap(b1, b2).color(255, 255, 255, 255).endVertex();
        buffer.pos(ox + scale, oy+scale, oz).tex(u2, v2).lightmap(b1, b2).color(255, 255, 255, 255).endVertex();
        buffer.pos(ox + scale, oy-scale, oz).tex(u2, v1).lightmap(b1, b2).color(255, 255, 255, 255).endVertex();

        tessellator.draw();
        GlStateManager.popMatrix();

        GlStateManager.depthMask(true);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void rotateToPlayer() {
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
    }
}
