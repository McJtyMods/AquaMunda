package mcjty.aquamunda.fluid;

import mcjty.aquamunda.blocks.ModBlocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemFreshWaterBucket extends ItemBucket {

    public ItemFreshWaterBucket() {
        super(ModBlocks.blockFreshWater);
        setUnlocalizedName("freshWaterBucket");
//        setTextureName("minecraft:bucket_water");
        setContainerItem(Items.bucket);
    }
}
