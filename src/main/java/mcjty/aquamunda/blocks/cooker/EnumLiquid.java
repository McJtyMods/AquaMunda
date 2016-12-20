package mcjty.aquamunda.blocks.cooker;

import net.minecraft.util.IStringSerializable;

public enum EnumLiquid implements IStringSerializable {
    WATER("water"),
    SOUP("soup");

    private final String name;

    EnumLiquid(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
