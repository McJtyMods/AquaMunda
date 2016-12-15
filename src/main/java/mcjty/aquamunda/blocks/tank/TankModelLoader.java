package mcjty.aquamunda.blocks.tank;

import mcjty.aquamunda.AquaMunda;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class TankModelLoader implements ICustomModelLoader {

    public static final TankModel TANK_MODEL = new TankModel();


    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(AquaMunda.MODID) && "tankmodelblock".equals(modelLocation.getResourcePath());
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return TANK_MODEL;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
