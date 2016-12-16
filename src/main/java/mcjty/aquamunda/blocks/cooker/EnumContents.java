package mcjty.aquamunda.blocks.cooker;

import net.minecraft.util.IStringSerializable;

public enum EnumContents implements IStringSerializable {
    EMPTY("empty"),
    LOW("low"),
    MEDIUM("medium"),
    FULL("full");

    private final String name;

    EnumContents(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
