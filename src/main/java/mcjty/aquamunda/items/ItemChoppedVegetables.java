package mcjty.aquamunda.items;

import mcjty.aquamunda.AquaMunda;
import mcjty.lib.McJtyRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChoppedVegetables extends Item {

    public ItemChoppedVegetables() {
        super();
        setMaxStackSize(16);
        setRegistryName("chopped_vegetables");
        setUnlocalizedName(AquaMunda.MODID + ".chopped_vegetables");
        setCreativeTab(AquaMunda.creativeTab);
        McJtyRegister.registerLater(this, AquaMunda.instance);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
