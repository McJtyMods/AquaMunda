package mcjty.aquamunda.compat.jei;

import mcjty.aquamunda.recipes.GrindstoneRecipe;

public class JeiGrindstoneRecipe {

    private final GrindstoneRecipe recipe;

    public JeiGrindstoneRecipe(GrindstoneRecipe recipe) {
        this.recipe = recipe;
    }

    public GrindstoneRecipe getRecipe() {
        return recipe;
    }
}
