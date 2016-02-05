package mcjty.aquamunda.rendering;


//import net.minecraftforge.client.IItemRenderer;
//import net.minecraftforge.client.model.AdvancedModelLoader;
//import net.minecraftforge.client.model.IModelCustom;

public class GenericItemRenderer {
//
//} implements IItemRenderer {
//
//    private IModelCustom model;
//    private ResourceLocation blockTexture;
//
//    public GenericItemRenderer(String modelName, String texture) {
//        if (modelName != null) {
//            model = AdvancedModelLoader.loadModel(new ResourceLocation(Gaia.MODID, modelName));
//        }
//        if (texture != null) {
//            blockTexture = new ResourceLocation(Gaia.MODID, texture);
//        }
//    }
//
//    @Override
//    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
//        return true;
//    }
//
//    @Override
//    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
//        return true;
//    }
//
//    @Override
//    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
//        if (blockTexture == null || model == null) {
//            return;
//        }
//        FMLClientHandler.instance().getClient().renderEngine.bindTexture(blockTexture);
//
//        GL11.glPushMatrix();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        GL11.glTranslatef(0.5F, 0.0F, 0.5F);
//
//        model.renderAll();
//        GL11.glPopMatrix();
//    }
}
