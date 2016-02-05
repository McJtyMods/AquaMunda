package mcjty.aquamunda.network;


import mcjty.aquamunda.blocks.desalination.BoilerContentsInfoPacketClient;
import mcjty.aquamunda.blocks.desalination.BoilerContentsInfoPacketServer;
import mcjty.aquamunda.blocks.desalination.TankContentsInfoPacketClient;
import mcjty.aquamunda.blocks.desalination.TankContentsInfoPacketServer;
import mcjty.aquamunda.blocks.tank.TankInfoPacketClient;
import mcjty.aquamunda.blocks.tank.TankInfoPacketServer;
import mcjty.aquamunda.hosemultiblock.HoseInfoPacketClient;
import mcjty.aquamunda.hosemultiblock.HoseInfoPacketServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

public class PacketHandler {
    private static int ID = 12;
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    private static Map<Integer,Class<? extends InfoPacketClient>> clientInfoPackets = new HashMap<>();
    private static Map<Integer,Class<? extends InfoPacketServer>> serverInfoPackets = new HashMap<>();
    private static Map<Class<? extends InfoPacketClient>,Integer> clientInfoPacketsToId = new HashMap<>();
    private static Map<Class<? extends InfoPacketServer>,Integer> serverInfoPacketsToId = new HashMap<>();

    public static int nextPacketID() {
        return packetId++;
    }

    private static void register(Integer id, Class<? extends InfoPacketServer> serverClass, Class<? extends InfoPacketClient> clientClass) {
        serverInfoPackets.put(id, serverClass);
        clientInfoPackets.put(id, clientClass);
        serverInfoPacketsToId.put(serverClass, id);
        clientInfoPacketsToId.put(clientClass, id);
    }

    public static Class<? extends InfoPacketServer> getServerInfoPacket(int id) {
        return serverInfoPackets.get(id);
    }

    public static Integer getServerInfoPacketId(Class<? extends InfoPacketServer> clazz) {
        return serverInfoPacketsToId.get(clazz);
    }

    public static Class<? extends InfoPacketClient> getClientInfoPacket(int id) {
        return clientInfoPackets.get(id);
    }

    public static Integer getClientInfoPacketId(Class<? extends InfoPacketClient> clazz) {
        return clientInfoPacketsToId.get(clazz);
    }

    public PacketHandler() {
    }

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() {
        // Server side
        INSTANCE.registerMessage(PacketGetInfoFromServer.Handler.class, PacketGetInfoFromServer.class, nextID(), Side.SERVER);

        // Client side
        INSTANCE.registerMessage(PacketReturnInfoHandler.class, PacketReturnInfoToClient.class, nextID(), Side.CLIENT);

        register(nextPacketID(), TankInfoPacketServer.class, TankInfoPacketClient.class);
        register(nextPacketID(), HoseInfoPacketServer.class, HoseInfoPacketClient.class);
        register(nextPacketID(), BoilerContentsInfoPacketServer.class, BoilerContentsInfoPacketClient.class);
        register(nextPacketID(), TankContentsInfoPacketServer.class, TankContentsInfoPacketClient.class);
    }
}
