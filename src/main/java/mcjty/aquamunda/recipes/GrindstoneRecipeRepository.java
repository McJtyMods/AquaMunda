package mcjty.aquamunda.recipes;

import mcjty.lib.tools.ItemStackTools;
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

    @Nullable
    public static GrindstoneRecipe getRecipe(ItemStack stack) {
        if (ItemStackTools.isEmpty(stack)) {
            return null;
        }
        ResourceLocation key = stack.getItem().getRegistryName();
        if (recipeMap.containsKey(key)) {
            GrindstoneRecipe recipe = recipeMap.get(key);
            if (ItemStack.areItemStackTagsEqual(recipe.getInputItem(), stack)) {
                return recipe;
            }

        }
        return null;
    }
}
