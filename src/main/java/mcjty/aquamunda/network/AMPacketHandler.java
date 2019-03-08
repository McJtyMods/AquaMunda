package mcjty.aquamunda.network;


import mcjty.aquamunda.AquaMunda;
import mcjty.aquamunda.blocks.customblocks.FarmLandMoistnessPacketClient;
import mcjty.aquamunda.blocks.customblocks.FarmLandMoistnessPacketServer;
import mcjty.aquamunda.blocks.desalination.BoilerContentsInfoPacketClient;
import mcjty.aquamunda.blocks.desalination.BoilerContentsInfoPacketServer;
import mcjty.aquamunda.blocks.desalination.TankContentsInfoPacketClient;
import mcjty.aquamunda.blocks.desalination.TankContentsInfoPacketServer;
import mcjty.lib.network.PacketHandler;
import mcjty.lib.thirteen.ChannelBuilder;
import mcjty.lib.thirteen.SimpleChannel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.HashMap;
import java.util.Map;

public class AMPacketHandler {
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    private static Map<Integer,Class<? extends InfoPacketClient>> clientInfoPackets = new HashMap<>();
    private static Map<Integer,Class<? extends InfoPacketServer>> serverInfoPackets = new HashMap<>();
    private static Map<Class<? extends InfoPacketClient>,Integer> clientInfoPacketsToId = new HashMap<>();
    private static Map<Class<? extends InfoPacketServer>,Integer> serverInfoPacketsToId = new HashMap<>();

    public static int infoId() {
        return packetId++;
    }

    private static int id() {
        return PacketHandler.nextPacketID();
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

    public AMPacketHandler() {
    }

    public static void registerMessages(String name) {
        SimpleChannel net = ChannelBuilder
                .named(new ResourceLocation(AquaMunda.MODID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net.getNetwork();

        // Server side
        net.registerMessageServer(id(), PacketGetInfoFromServer.class, PacketGetInfoFromServer::toBytes, PacketGetInfoFromServer::new, PacketGetInfoFromServer::handle);

        // Client side
        net.registerMessageClient(id(), PacketReturnInfoToClient.class, PacketReturnInfoToClient::toBytes, PacketReturnInfoToClient::new, PacketReturnInfoToClient::handle);

        register(infoId(), BoilerContentsInfoPacketServer.class, BoilerContentsInfoPacketClient.class);
        register(infoId(), TankContentsInfoPacketServer.class, TankContentsInfoPacketClient.class);
        register(infoId(), FarmLandMoistnessPacketServer.class, FarmLandMoistnessPacketClient.class);
    }

}
