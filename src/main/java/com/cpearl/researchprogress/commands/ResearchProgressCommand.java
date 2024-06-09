package com.cpearl.researchprogress.commands;

import com.cpearl.researchprogress.ResearchProgressHelper;
import com.cpearl.researchprogress.capability.ResearchCapabilityProvider;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;

public class ResearchProgressCommand {
    public static void registerCommands(RegisterCommandsEvent event) {
        var root = Commands.literal("research");
        root.then(Commands.literal("info").executes(context -> {
            showSelfInfo(context);
            return 0;
        }).then(Commands.argument("targets", EntityArgument.players()).executes(context -> {
            showInfo(context);
            return 0;
        })));
        root.then(Commands.literal("add").requires(p -> p.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("research", StringArgumentType.string())
                                .then(Commands.argument("point", IntegerArgumentType.integer()).executes(context -> {
                            addResearchPoints(context);
                            return 0;
                        })))));
        root.then(Commands.literal("set").requires(p -> p.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("research", StringArgumentType.string())
                                .then(Commands.argument("point", IntegerArgumentType.integer()).executes(context -> {
                            setResearchPoints(context);
                            return 0;
                        })))));
        root.then(Commands.literal("clear").requires(p -> p.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players()).executes(context -> {
                            clearPhase(context);
                            return 0;
                        })));
        event.getDispatcher().register(root);
    }

    private static void showInfo(CommandContext<CommandSourceStack> context) {
        try {
            var players = EntityArgument.getPlayers(context, "targets");
            players.forEach(player -> {
                StringBuilder researches = new StringBuilder();
                player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(phaseCapability -> {
                    phaseCapability.getResearches().forEach((research, point) -> {
                        researches.append(String.format(" %s: %d,", research, point));
                    });
                });
                context.getSource().sendSuccess(() -> Component.translatable("commands.researchprogress.info", player.getDisplayName(), researches.toString()), true);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showSelfInfo(CommandContext<CommandSourceStack> context) {
        var player = context.getSource().getPlayer();
        if (player == null)
            return;
        StringBuilder researches = new StringBuilder();
        player.getCapability(ResearchCapabilityProvider.RESEARCH).ifPresent(phaseCapability -> {
            phaseCapability.getResearches().forEach((research, point) -> {
                researches.append(String.format(" %s: %d,", research, point));
            });
        });
        context.getSource().sendSuccess(() -> Component.translatable("commands.researchprogress.info", player.getDisplayName(), researches.toString()), true);
    }

    private static void addResearchPoints(CommandContext<CommandSourceStack> context) {
        try {
            var players = EntityArgument.getPlayers(context, "targets");
            var research = StringArgumentType.getString(context, "research");
            var point = IntegerArgumentType.getInteger(context, "point");
            players.forEach(player -> {
                ResearchProgressHelper.addResearchPoints(player, research, point);
                context.getSource().sendSuccess(() -> Component.translatable("commands.researchprogress.add", player.getDisplayName(), research, point), true);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setResearchPoints(CommandContext<CommandSourceStack> context) {
        try {
            var players = EntityArgument.getPlayers(context, "targets");
            var research = StringArgumentType.getString(context, "research");
            var point = IntegerArgumentType.getInteger(context, "point");
            players.forEach(player -> {
                ResearchProgressHelper.setResearchPoints(player, research, point);
                context.getSource().sendSuccess(() -> Component.translatable("commands.researchprogress.set", player.getDisplayName(), research, point), true);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void clearPhase(CommandContext<CommandSourceStack> context) {
        try {
            var players = EntityArgument.getPlayers(context, "targets");
            players.forEach(player -> {
                ResearchProgressHelper.clearResearch(player);
                context.getSource().sendSuccess(() -> Component.translatable("commands.researchprogress.clear", player.getDisplayName()), true);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
