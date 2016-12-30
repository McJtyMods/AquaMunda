package mcjty.aquamunda.recipes;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Comparator;

public class CuttingBoardRecipe {

    private final ItemStack[] inputItems;
    private final ItemStack outputItem;
    private final int chopTime;
    private final boolean useRoller;

    public CuttingBoardRecipe(Item inputItem1, Item inputItem2, Item inputItem3, Item outputItem, int chopTime, boolean useRoller) {
        inputItems = new ItemStack[3];
        inputItems[0] = new ItemStack(inputItem1);
        inputItems[1] = new ItemStack(inputItem2);
        inputItems[2] = new ItemStack(inputItem3);
        sortItems(inputItems);

        this.outputItem = outputItem == null ? ItemStackTools.getEmptyStack() : new ItemStack(outputItem);
        this.chopTime = chopTime;
        this.useRoller = useRoller;
    }

    public static void sortItems(ItemStack[] stacks) {
        Arrays.sort(stacks, new Comparator<ItemStack>() {
            @Override
            public int compare(ItemStack i1, ItemStack i2) {
                if (ItemStackTools.isValid(i1) && ItemStackTools.isValid(i2)) {
                    return i1.getItem().getRegistryName().toString().compareTo(i2.getItem().getRegistryName().toString());
                }
                if (ItemStackTools.isValid(i1)) {
                    return 1;
                }
                if (ItemStackTools.isValid(i2)) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public ItemStack[] getInputItems() {
        return inputItems;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getChopTime() {
        return chopTime;
    }

    public boolean isUseRoller() {
        return useRoller;
    }
}