package mcjty.aquamunda.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static ItemCookedCarrot cookedCarrot;
    public static ItemChoppedVegetables choppedVegetables;
    public static ItemDish dish;
    public static ItemKitchenKnife kitchenKnife;
    public static ItemFlour flour;
    public static ItemDough dough;

    public static void init() {
        cookedCarrot = new ItemCookedCarrot();
        choppedVegetables = new ItemChoppedVegetables();
        dish = new ItemDish();
        kitchenKnife = new ItemKitchenKnife();
        flour = new ItemFlour();
        dough = new ItemDough();
    }

    public static void initCrafting() {
        GameRegistry.addSmelting(dough, new ItemStack(Items.BREAD), 0);
    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        cookedCarrot.initModel();
        choppedVegetables.initModel();
        dish.initModel();
        kitchenKnife.initModel();
        flour.initModel();
        dough.initModel();
    }
}
