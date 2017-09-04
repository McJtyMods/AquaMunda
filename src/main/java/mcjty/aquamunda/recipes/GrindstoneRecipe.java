package mcjty.aquamunda.recipes;

import net.minecraft.item.ItemStack;

public class GrindstoneRecipe {

    private final ItemStack inputItem;
    private final ItemStack outputItem;
    private final int grindTime;

    public GrindstoneRecipe(ItemStack inputItem, ItemStack outputItem, int grindTime) {
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.grindTime = grindTime;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getGrindTime() {
        return grindTime;
    }
}
