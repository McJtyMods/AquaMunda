package mcjty.aquamunda.recipes;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CuttingBoardRecipeRepository {

    private static Map<KeyResourceLocations, CuttingBoardRecipe> recipeMap = new HashMap<>();

    public static void addRecipe(CuttingBoardRecipe recipe) {
        KeyResourceLocations key = new KeyResourceLocations(recipe.getInputItems());
        recipeMap.put(key, recipe);
    }

    @Nullable
    public static CuttingBoardRecipe getRecipe(ItemStack stack1, ItemStack stack2, ItemStack stack3) {
        if (stack1.isEmpty() && stack2.isEmpty() && stack3.isEmpty()) {
            return null;
        }
        ItemStack[] items = new ItemStack[] { stack1, stack2, stack3 };
        CuttingBoardRecipe.sortItems(items);
        KeyResourceLocations key = new KeyResourceLocations(items);
        if (recipeMap.containsKey(key)) {
            CuttingBoardRecipe recipe = recipeMap.get(key);
            for (int i = 0 ; i < items.length ; i++) {
                ItemStack item = items[i];
                if (!item.isEmpty()) {
                    if (!ItemStack.areItemStackTagsEqual(recipe.getInputItems()[i], item)) {
                        return null;
                    }
                }
            }
            return recipe;
        }
        return null;
    }
}
