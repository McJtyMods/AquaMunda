package mcjty.aquamunda.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

class JeiGrindstoneRecipeWrapper implements ICraftingRecipeWrapper {

    private final JeiGrindstoneRecipe recipe;

    public JeiGrindstoneRecipeWrapper(JeiGrindstoneRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipe().getOutputItem());
        ingredients.setInput(VanillaTypes.ITEM, recipe.getRecipe().getInputItem());
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }
}
