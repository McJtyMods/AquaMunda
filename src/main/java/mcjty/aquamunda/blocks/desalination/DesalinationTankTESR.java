package mcjty.aquamunda.blocks.desalination;


import mcjty.aquamunda.blocks.generic.GenericAMBlock;
import mcjty.aquamunda.fluid.FluidSetup;
import mcjty.aquamunda.network.PacketGetInfoFromServer;
import mcjty.aquamunda.network.PacketHandler;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class DesalinationTankTESR extends TileEntitySpecialRenderer<DesalinationTankTE> {

    public DesalinationTankTESR() {
    }

    @Override
    public void renderTileEntityAt(DesalinationTankTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + .5, y, z + .5);
        GlStateManager.disableRescaleNormal();

        BlockRenderHelper.rotateFacing(tileEntity, GenericAMBlock.MetaUsage.HORIZROTATION);
        renderExtra(tileEntity);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

    private static long lastUpdateTime = 0;

    private void renderExtra(DesalinationTankTE tileEntity) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        long time = System.currentTimeMillis();
        if ((time - lastUpdateTime) > 200) {
            lastUpdateTime = time;
            PacketHandler.INSTANCE.sendToServer(new PacketGetInfoFromServer(new TankContentsInfoPacketServer(tileEntity.getPos())));
        }
        float percentage = tileEntity.getFilledPercentage();

        if (percentage > .01f) {
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            ResourceLocation still = FluidSetup.freshWater.getStill();
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(still.toString());

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

            float scalex = .30f;
            float scaley = percentage / 240.0f;
            float offsety = percentage / 480.0f + .24f;
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer renderer = tessellator.getBuffer();
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            renderer.pos(-scalex, offsety - scaley, -scalex).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            renderer.pos(-scalex, offsety + scaley, -scalex).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            renderer.pos(+scalex, offsety + scaley, -scalex).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            renderer.pos(+scalex, offsety - scaley, -scalex).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();

            renderer.pos(+scalex, offsety - scaley, +scalex).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            renderer.pos(+scalex, offsety + scaley, +scalex).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            renderer.pos(-scalex, offsety + scaley, +scalex).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            renderer.pos(-scalex, offsety - scaley, +scalex).tex(sprite.getMinU(), sprite.getMinV()).endVertex();

            renderer.pos(-scalex, offsety - scaley, +scalex).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            renderer.pos(-scalex, offsety + scaley, +scalex).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            renderer.pos(-scalex, offsety + scaley, -scalex).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            renderer.pos(-scalex, offsety - scaley, -scalex).tex(sprite.getMinU(), sprite.getMinV()).endVertex();

            renderer.pos(+scalex, offsety - scaley, -scalex).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            renderer.pos(+scalex, offsety + scaley, -scalex).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            renderer.pos(+scalex, offsety + scaley, +scalex).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            renderer.pos(+scalex, offsety - scaley, +scalex).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();

            // Top
            renderer.pos(-scalex, offsety + scaley, -scalex).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            renderer.pos(-scalex, offsety + scaley, +scalex).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            renderer.pos(+scalex, offsety + scaley, +scalex).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            renderer.pos(+scalex, offsety + scaley, -scalex).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();

            // Bottom
            renderer.pos(+scalex, offsety - scaley, -scalex).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            renderer.pos(+scalex, offsety - scaley, +scalex).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
            renderer.pos(-scalex, offsety - scaley, +scalex).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
            renderer.pos(-scalex, offsety - scaley, -scalex).tex(sprite.getMinU(), sprite.getMinV()).endVertex();

            tessellator.draw();
        }
    }
}
