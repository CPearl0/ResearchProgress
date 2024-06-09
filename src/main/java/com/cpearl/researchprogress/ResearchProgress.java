package com.cpearl.researchprogress;

import com.cpearl.researchprogress.network.ResearchPacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ResearchProgress.MODID)
public class ResearchProgress
{
    public static final String MODID = "researchprogress";
    public static final Logger LOGGER = LogUtils.getLogger();

    static {
        ResearchPacketHandler.init();
    }
}
