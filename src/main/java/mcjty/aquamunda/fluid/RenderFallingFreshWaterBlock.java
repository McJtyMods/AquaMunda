package mcjty.aquamunda.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFallingFreshWaterBlock extends Render<EntityFallingFreshWaterBlock> {

    public RenderFallingFreshWaterBlock(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityFallingFreshWaterBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
        System.out.println("RenderFallingFreshWaterBlock.doRender");
        if (entity.getBlock() != null) {
            this.bindTexture(TextureMap.locationBlocksTexture);
            IBlockState iblockstate = entity.getBlock();
            Block block = iblockstate.getBlock();
            BlockPos blockpos = new BlockPos(entity);
            World world = entity.getWorldObj();

            if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1) {
                if (block.getRenderType() == 3) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) x, (float) y, (float) z);
                    GlStateManager.disableLighting();
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                    int i = blockpos.getX();
                    int j = blockpos.getY();
                    int k = blockpos.getZ();
                    worldrenderer.setTranslation(((-i) - 0.5F), (-j), ((-k) - 0.5F));
                    BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                    IBakedModel ibakedmodel = blockrendererdispatcher.getBlockModelShapes().getModelForState(iblockstate);
//                    IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, null);
                    blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
                    worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    GlStateManager.enableLighting();
                    GlStateManager.popMatrix();
                    super.doRender(entity, x, y, z, entityYaw, partialTicks);
                }
            }
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityFallingFreshWaterBlock entity) {
        return TextureMap.locationBlocksTexture;
    }
}
