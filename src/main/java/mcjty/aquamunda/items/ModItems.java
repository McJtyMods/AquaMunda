package mcjty.aquamunda.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static ItemCookedCarrot cookedCarrot;
    public static ItemChoppedVegetables choppedVegetables;
    public static ItemDish dish;
    public static ItemKitchenKnife kitchenKnife;

    public static void init() {
        cookedCarrot = new ItemCookedCarrot();
        choppedVegetables = new ItemChoppedVegetables();
        dish = new ItemDish();
        kitchenKnife = new ItemKitchenKnife();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        cookedCarrot.initModel();
        choppedVegetables.initModel();
        dish.initModel();
        kitchenKnife.initModel();
    }
}
