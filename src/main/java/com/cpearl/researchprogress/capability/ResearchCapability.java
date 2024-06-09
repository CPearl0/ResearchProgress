package com.cpearl.researchprogress.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.*;

@AutoRegisterCapability
public class ResearchCapability implements IResearchCapability {
    private Map<String, Integer> researches = new TreeMap<>();
    @Override
    public void addResearchPoints(String research, int point) {
        researches.put(research, researches.getOrDefault(research, 0) + point);
    }

    @Override
    public void setResearchPoints(String research, int point) {
        researches.put(research, point);
    }

    @Override
    public void clearResearch() {
        researches.clear();
    }

    @Override
    public int getResearchPoints(String research) {
        return researches.getOrDefault(research, 0);
    }
    @Override
    public Map<String, Integer> getResearches() {
        return researches;
    }
    @Override
    public void setResearches(Map<String, Integer> researches) {
        this.researches = new TreeMap<>(researches);
    }

    @Override
    public ResearchCapability clone() throws CloneNotSupportedException {
        ResearchCapability clone = (ResearchCapability) super.clone();
        clone.setResearches(this.researches);
        return clone;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listPhases = new ListTag();
        for (var research: researches.entrySet()) {
            CompoundTag researchTag = new CompoundTag();
            researchTag.put("Research", StringTag.valueOf(research.getKey()));
            researchTag.putInt("Points", research.getValue());
            listPhases.add(researchTag);
        }
        tag.put("Researches", listPhases);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        researches = new TreeMap<>();
        var listResearches = nbt.getList("Researches", Tag.TAG_COMPOUND);
        for (int i = 0; i < listResearches.size(); i++) {
            var researchTag = listResearches.getCompound(i);
            addResearchPoints(researchTag.getString("Research"), researchTag.getInt("Points"));
        }
    }
}
