package mcjty.aquamunda.immcraft;

import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;

public class ImmersiveCraftHandler {

    public static IImmersiveCraft immersiveCraft;

    public static ICableType liquidType = new LiquidCableType();
    public static ICableSubType liquidSubtype = new LiquidCableSubType();

    public static void setImmersiveCraft(IImmersiveCraft immersiveCraft) {
        ImmersiveCraftHandler.immersiveCraft = immersiveCraft;

        immersiveCraft.registerCableType(liquidType);
    }
}
