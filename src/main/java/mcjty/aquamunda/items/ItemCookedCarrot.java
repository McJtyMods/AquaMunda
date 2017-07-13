package mcjty.aquamunda.items;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.cooker.ICookerResult;
import mcjty.aquamunda.immcraft.ImmersiveCraftHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCookedCarrot extends Item implements ICookerResult {

    public ItemCookedCarrot() {
        super();
        setMaxStackSize(16);
        setRegistryName("cooked_carrot");
        setUnlocalizedName(AquaMunda.MODID + ".cooked_carrot");
        setCreativeTab(AquaMunda.creativeTab);
        ImmersiveCraftHandler.immersiveCraft.getRegistry().registerLater(this, AquaMunda.MODID);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public String getDishName() {
        return ItemDish.DISH_CARROTS;
    }
}
