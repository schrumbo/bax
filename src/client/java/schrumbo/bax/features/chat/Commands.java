package schrumbo.bax.features.chat;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import schrumbo.bax.config.ConfigManager;
import schrumbo.bax.utils.ChatUtils;
import schrumbo.bax.utils.ItemUtils;

import java.util.Objects;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.BaxClient.highlightConfig;

public class Commands {
    public static void register(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            highlightCommand(dispatcher);
        });
    }

    public static void highlightCommand(CommandDispatcher<FabricClientCommandSource> dispatcher){
        dispatcher.register(ClientCommandManager.literal("bax")
                .then(ClientCommandManager.literal("highlight")
                        .then(ClientCommandManager.literal("add")
                                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                        .executes(commandContext -> {
                                            String name = StringArgumentType.getString(commandContext, "name");
                                            highlightConfig.addName(name);
                                            return 1;
                                        })
                                )
                                .then(ClientCommandManager.literal("starred")
                                        .executes(commandContext -> {
                                            highlightConfig.addName("✯");
                                            return 1;
                                        })
                                )
                        )
                        .then(ClientCommandManager.literal("remove")
                                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                        .executes(commandContext -> {
                                            String name = StringArgumentType.getString(commandContext, "name");
                                            highlightConfig.removeName(name);
                                            return 1;
                                        })
                                )
                                .then(ClientCommandManager.literal("starred")
                                        .executes(commandContext -> {
                                            highlightConfig.removeName("✯");
                                            return 1;
                                        })
                                )
                        )
                        .then(ClientCommandManager.literal("list")
                                .executes(commandContext -> {
                                    if (highlightConfig.getHighlightList().isEmpty()){
                                        ChatUtils.modMessage("List is empty.");
                                        return 1;
                                    }
                                    ChatUtils.modMessage("List of all Names to highlight: ");
                                    for (String name: highlightConfig.getHighlightList()){
                                        ChatUtils.modMessage(name);
                                    }
                                    return 1;
                                })
                        )
                        .then(ClientCommandManager.literal("clear")
                                .executes(commandContext -> {
                                    highlightConfig.clearHighlightList();
                                    return 1;
                                })
                        )
                )
                .then(ClientCommandManager.literal("petswap")
                        .executes(commandContext -> {
                            config.setPetUUID(ItemUtils.getItemUUID(Objects.requireNonNull(ItemUtils.getHeldItem())));
                            ConfigManager.save();
                            ChatUtils.modMessage("set pet: " + config.getPetUUID());
                            return 1;
                        })

                )
        );
    }
}