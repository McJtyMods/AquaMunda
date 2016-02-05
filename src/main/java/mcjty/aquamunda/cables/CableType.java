package mcjty.aquamunda.cables;

import java.util.HashMap;
import java.util.Map;

public enum CableType {
    LIQUID("liq", new LiquidCableHandler());
//    ELECTRICITY("ele", new ElectricCableHandler());

    private final String typeId;
    private final ICableHandler cableHandler;

    private static final Map<String,CableType> TYPE_MAP = new HashMap<>();

    static {
        for (CableType type : values()) {
            TYPE_MAP.put(type.getTypeId(), type);
        }
    }

    CableType(String typeId, ICableHandler cableHandler) {
        this.typeId = typeId;
        this.cableHandler = cableHandler;
    }

    public String getTypeId() {
        return typeId;
    }

    public ICableHandler getCableHandler() {
        return cableHandler;
    }

    public static CableType getTypeById(String id) {
        return TYPE_MAP.get(id);
    }
}
