package mcjty.aquamunda.compat.immcraft;

import mcjty.aquamunda.blocks.tank.Tank;
import mcjty.aquamunda.blocks.tank.TankClientInfo;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import mcjty.immcraft.api.multiblock.IMultiBlockFactory;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import net.minecraft.util.EnumFacing;

public class ImmersiveCraftHandler {

    public static final String AQUA_MUNDA_TANKS = "AquaMundaTanks";
    public static final String AQUA_MUNDA_HOSES = "AquaMundaHoses";

    public static IImmersiveCraft immersiveCraft;

    public static ICableType liquidType = new LiquidCableType();
    public static ICableSubType liquidSubtype = new LiquidCableSubType();

    public static IMultiBlockNetwork<Tank> tankNetwork;
    public static IMultiBlockNetwork hoseNetwork;

    public static void setImmersiveCraft(IImmersiveCraft immersiveCraft) {
        ImmersiveCraftHandler.immersiveCraft = immersiveCraft;

        immersiveCraft.registerCableType(liquidType);
        tankNetwork = immersiveCraft.createMultiBlockNetwork(AQUA_MUNDA_TANKS, new IMultiBlockFactory<Tank>() {
            @Override
            public Tank create() {
                return new Tank();
            }

            @Override
            public boolean isSameType(IMultiBlock mb) {
                return mb instanceof Tank;
            }

            @Override
            public IMultiBlockClientInfo createClientInfo() {
                return new TankClientInfo(0, 0, null);
            }
        }, EnumFacing.HORIZONTALS);

        hoseNetwork = ImmersiveCraftHandler.immersiveCraft.createCableNetwork(AQUA_MUNDA_HOSES, ImmersiveCraftHandler.liquidType, ImmersiveCraftHandler.liquidSubtype);
    }
}
