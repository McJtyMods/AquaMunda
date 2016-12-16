package mcjty.aquamunda.blocks.cooker;

import net.minecraft.util.IStringSerializable;

public enum EnumBoiling implements IStringSerializable {
    COLD("cold"),
    HOT("hot"),
    BOILING("boiling");

    private final String name;

    EnumBoiling(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
