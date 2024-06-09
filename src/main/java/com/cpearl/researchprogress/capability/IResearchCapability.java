package com.cpearl.researchprogress.capability;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.Map;

@AutoRegisterCapability
public interface IResearchCapability {
    void addResearchPoints(String research, int point);
    void setResearchPoints(String research, int point);
    void clearResearch();
    int getResearchPoints(String research);
    Map<String, Integer> getResearches();
    void setResearches(Map<String, Integer> researches);
}
