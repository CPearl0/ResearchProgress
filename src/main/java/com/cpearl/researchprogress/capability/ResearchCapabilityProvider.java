package com.cpearl.researchprogress.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = "gamephase")
public class ResearchCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    public static Capability<IResearchCapability> RESEARCH = CapabilityManager.get(new CapabilityToken<IResearchCapability>() {});
    private IResearchCapability researchCapability = null;
    private final LazyOptional<IResearchCapability> researchCapabilityLazyOptional = LazyOptional.of(this::createResearchCapability);
    private IResearchCapability createResearchCapability() {
        if (researchCapability == null)
            researchCapability = new ResearchCapability();
        return researchCapability;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return RESEARCH.orEmpty(cap, researchCapabilityLazyOptional);
    }

    @Override
    public CompoundTag serializeNBT() {
        var capability = (ResearchCapability) createResearchCapability();
        return capability.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        var capability = (ResearchCapability) createResearchCapability();
        capability.deserializeNBT(nbt);
    }
}
