package ru.kelcuprum.alinlib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import ru.kelcuprum.alinlib.api.events.client.ClientLifecycleEvents;
import ru.kelcuprum.alinlib.api.events.client.GuiRenderEvents;
import ru.kelcuprum.alinlib.command.AlinLibCommand;
import ru.kelcuprum.alinlib.config.Localization;

public class AlinLibTest implements ClientModInitializer {
    public static final String MODID = "alinlibtest";
    public static Localization localization = new Localization(MODID, "config/AlinLib/lang/");
    public static Minecraft MINECRAFT = Minecraft.getInstance();

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(AlinLibCommand::register);

        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            AlinLib.log(String.format("Client started. MC Version: %s", client.getLaunchedVersion()));
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> {
            AlinLib.log(String.format("Client stopped. MC Version: %s", client.getLaunchedVersion()));
        });
        GuiRenderEvents.RENDER.register((guiGraphics, partialTick) -> {
            AlinLib.log("Gui ticked?");
        });
    }
}
