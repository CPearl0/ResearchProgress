package com.cpearl.researchprogress.network;

import com.cpearl.researchprogress.ResearchProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ResearchPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ResearchProgress.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(ResearchMessage.ID, ResearchMessage.class,
                ResearchMessage::encode, ResearchMessage::decode, ResearchMessage::process);
    }
}
