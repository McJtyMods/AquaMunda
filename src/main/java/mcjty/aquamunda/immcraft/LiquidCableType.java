package mcjty.aquamunda.immcraft;

import mcjty.immcraft.api.cable.ICableHandler;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;

public class LiquidCableType implements ICableType {

    private final LiquidCableHandler cableHandler = new LiquidCableHandler();

    @Override
    public ICableHandler getCableHandler() {
        return cableHandler;
    }

    @Override
    public String getTypeID() {
        return "liquid";
    }

    @Override
    public String getReadableName() {
        return "liquid";
    }

    @Override
    public ICableSubType getSubTypeByID(String id) {
        if ("liquid".equals(id)) {
            return ImmersiveCraftHandler.liquidSubtype;
        } else {
            return null;
        }
    }
}
