package com.cpearl.researchprogress.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ResearchesSyncEvent extends PlayerEvent {
    private final Map<String, Integer> researches;
    public ResearchesSyncEvent(Map<String, Integer> researches, Player player) {
        super(player);
        this.researches = researches;
    }
    public ResearchesSyncEvent(Map<String, Integer> researches) {
        this(researches, Minecraft.getInstance().player);
    }

    public Map<String, Integer> getResearches() {
        return researches;
    }
}
