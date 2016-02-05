package mcjty.aquamunda.rendering;


import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.generic.GenericBlock;
import mcjty.aquamunda.blocks.generic.GenericTE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GenericTESR<T extends GenericTE> extends TileEntitySpecialRenderer<T> {

    protected IModel model;
    protected IBakedModel bakedModel;
    protected ResourceLocation blockTexture;
    protected GenericBlock block;

    protected Vec3 textOffset = new Vec3(0, 0, 0);

    public GenericTESR(String modelName, String texture, GenericBlock block) {
        if (modelName != null) {
            try {
                model = ModelLoaderRegistry.getModel(new ResourceLocation(AquaMunda.MODID, modelName));
                bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.POSITION_TEX, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            blockTexture = new ResourceLocation(AquaMunda.MODID, texture);
        }
        this.block = block;
    }

    @Override
    public void renderTileEntityAt(T tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + .5, y, z + .5);
        GlStateManager.disableRescaleNormal();

        BlockRenderHelper.rotateFacing(tileEntity, block.getMetaUsage());
//        renderHandles(tileEntity);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void XrenderTileEntityAt(T tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ENABLE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_TEXTURE_BIT);

        GL11.glPushMatrix();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        translate(tileEntity, (float) x, (float) y, (float) z);
        BlockRenderHelper.rotateFacing(tileEntity, block.getMetaUsage());
        IBakedModel m = getModel(tileEntity);
        if (m != null) {
            bindTexture(blockTexture);
//            model.bake()
            /*
<fry> so, call IModel.bake, get baked model, call getFaceQuads + getGeneralQuads, for each of those quads - feed them to WorldRenderer
<fry> either manually or doint IVertexConsumer cons = new WorldRendererConsumer(worldRenderer); quad.pipe(cons);
             */
            World world = tileEntity.getWorld();
            Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, m, world.getBlockState(tileEntity.getPos()), tileEntity.getPos(), Tessellator.getInstance().getWorldRenderer());
        }

        renderExtra(tileEntity);

        GL11.glPopMatrix();

        GL11.glPopAttrib();
    }

    protected IBakedModel getModel(T tileEntity) {
        return bakedModel;
    }

    protected void translate(T tileEntity, float x, float y, float z) {
        GL11.glTranslatef(x + 0.5F, y + 0.0F, z + 0.5F);
    }

    protected void renderExtra(T tileEntity) {

    }
}
