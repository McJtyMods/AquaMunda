package mcjty.aquamunda.recipes;

import mcjty.aquamunda.items.ItemDish;
import mcjty.aquamunda.items.ModItems;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookerRecipeRepository {

    private static CookerRecipe[] recipes = new CookerRecipe[] {
            new CookerRecipe(Items.CARROT, ModItems.cookedCarrot, "", 10),
            new CookerRecipe(ModItems.choppedVegetables, null, ItemDish.DISH_VEGETABLE_SOUP, 10),
    };
    private static Map<ResourceLocation, List<CookerRecipe>> recipeMap = null;

    private static void setupRecipeMap() {
        if (recipeMap == null) {
            recipeMap = new HashMap<>();
            for (CookerRecipe recipe : recipes) {
                ResourceLocation key = recipe.getInputItem().getItem().getRegistryName();
                if (!recipeMap.containsKey(key)) {
                    recipeMap.put(key, new ArrayList<>());
                }
                recipeMap.get(key).add(recipe);
            }
        }
    }

    @Nullable
    public static CookerRecipe getRecipe(ItemStack stack) {
        if (ItemStackTools.isEmpty(stack)) {
            return null;
        }
        setupRecipeMap();
        ResourceLocation key = stack.getItem().getRegistryName();
        if (recipeMap.containsKey(key)) {
            List<CookerRecipe> recipes = recipeMap.get(key);
            for (CookerRecipe recipe : recipes) {
                if (ItemStack.areItemStackTagsEqual(recipe.getInputItem(), stack)) {
                    return recipe;
                }
            }

        }
        return null;
    }
}
