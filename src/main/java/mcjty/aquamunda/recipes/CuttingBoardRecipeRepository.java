package mcjty.aquamunda.recipes;

import mcjty.aquamunda.items.ModItems;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuttingBoardRecipeRepository {

    private static CuttingBoardRecipe[] recipes = new CuttingBoardRecipe[] {
            new CuttingBoardRecipe(Items.CARROT, Items.BEETROOT, Item.getItemFromBlock(Blocks.BROWN_MUSHROOM), ModItems.choppedVegetables, 2, false),
            new CuttingBoardRecipe(ModItems.flour, null, null, ModItems.dough, 10, true)
    };
    private static Map<KeyResourceLocations, List<CuttingBoardRecipe>> recipeMap = null;

    private static void setupRecipeMap() {
        if (recipeMap == null) {
            recipeMap = new HashMap<>();
            for (CuttingBoardRecipe recipe : recipes) {
                KeyResourceLocations key = new KeyResourceLocations(recipe.getInputItems());
                if (!recipeMap.containsKey(key)) {
                    recipeMap.put(key, new ArrayList<>());
                }
                recipeMap.get(key).add(recipe);
            }
        }
    }

    @Nullable
    public static CuttingBoardRecipe getRecipe(ItemStack stack1, ItemStack stack2, ItemStack stack3) {
        if (ItemStackTools.isEmpty(stack1) && ItemStackTools.isEmpty(stack2) && ItemStackTools.isEmpty(stack3)) {
            return null;
        }
        setupRecipeMap();
        ItemStack[] items = new ItemStack[] { stack1, stack2, stack3 };
        CuttingBoardRecipe.sortItems(items);
        KeyResourceLocations key = new KeyResourceLocations(items);
        if (recipeMap.containsKey(key)) {
            List<CuttingBoardRecipe> recipes = recipeMap.get(key);
            for (CuttingBoardRecipe recipe : recipes) {
                boolean ok = true;
                for (int i = 0 ; i < items.length ; i++) {
                    ItemStack item = items[i];
                    if (ItemStackTools.isValid(item)) {
                        if (!ItemStack.areItemStackTagsEqual(recipe.getInputItems()[i], item)) {
                            ok = false;
                        }
                    }
                }
                if (ok) {
                    return recipe;
                }
            }
        }
        return null;
    }
}
