package mcjty.aquamunda.compat.jei;

import mcjty.aquamunda.recipes.CookerRecipe;

public class JeiCookerRecipe {

    private final CookerRecipe recipe;

    public JeiCookerRecipe(CookerRecipe recipe) {
        this.recipe = recipe;
    }

    public CookerRecipe getRecipe() {
        return recipe;
    }
}
