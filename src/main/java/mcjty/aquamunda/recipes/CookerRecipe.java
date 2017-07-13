package mcjty.aquamunda.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CookerRecipe {

    private final ItemStack inputItem;
    private final ItemStack outputItem;
    private final String outputSoup;
    private final int cookTime;

    public CookerRecipe(Item inputItem, Item outputItem, @Nonnull String outputSoup, int cookTime) {
        this.inputItem = new ItemStack(inputItem);
        this.outputItem = outputItem == null ? ItemStack.EMPTY : new ItemStack(outputItem);
        this.outputSoup = outputSoup;
        this.cookTime = cookTime;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Nonnull
    public String getOutputSoup() {
        return outputSoup;
    }
}
