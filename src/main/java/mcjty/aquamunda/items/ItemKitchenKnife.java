package mcjty.aquamunda.items;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.lib.compat.CompatItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKitchenKnife extends CompatItem {

    public ItemKitchenKnife() {
        super();
        setMaxStackSize(1);
        setRegistryName("kitchen_knife");
        setUnlocalizedName(AquaMunda.MODID + ".kitchen_knife");
        setCreativeTab(AquaMunda.creativeTab);
        ImmersiveCraftHandler.immersiveCraft.getRegistry().registerLater(this, AquaMunda.MODID);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
