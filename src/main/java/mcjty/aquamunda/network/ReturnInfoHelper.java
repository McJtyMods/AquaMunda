package mcjty.aquamunda.network;


import mcjty.aquamunda.AquaMunda;

public class ReturnInfoHelper {
    public static void onMessageFromServer(PacketReturnInfoToClient message) {
        AquaMunda.proxy.addScheduledTaskClient(() -> message.getPacket().onMessageClient(AquaMunda.proxy.getClientPlayer()));
    }
}
