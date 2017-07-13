package mcjty.aquamunda.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CookerRecipeRepository {

    private static Map<ResourceLocation, CookerRecipe> recipeMap = new HashMap<>();

    public static void addRecipe(CookerRecipe recipe) {
        ResourceLocation key = recipe.getInputItem().getItem().getRegistryName();
        recipeMap.put(key, recipe);
    }

    @Nullable
    public static CookerRecipe getRecipe(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        ResourceLocation key = stack.getItem().getRegistryName();
        if (recipeMap.containsKey(key)) {
            CookerRecipe recipe = recipeMap.get(key);
            if (ItemStack.areItemStackTagsEqual(recipe.getInputItem(), stack)) {
                return recipe;
            }

        }
        return null;
    }
}
