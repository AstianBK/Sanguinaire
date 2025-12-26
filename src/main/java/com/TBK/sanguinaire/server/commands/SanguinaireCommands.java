package com.TBK.sanguinaire.server.commands;

import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class SanguinaireCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("sanguinaire")
                        .requires(source -> source.hasPermission(2)) // OP only
                        .then(
                                Commands.literal("isvampire")
                                        .then(
                                                Commands.argument("player", EntityArgument.player())
                                                        .then(
                                                                Commands.argument("value", BoolArgumentType.bool())
                                                                        .executes(ctx -> {
                                                                            ServerPlayer target =
                                                                                    EntityArgument.getPlayer(ctx, "player");
                                                                            boolean value =
                                                                                    BoolArgumentType.getBool(ctx, "value");

                                                                            VampirePlayerCapability cap =
                                                                                    VampirePlayerCapability.get(target);

                                                                            if (cap != null) {
                                                                                cap.setIsVampire(value);
                                                                                cap.syncCap(target);
                                                                            }

                                                                            return 1;
                                                                        })
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("vampireage")
                                        .then(
                                                Commands.argument("player", EntityArgument.player())
                                                        .then(
                                                                Commands.argument("age",
                                                                                IntegerArgumentType.integer(0, 10))
                                                                        .executes(ctx -> {
                                                                            ServerPlayer target =
                                                                                    EntityArgument.getPlayer(ctx, "player");
                                                                            int age =
                                                                                    IntegerArgumentType.getInteger(ctx, "age");

                                                                            VampirePlayerCapability cap =
                                                                                    VampirePlayerCapability.get(target);

                                                                            if (cap != null && cap.isVampire()) {
                                                                                cap.setAge(age);
                                                                                cap.syncCap(target);
                                                                            }

                                                                            return 1;
                                                                        })
                                                        )
                                        )
                        )
        );
    }
}

