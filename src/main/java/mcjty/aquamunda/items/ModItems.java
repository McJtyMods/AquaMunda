package mcjty.aquamunda.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    public static ItemCookedCarrot cookedCarrot;
    public static ItemCookedPotato cookedPotato;
    public static ItemChoppedVegetables choppedVegetables;
    public static ItemDish dish;
    public static ItemKitchenKnife kitchenKnife;
    public static ItemDoughRoller doughRoller;
    public static ItemFlour flour;
    public static ItemDough dough;
    public static AquaMundaManual manual;
    public static BoardManual boardManual;
    public static CookerManual cookerManual;

    public static void init() {
        cookedCarrot = new ItemCookedCarrot();
        cookedPotato = new ItemCookedPotato();
        choppedVegetables = new ItemChoppedVegetables();
        dish = new ItemDish();
        kitchenKnife = new ItemKitchenKnife();
        doughRoller = new ItemDoughRoller();
        flour = new ItemFlour();
        dough = new ItemDough();
        manual = new AquaMundaManual();
        boardManual = new BoardManual();
        cookerManual = new CookerManual();
    }

    public static void initCrafting() {
        GameRegistry.addSmelting(dough, new ItemStack(Items.BREAD), 0);
    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        cookedCarrot.initModel();
        cookedPotato.initModel();
        choppedVegetables.initModel();
        dish.initModel();
        kitchenKnife.initModel();
        doughRoller.initModel();
        flour.initModel();
        dough.initModel();
        manual.initModel();
        boardManual.initModel();
        cookerManual.initModel();
    }
}
