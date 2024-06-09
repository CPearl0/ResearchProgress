package com.cpearl.researchprogress.compat.kjs;

import com.cpearl.researchprogress.ResearchProgressHelper;
import net.minecraft.server.level.ServerPlayer;

public class ResearchProgressKubeJSBindings {
    public void addResearchPoints(ServerPlayer player, String research, int point) {
        ResearchProgressHelper.addResearchPoints(player, research, point);
    }
    public void setResearchPoints(ServerPlayer player, String research, int point) {
        ResearchProgressHelper.setResearchPoints(player, research, point);
    }
    public void clearResearch(ServerPlayer player) {
        ResearchProgressHelper.clearResearch(player);
    }
    public int getResearchPoints(ServerPlayer player, String research) {
        return ResearchProgressHelper.getResearchPoints(player, research);
    }
}
