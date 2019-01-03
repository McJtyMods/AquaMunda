package mcjty.aquamunda.blocks.cooker;


import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.rendering.HandleTESR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
    public void render(CookerTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(tileEntity, x, y, z, partialTicks, destroyStage, alpha);
        renderBoilingState(tileEntity, x, y, z);
    }


    private Vec3d offsets[] = new Vec3d[] {
            new Vec3d(0, 0, 0),
            new Vec3d(.25, 0, .2),
            new Vec3d(0, 0, .26),
            new Vec3d(-.1, 0, .3),
            new Vec3d(-.17, 0, -.15),
            new Vec3d(-.3, 0, -.2),
            new Vec3d(.2, 0, -.3),
            new Vec3d(-.07, 0, .12),
            new Vec3d(.1, 0, -.1),
            new Vec3d(-.23, 0, .15),
    };
    private long timeOffsets[] = new long[] {
            0,
            133,
            300,
            448,
            750,
            1010,
            1200,
            1344,
            1800,
            2369,
    };

    private void renderBoilingState(CookerTE tileEntity, double x, double y, double z) {
        int boilingState = tileEntity.getBoilingState();
        if (boilingState < 1) {
            return;
        }

        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        this.bindTexture(particles);

        GlStateManager.disableAlpha();

        int brightness = 240;
        int b1 = brightness >> 16 & 65535;
        int b2 = brightness & 65535;

        float height = tileEntity.getContentsHeight();
        long time = System.currentTimeMillis();

        double u1 = 10.0 / 16;
        double v1 = 0.0;
        double u2 = 12.0 / 16;
        double v2 = 2.0 / 16;

        for (int i = 0 ; i < offsets.length ; i++) {
            float offset = ((time+timeOffsets[i]) % (3000 - boilingState * 250)) / 300.0f;

            if (offset < 1) {
                GlStateManager.pushMatrix();

                double ox = offsets[i].x + x + 0.5f;
                double oy = offsets[i].y + height - .47 + y + 0.5f;
                double oz = offsets[i].z + z + 0.5f;

                GlStateManager.translate(ox, oy, oz);

                rotateToPlayer();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

                double scale;
                if (boilingState < 3) {
                    scale = .002 + offset / 38.0f;
                } else {
                    scale = .01 + offset / (38.0f - boilingState * 2);
                }

                buffer.pos(-scale, -scale, 0).tex(u1, v1).lightmap(b1, b2).color(255, 255, 255, 255).endVertex();
                buffer.pos(-scale,  scale, 0).tex(u1, v2).lightmap(b1, b2).color(255, 255, 255, 255).endVertex();
                buffer.pos( scale,  scale, 0).tex(u2, v2).lightmap(b1, b2).color(255, 255, 255, 255).endVertex();
                buffer.pos( scale, -scale, 0).tex(u2, v1).lightmap(b1, b2).color(255, 255, 255, 255).endVertex();

                tessellator.draw();
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void rotateToPlayer() {
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
    }

    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(CookerTE.class, new CookerTESR());
    }
}
