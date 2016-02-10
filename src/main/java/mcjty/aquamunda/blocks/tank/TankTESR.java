package mcjty.aquamunda.blocks.tank;


import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TankTESR extends TileEntitySpecialRenderer<TankTE> {

    public static final float TANK_THICKNESS = 0.05f;

    public TankTESR() {
    }

    @Override
    public void renderTileEntityAt(TankTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
//        GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_TEXTURE_BIT);

        GlStateManager.pushMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.translate((float) x, (float) y, (float) z);

        Tessellator tessellator = Tessellator.getInstance();

        BlockPos pos = tileEntity.getPos();

        TankNetwork tankNetwork = TankNetwork.getClientSide();
        int id = tileEntity.getID();
        tankNetwork.getNetwork().refreshInfo(id);
        IMultiBlockClientInfo clientInfo = tankNetwork.getNetwork().getClientInfo(id);

        bindTexture(TextureMap.locationBlocksTexture);
        renderFluid(tessellator, (TankClientInfo) clientInfo, pos);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderFluid(Tessellator tessellator, TankClientInfo tankInfo, BlockPos pos) {
        if (tankInfo == null) {
            return;
        }

        Fluid renderFluid = tankInfo.getFluid();
        if (renderFluid == null) {
            return;
        }

        float scale = (1.0f - TANK_THICKNESS/2 - TANK_THICKNESS) * tankInfo.getContents() / (tankInfo.getBlockCount() * TankTE.MAX_CONTENTS);

        if (scale > 0.0f) {
            WorldRenderer renderer = tessellator.getWorldRenderer();
            ResourceLocation still = renderFluid.getStill();
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(still.toString());

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

            GlStateManager.color(1, 1, 1, .5f);
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

            float u1 = sprite.getMinU();
            float v1 = sprite.getMinV();
            float u2 = sprite.getMaxU();
            float v2 = sprite.getMaxV();

            int brightness = ModBlocks.tankBlock.getMixedBrightnessForBlock(Minecraft.getMinecraft().theWorld, pos);
            int b1 = brightness >> 16 & 65535;
            int b2 = brightness & 65535;

            // Top
            renderer.pos(0, scale + TANK_THICKNESS, 0).tex(u1, v1).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();
            renderer.pos(0, scale + TANK_THICKNESS, 1).tex(u1, v2).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();;
            renderer.pos(1, scale + TANK_THICKNESS, 1).tex(u2, v2).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();;
            renderer.pos(1, scale + TANK_THICKNESS, 0).tex(u2, v1).lightmap(b1, b2).color(255, 255, 255, 128).endVertex();;

            tessellator.draw();

            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        }
    }
}
