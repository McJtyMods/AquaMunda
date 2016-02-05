package mcjty.aquamunda.blocks.tank;

import mcjty.aquamunda.multiblock.IMultiBlock;
import mcjty.aquamunda.multiblock.IMultiBlockFactory;
import mcjty.aquamunda.multiblock.MultiBlockNetwork;
import mcjty.aquamunda.network.PacketGetInfoFromServer;
import mcjty.aquamunda.network.PacketHandler;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class TankMultiBlockNetwork extends MultiBlockNetwork<Tank> {

    public static final EnumFacing[] DIRECTIONS = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST };

    public TankMultiBlockNetwork() {
        super(new IMultiBlockFactory<Tank>() {
            @Override
            public Tank create() {
                return new Tank();
            }

            @Override
            public boolean isSameType(IMultiBlock mb) {
                return mb instanceof Tank;
            }
        });
    }

    // Client-side only. How long ago the tank data was updated
    private final Map<Integer,Long> lastUpdateTime = new HashMap<>();

    // This should only be used client-side!
    @Override
    public void refreshInfo(int id) {
        long time = System.currentTimeMillis();
        if ((!lastUpdateTime.containsKey(id)) || (time - lastUpdateTime.get(id)) > 100) {
            lastUpdateTime.put(id, time);
            PacketHandler.INSTANCE.sendToServer(new PacketGetInfoFromServer(new TankInfoPacketServer(id)));
        }
    }

    @Override
    public EnumFacing[] getDirections() {
        return DIRECTIONS;
    }
}
