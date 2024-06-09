package com.cpearl.researchprogress.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.*;
import java.util.function.Supplier;

public class ResearchMessage {
    public static final int ID = 0;
    public static final int INIT = 0;
    public static final int SYNC = 1;
    private final Map<String, Integer> researches;
    private final int type;
    public ResearchMessage(Map<String, Integer> phases, int type) {
        this.researches = new TreeMap<>(phases);
        this.type = type;
    }

    public Map<String, Integer> getResearches() {
        return researches;
    }

    public int getType() {
        return type;
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Type", type);
        ListTag listPhases = new ListTag();
        for (var research: researches.entrySet()) {
            CompoundTag researchTag = new CompoundTag();
            researchTag.put("Research", StringTag.valueOf(research.getKey()));
            researchTag.putInt("Points", research.getValue());
            listPhases.add(researchTag);
        }
        tag.put("Researches", listPhases);
        buf.writeNbt(tag);
    }

    public static ResearchMessage decode(FriendlyByteBuf buf) {
        CompoundTag nbt = buf.readNbt();
        assert nbt != null;
        var type = nbt.getInt("Type");
        var listResearches = nbt.getList("Researches", Tag.TAG_COMPOUND);
        Map<String, Integer> researches = new TreeMap<>();
        for (int i = 0; i < listResearches.size(); i++) {
            var researchTag = listResearches.getCompound(i);
            researches.put(researchTag.getString("Research"), researchTag.getInt("Points"));
        }
        return new ResearchMessage(researches, type);
    }

    public void process(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPacketHandler.handlePacket(this, context);
            });
        });
        context.get().setPacketHandled(true);
    }
}
