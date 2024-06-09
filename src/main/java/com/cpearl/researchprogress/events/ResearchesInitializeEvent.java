package com.cpearl.researchprogress.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.Map;

public class ResearchesInitializeEvent extends PlayerEvent {
    private final Map<String, Integer> researches;
    public ResearchesInitializeEvent(Map<String, Integer> researches, Player player) {
        super(player);
        this.researches = researches;
    }
    public ResearchesInitializeEvent(Map<String, Integer> researches) {
        this(researches, Minecraft.getInstance().player);
    }

    public Map<String, Integer> getResearches() {
        return researches;
    }
}
