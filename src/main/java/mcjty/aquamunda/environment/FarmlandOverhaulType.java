package mcjty.aquamunda.environment;

import java.util.HashMap;
import java.util.Map;

public enum FarmlandOverhaulType {
    NONE("none"),                   // Farmland works with vanilla water
    FRESH("fresh"),                 // Farmland needs fresh water
    HARSH("harsh");                 // Farmland needs fresh water and must be sprinkled

    private final String name;

    private final static Map<String, FarmlandOverhaulType> TYPE_MAP = new HashMap<>();

    static {
        for (FarmlandOverhaulType type : FarmlandOverhaulType.values()) {
            TYPE_MAP.put(type.getName(), type);
        }
    }

    FarmlandOverhaulType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FarmlandOverhaulType getByName(String name) {
        return TYPE_MAP.get(name);
    }
}
