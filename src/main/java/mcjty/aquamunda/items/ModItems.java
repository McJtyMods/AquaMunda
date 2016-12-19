package mcjty.aquamunda.items;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static ItemCookedCarrot cookedCarrot;
    public static ItemDish dish;

    public static void init() {
        cookedCarrot = new ItemCookedCarrot();
        dish = new ItemDish();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        cookedCarrot.initModel();
        dish.initModel();
    }
}
