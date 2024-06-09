package com.cpearl.researchprogress.events;

import com.cpearl.researchprogress.ResearchProgress;
import com.cpearl.researchprogress.capability.ResearchCapability;
import com.cpearl.researchprogress.capability.ResearchCapabilityProvider;
import com.cpearl.researchprogress.commands.ResearchProgressCommand;
import com.cpearl.researchprogress.network.ResearchMessage;
import com.cpearl.researchprogress.network.ResearchPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ResearchProgress.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    @SubscribeEvent
    public static void addCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayer player) {
            event.addCapability(new ResourceLocation(ResearchProgress.MODID, "research"), new ResearchCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(phaseCapability -> {
                ResearchMessage msg = new ResearchMessage(phaseCapability.getResearches(), ResearchMessage.INIT);
                ResearchPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
            });
        }
    }

    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;
        if (event.getDimension().location().toString().equals("minecraft:overworld") &&
            player.serverLevel().dimension().location().toString().equals("minecraft:the_end"))
            player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(phaseCapability -> {
                if (phaseCapability instanceof ResearchCapability capability) {
                    player.getPersistentData().put("clone_researches", capability.serializeNBT());
                }
            });
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;
        player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(phaseCapability -> {
            if (phaseCapability instanceof ResearchCapability capability) {
                player.getPersistentData().put("clone_researches", capability.serializeNBT());
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal() instanceof ServerPlayer oldPlayer &&
                event.getEntity() instanceof ServerPlayer newPlayer) {
            newPlayer.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(phaseCapability -> {
                if (phaseCapability instanceof ResearchCapability capability) {
                    var tag = oldPlayer.getPersistentData().get("clone_researches");
                    if (tag instanceof CompoundTag compoundTag)
                        capability.deserializeNBT(compoundTag);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        ResearchProgressCommand.registerCommands(event);
    }
}
