package mcjty.aquamunda.items;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
        Block rock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("immcraft", "rock"));
        GameRegistry.addSmelting(dough, new ItemStack(Items.BREAD), 0);
        //@todo recipes
//        GameRegistry.addShapedRecipe(new ItemStack(kitchenKnife), "i  ", " i ", "  r", 'i', Items.IRON_INGOT, 'r', rock);
//        GameRegistry.addShapedRecipe(new ItemStack(doughRoller), "  s", " p ", "s  ", 's', Items.STICK, 'p', Blocks.PLANKS);
//        GameRegistry.addShapedRecipe(new ItemStack(manual), "br", "s ", 'b', Items.BOOK, 'r', rock, 's', Items.WHEAT_SEEDS);
//        GameRegistry.addShapedRecipe(new ItemStack(boardManual), "br", "s ", 'b', Items.BOOK, 'r', rock, 's', kitchenKnife);
//        GameRegistry.addShapedRecipe(new ItemStack(cookerManual), "br", "ss", 'b', Items.BOOK, 'r', rock, 's', Items.WHEAT_SEEDS);
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
