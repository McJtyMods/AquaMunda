package mcjty.aquamunda.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class CuttingBoardRecipe {

    private final ItemStack[] inputItems;
    private final ItemStack outputItem;
    private final int chopTime;
    private final boolean useRoller;

    public CuttingBoardRecipe(Item inputItem1, Item inputItem2, Item inputItem3, Item outputItem, int chopTime, boolean useRoller) {
        inputItems = new ItemStack[3];
        inputItems[0] = inputItem1 == null ? ItemStack.EMPTY : new ItemStack(inputItem1);
        inputItems[1] = inputItem2 == null ? ItemStack.EMPTY : new ItemStack(inputItem2);
        inputItems[2] = inputItem3 == null ? ItemStack.EMPTY : new ItemStack(inputItem3);
        sortItems(inputItems);

        this.outputItem = outputItem == null ? ItemStack.EMPTY : new ItemStack(outputItem);
        this.chopTime = chopTime;
        this.useRoller = useRoller;
    }

    private static String getRegNameSafe(ItemStack s) {
        if (s.getItem() == null) {
            return "";
        }
        ResourceLocation r = s.getItem().getRegistryName();
        if (r == null) {
            return "";
        }
        return r.toString();
    }

    public static void sortItems(ItemStack[] stacks) {
        Arrays.sort(stacks, (i1, i2) -> {
            if (!i1.isEmpty() && !i2.isEmpty()) {
                return getRegNameSafe(i1).compareTo(getRegNameSafe(i2));
            }
            if (!i1.isEmpty()) {
                return 1;
            }
            if (!i2.isEmpty()) {
                return -1;
            }
            return 0;
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