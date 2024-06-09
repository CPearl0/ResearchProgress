package com.cpearl.researchprogress.network;

import com.cpearl.researchprogress.events.ResearchesInitializeEvent;
import com.cpearl.researchprogress.events.ResearchesSyncEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handlePacket(ResearchMessage msg, Supplier<NetworkEvent.Context> context) {
        switch (msg.getType()) {
            case ResearchMessage.INIT -> MinecraftForge.EVENT_BUS.post(new ResearchesInitializeEvent(msg.getResearches()));
            case ResearchMessage.SYNC -> MinecraftForge.EVENT_BUS.post(new ResearchesSyncEvent(msg.getResearches()));
        }
    }
}
