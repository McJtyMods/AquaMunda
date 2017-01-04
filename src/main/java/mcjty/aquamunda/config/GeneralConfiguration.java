package mcjty.aquamunda.config;

import mcjty.aquamunda.environment.FarmlandOverhaulType;
import mcjty.aquamunda.items.ItemDish;
import mcjty.aquamunda.items.ModItems;
import mcjty.aquamunda.recipes.CookerRecipe;
import mcjty.aquamunda.recipes.CookerRecipeRepository;
import mcjty.aquamunda.recipes.CuttingBoardRecipe;
import mcjty.aquamunda.recipes.CuttingBoardRecipeRepository;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Map;

public class GeneralConfiguration {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_RECIPES_CUTTINGBOARD = "recipescuttingboard";
    public static final String CATEGORY_RECIPES_COOKER = "recipescooker";

    public static float baseCookerVolume = 0.6f;     // Use 0 to turn off
    public static float baseChoppingVolume = 1.0f;   // Use 0 to turn off
    public static float baseGrindstoneVolume = 0.6f; // Use 0 to turn off

    public static FarmlandOverhaulType farmlandOverhaulType = FarmlandOverhaulType.FRESH;

    public static void init(Configuration cfg) {
        baseCookerVolume = (float) cfg.get(CATEGORY_GENERAL, "baseCookerVolume", baseCookerVolume,
                "The volume for the cooker sound (0.0 is off)").getDouble();
        baseChoppingVolume = (float) cfg.get(CATEGORY_GENERAL, "baseChoppingVolume", baseChoppingVolume,
                "The volume for the chopping sound (0.0 is off)").getDouble();
        baseGrindstoneVolume = (float) cfg.get(CATEGORY_GENERAL, "baseGrindstoneVolume", baseGrindstoneVolume,
                "The volume for the grindstone sound (0.0 is off)").getDouble();

        String overhaul = cfg.get(CATEGORY_GENERAL, "farmlandOverhaulType", GeneralConfiguration.farmlandOverhaulType.getName(), "The type of overhaul for farmland: 'none' means vanilla water will work, 'fresh' means fresh water is required, 'harsh' means fresh water is required and the farmland must be sprinkled").getString();
        farmlandOverhaulType = FarmlandOverhaulType.getByName(overhaul);
        if (farmlandOverhaulType == null) {
            farmlandOverhaulType = FarmlandOverhaulType.FRESH;
        }
    }

    public static void initCookerRecipes(Configuration cfg) {
        ConfigCategory category = cfg.getCategory(CATEGORY_RECIPES_COOKER);
        if (category.isEmpty()) {
            // Initialize with defaults
            addRecipe(cfg, "cookedcarrot", Items.CARROT, ModItems.cookedCarrot, "", 10);
            addRecipe(cfg, "cookedpotato", Items.POTATO, ModItems.cookedPotato, "", 10);
            addRecipe(cfg, "vegetablesoup", ModItems.choppedVegetables, null, ItemDish.DISH_VEGETABLE_SOUP, 10);
        } else {
            for (Map.Entry<String, Property> entry : category.entrySet()) {
                String[] list = entry.getValue().getStringList();
                CookerRecipeRepository.addRecipe(new CookerRecipe(getItem(list, 0), getItem(list, 1), getString(list, 2), getInt(list, 3)));
            }
        }
    }

    public static void initCookingBoardRecipes(Configuration cfg) {
        ConfigCategory category = cfg.getCategory(CATEGORY_RECIPES_CUTTINGBOARD);
        if (category.isEmpty()) {
            // Initialize with defaults
            addRecipe(cfg, "chopped1", Items.CARROT, Items.BEETROOT, Item.getItemFromBlock(Blocks.BROWN_MUSHROOM), ModItems.choppedVegetables, 2, "knife");
            addRecipe(cfg, "chopped2", Items.CARROT, Items.BEETROOT, Item.getItemFromBlock(Blocks.RED_MUSHROOM), ModItems.choppedVegetables, 2, "knife");
            addRecipe(cfg, "dough", ModItems.flour, null, null, ModItems.dough, 10, "roller");
        } else {
            for (Map.Entry<String, Property> entry : category.entrySet()) {
                String[] list = entry.getValue().getStringList();
                boolean roller = "roller".equals(getString(list, 5));
                CuttingBoardRecipeRepository.addRecipe(new CuttingBoardRecipe(getItem(list, 0), getItem(list, 1), getItem(list, 2), getItem(list, 3), getInt(list, 4), roller));
            }
        }
    }

    private static Item getItem(String[] list, int index) {
        if (index >= list.length) {
            return null;
        }
        String reg = list[index];
        if ("-".equals(reg)) {
            return null;
        }
        return getItem(reg);
    }

    private static Item getItem(String reg) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(reg));
    }

    private static int getInt(String[] list, int index) {
        if (index >= list.length) {
            return 0;
        }
        return Integer.parseInt(list[index]);
    }

    private static String getString(String[] list, int index) {
        if (index >= list.length) {
            return "";
        }
        String s = list[index];
        if ("-".equals(s)) {
            return "";
        }
        return s;
    }

    private static void addRecipe(Configuration cfg, String name, Item it1, Item it2, Item it3, Item outi, int chopTime, String tool) {
        cfg.get(CATEGORY_RECIPES_CUTTINGBOARD, name, new String[] { it1 == null ? "-" : it1.getRegistryName().toString(),
                it2 == null ? "-" : it2.getRegistryName().toString(),
                it3 == null ? "-" : it3.getRegistryName().toString(),
                outi.getRegistryName().toString(), Integer.toString(chopTime), tool });
        CuttingBoardRecipeRepository.addRecipe(new CuttingBoardRecipe(it1, it2, it3, outi, chopTime, "roller".equals(tool)));
    }

    private static void addRecipe(Configuration cfg, String name, Item in, Item out, String soup, int cookTime) {
        cfg.get(CATEGORY_RECIPES_COOKER, name, new String[] { in == null ? "-" : in.getRegistryName().toString(),
                out == null ? "-" : out.getRegistryName().toString(), "".equals(soup) ? "-" : soup, Integer.toString(cookTime) });
        CookerRecipeRepository.addRecipe(new CookerRecipe(in, out, soup, cookTime));
    }

}
