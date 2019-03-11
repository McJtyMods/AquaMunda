package mcjty.aquamunda.blocks.grindstone;


import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.ModBlocks;
import mcjty.aquamunda.compat.immcraft.ImmersiveCraftHandler;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.rendering.HandleTESR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class GrindStoneTESR extends HandleTESR<GrindStoneTE> {

    private IModel lidModel;
    private IBakedModel bakedLidModel;

    private IBakedModel getBakedLidModel() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedLidModel == null) {
            try {
                lidModel = ModelLoaderRegistry.getModel(new ResourceLocation(AquaMunda.MODID, "block/grindstone_top.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedLidModel = lidModel.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedLidModel;
    }


    public GrindStoneTESR() {
        super(ModBlocks.grindStoneBlock);
        textOffset = new Vec3d(0, 0, -.2);
    }

    @Override
    protected void renderExtra(GrindStoneTE tileEntity) {
        GlStateManager.pushMatrix();

        if (tileEntity.getGrindCounter() >= 0) {
            float t = ((long)(2000.0f * 2.0f * (float) tileEntity.getGrindCounter()) / tileEntity.getMaxGrindCounter()) % 2000;
            GlStateManager.rotate(360.0f * t / 2000.0f, 0, 1, 0);
        }

        GlStateManager.translate(-tileEntity.getPos().getX()-.5, -tileEntity.getPos().getY() - 1, -tileEntity.getPos().getZ()-.5);


        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = tileEntity.getWorld();
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, getBakedLidModel(), world.getBlockState(tileEntity.getPos()),
                tileEntity.getPos().up(),       // To fix chest lid lighting on the underside
                Tessellator.getInstance().getBuffer(), true);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Nonnull
    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraftHandler.immersiveCraft;
    }

    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(GrindStoneTE.class, new GrindStoneTESR());
    }
}
