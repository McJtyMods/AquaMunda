package mcjty.aquamunda.items;

import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.AquaMundaRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDoughRoller extends Item {

    public ItemDoughRoller() {
        super();
        setMaxStackSize(1);
        setRegistryName("dough_roller");
        setUnlocalizedName(AquaMunda.MODID + ".dough_roller");
        setCreativeTab(AquaMunda.creativeTab);
        AquaMundaRegister.getRegistry().registerLater(this, AquaMunda.MODID);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
