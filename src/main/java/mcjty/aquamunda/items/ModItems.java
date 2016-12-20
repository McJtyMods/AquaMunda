package mcjty.aquamunda.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static ItemCookedCarrot cookedCarrot;
    public static ItemVegetableSoup vegetableSoup;
    public static ItemChoppedVegetables choppedVegetables;
    public static ItemDish dish;

    public static void init() {
        cookedCarrot = new ItemCookedCarrot();
        choppedVegetables = new ItemChoppedVegetables();
        vegetableSoup = new ItemVegetableSoup();
        dish = new ItemDish();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        cookedCarrot.initModel();
        choppedVegetables.initModel();
        vegetableSoup.initModel();
        dish.initModel();
    }
}
