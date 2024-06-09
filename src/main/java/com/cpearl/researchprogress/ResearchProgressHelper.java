package com.cpearl.researchprogress;

import com.cpearl.researchprogress.capability.ResearchCapabilityProvider;
import com.cpearl.researchprogress.events.ResearchesUpdateEvent;
import com.cpearl.researchprogress.network.ResearchMessage;
import com.cpearl.researchprogress.network.ResearchPacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResearchProgressHelper {
    public static Map<String, Integer> getResearches(ServerPlayer player) {
        final Map<String, Integer>[] ret = (Map<String, Integer>[]) new Map<?, ?>[1];
        player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(researchCapability -> {
            ret[0] = new HashMap<>(researchCapability.getResearches());
        });
        return ret[0];
    }

    public static void addResearchPoints(ServerPlayer player, String research, int point) {
        player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(researchCapability -> {
            var researchesOld = new HashMap<>(researchCapability.getResearches());
            researchCapability.addResearchPoints(research, point);
            var researchesNew = new HashMap<>(researchCapability.getResearches());

            if (MinecraftForge.EVENT_BUS.post(new ResearchesUpdateEvent(researchesOld, researchesNew, player)))
                researchCapability.addResearchPoints(research, -point);
            else
                syncPlayer(player);
        });
    }

    public static void setResearchPoints(ServerPlayer player, String research, int point) {
        player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(researchCapability -> {
            var researchesOld = new HashMap<>(researchCapability.getResearches());
            researchCapability.addResearchPoints(research, point);
            var researchesNew = new HashMap<>(researchCapability.getResearches());

            if (MinecraftForge.EVENT_BUS.post(new ResearchesUpdateEvent(researchesOld, researchesNew, player)))
                researchCapability.addResearchPoints(research, -point);
            else
                syncPlayer(player);
        });
    }

    public static void clearResearch(ServerPlayer player) {
        player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(researchCapability -> {
            var researchesOld = new HashMap<>(researchCapability.getResearches());
            if (researchesOld.isEmpty())
                return;
            researchCapability.clearResearch();
            var researchesNew = new HashMap<>(researchCapability.getResearches());
            if (MinecraftForge.EVENT_BUS.post(new ResearchesUpdateEvent(researchesOld, researchesNew, player)))
                researchCapability.setResearches(researchesOld);
            else
                syncPlayer(player);
        });
    }

    public static int getResearchPoints(ServerPlayer player, String research) {
        final int[] res = new int[1];
        player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(researchCapability -> {
            res[0] = researchCapability.getResearchPoints(research);
        });
        return res[0];
    }

    public static void syncPlayer(ServerPlayer player) {
        player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(researchCapability -> {
            ResearchMessage msg = new ResearchMessage(researchCapability.getResearches(), ResearchMessage.SYNC);
            ResearchProgress.LOGGER.info("Begin syncing player " + player.getDisplayName());
            ResearchPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
        });
    }
}
