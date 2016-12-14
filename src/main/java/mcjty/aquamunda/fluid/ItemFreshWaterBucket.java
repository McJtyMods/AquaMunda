package mcjty.aquamunda.fluid;

import mcjty.aquamunda.blocks.ModBlocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFreshWaterBucket extends ItemBucket {

    public ItemFreshWaterBucket() {
        super(ModBlocks.blockFreshWater);
        setUnlocalizedName("fresh_water_bucket");
        setRegistryName("fresh_water_bucket");
        setContainerItem(Items.BUCKET);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
