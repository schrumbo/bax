package schrumbo.bax.features.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;


public class PetCommand {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            petCommand(dispatcher);
        });
    }

    public static void petCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("bax")
                .then(ClientCommandManager.literal("pet")
                        .then(ClientCommandManager.literal("set")
                                .then(ClientCommandManager.argument("slot", IntegerArgumentType.integer(1, 9))
                                        .executes(commandContext -> {
                                            int slot = IntegerArgumentType.getInteger(commandContext, "slot");
                                            PetSwap.setPetFromHand(slot);
                                            return 1;
                                        })
                                )
                        )
                        .then(ClientCommandManager.literal("remove")
                                .then(ClientCommandManager.argument("slot", IntegerArgumentType.integer(1, 9))
                                        .executes(commandContext -> {
                                            int slot = IntegerArgumentType.getInteger(commandContext, "slot");
                                            PetSwap.removePet(slot);
                                            return 1;
                                        })
                                )
                        )
                        .then(ClientCommandManager.literal("list")
                                .executes(commandContext -> {
                                    PetSwap.listPets();
                                    return 1;
                                })
                        )
                        .then(ClientCommandManager.literal("info")
                                .then(ClientCommandManager.argument("slot", IntegerArgumentType.integer(1, 9))
                                        .executes(commandContext -> {
                                            int slot = IntegerArgumentType.getInteger(commandContext, "slot");
                                            PetSwap.showPetInfo(slot);
                                            return 1;
                                        })
                                )
                        )
                        .then(ClientCommandManager.literal("clear")
                                .executes(commandContext -> {
                                    PetSwap.clearAllPets();
                                    return 1;
                                })
                        )
                )
        );
    }
}