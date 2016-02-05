package mcjty.aquamunda.hosemultiblock;

import mcjty.aquamunda.cables.Cable;
import mcjty.aquamunda.cables.CableSubType;
import mcjty.aquamunda.cables.CableType;
import mcjty.aquamunda.multiblock.IMultiBlock;
import mcjty.aquamunda.multiblock.IMultiBlockFactory;
import mcjty.aquamunda.multiblock.MultiBlockNetwork;
import mcjty.aquamunda.network.PacketGetInfoFromServer;
import mcjty.aquamunda.network.PacketHandler;

import java.util.HashMap;
import java.util.Map;

public class HoseMultiBlockNetwork extends MultiBlockNetwork<Cable> {

    public HoseMultiBlockNetwork() {
        super(new IMultiBlockFactory<Cable>() {
            @Override
            public Cable create() {
                return new Cable(CableType.LIQUID, CableSubType.LIQUID);
            }

            @Override
            public boolean isSameType(IMultiBlock mb) {
                return mb instanceof Cable && ((Cable)mb).getType() == CableType.LIQUID;
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
            PacketHandler.INSTANCE.sendToServer(new PacketGetInfoFromServer(new HoseInfoPacketServer(id)));
        }
    }

}
