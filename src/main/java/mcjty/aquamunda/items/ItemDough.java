package mcjty.aquamunda.items;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import mcjty.lib.compat.CompatItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDough extends CompatItem {

    public ItemDough() {
        super();
        setMaxStackSize(16);
        setRegistryName("dough");
        setUnlocalizedName(AquaMunda.MODID + ".dough");
        setCreativeTab(AquaMunda.creativeTab);
        ImmersiveCraftHandler.immersiveCraft.getRegistry().registerLater(this, AquaMunda.MODID);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
