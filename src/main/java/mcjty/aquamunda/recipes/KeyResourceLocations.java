package mcjty.aquamunda.recipes;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class KeyResourceLocations {
    private final String[] r;

    public KeyResourceLocations(ItemStack[] items) {
        r = new String[items.length];
        for (int i = 0 ; i < items.length ; i++) {
            ItemStack item = items[i];
            if (ItemStackTools.isValid(item)) {
                r[i] = item.getItem().getRegistryName().toString();
            } else {
                r[i] = "";
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyResourceLocations that = (KeyResourceLocations) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(r, that.r)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(r);
    }
}
