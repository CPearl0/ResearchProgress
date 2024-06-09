package com.cpearl.researchprogress.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.Map;

@Cancelable
public class ResearchesUpdateEvent extends PlayerEvent {
    private final Map<String, Integer> researchesOld, researchesNew;
    public ResearchesUpdateEvent(Map<String, Integer> phasesOld, Map<String, Integer> phasesNew, Player player) {
        super(player);
        this.researchesOld = phasesOld;
        this.researchesNew = phasesNew;
    }

    public Map<String, Integer> getResearchesOld() {
        return researchesOld;
    }

    public Map<String, Integer> getResearchesNew() {
        return researchesNew;
    }
}
