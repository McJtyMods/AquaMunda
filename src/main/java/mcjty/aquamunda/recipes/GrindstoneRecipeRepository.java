package mcjty.aquamunda.recipes;

import mcjty.immcraft.api.helpers.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class GrindstoneRecipeRepository {

    private static Map<ResourceLocation, GrindstoneRecipe> recipeMap = new HashMap<>();

    public static void addRecipe(GrindstoneRecipe recipe) {
        ResourceLocation key = recipe.getInputItem().getItem().getRegistryName();
        recipeMap.put(key, recipe);
    }

    public static Map<ResourceLocation, GrindstoneRecipe> getRecipeMap() {
        return recipeMap;
    }

    @Nullable
    public static GrindstoneRecipe getRecipe(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        ResourceLocation key = stack.getItem().getRegistryName();
        if (recipeMap.containsKey(key)) {
            GrindstoneRecipe recipe = recipeMap.get(key);
            if (InventoryHelper.isItemStackConsideredEqual(recipe.getInputItem(), stack)) {
                return recipe;
            }

        }
        return null;
    }
}
