package mcjty.aquamunda.rendering;


import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.generic.GenericAMBlock;
import mcjty.aquamunda.blocks.generic.GenericAMTE;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GenericTESR<T extends GenericAMTE> extends TileEntitySpecialRenderer<T> {

    protected IModel model;
    protected IBakedModel bakedModel;
    protected ResourceLocation blockTexture;
    protected GenericAMBlock block;

    protected Vec3d textOffset = new Vec3d(0, 0, 0);

    public GenericTESR(String modelName, String texture, GenericAMBlock block) {
        if (modelName != null) {
            try {
                model = ModelLoaderRegistry.getModel(new ResourceLocation(AquaMunda.MODID, modelName));
                bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.POSITION_TEX, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
            } catch (Exception e) {
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

    protected IBakedModel getModel(T tileEntity) {
        return bakedModel;
    }

    protected void translate(T tileEntity, float x, float y, float z) {
        GL11.glTranslatef(x + 0.5F, y + 0.0F, z + 0.5F);
    }

    protected void renderExtra(T tileEntity) {

    }
}
