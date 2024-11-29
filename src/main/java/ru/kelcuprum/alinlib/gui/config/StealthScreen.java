package ru.kelcuprum.alinlib.gui.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBooleanBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.selector.SelectorBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.slider.SliderBuilder;
import ru.kelcuprum.alinlib.gui.components.builder.text.TextBuilder;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.screens.ConfigScreenBuilder;
import ru.kelcuprum.alinlib.gui.screens.ConfirmScreen;

import static ru.kelcuprum.alinlib.gui.Icons.*;

public class StealthScreen {
    public static Screen build(Screen parent) {
        ConfigScreenBuilder builder = new ConfigScreenBuilder(parent, Component.translatable("alinlib"))
                .addPanelWidget(new ButtonBuilder(Component.translatable("alinlib.config.design"), (s) -> AlinLib.MINECRAFT.setScreen(DesignScreen.build(parent))).setIcon(OPTIONS).setCentered(false))
                .addPanelWidget(new ButtonBuilder(Component.translatable("alinlib.config.stealth"), (s) -> AlinLib.MINECRAFT.setScreen(StealthScreen.build(parent))).setIcon(INVISIBILITY).setCentered(false))
                .addPanelWidget(new ButtonBuilder(Component.translatable("alinlib.localization"), (s) -> AlinLib.MINECRAFT.setScreen(LocalizationScreen.build(parent))).setIcon(LIST).setCentered(false));
        if(AlinLib.isNotReleaseVersion()){
            builder.addPanelWidget(new ButtonBuilder(Component.translatable("alinlib.title.not_release"),
                    (s) -> AlinLib.MINECRAFT.setScreen(new ConfirmScreen(builder.build(), Component.translatable("alinlib"),
                            Component.translatable("alinlib.title.not_release.description"), "https://github.com/kel-cu/alinlib/issues"))
            ).setIcon(SEARCH).setCentered(false));
        }
        builder.addWidget(new TextBuilder(Component.translatable("alinlib.config.stealth")))
                .addWidget(new ButtonBooleanBuilder(Component.translatable("alinlib.config.streamer.stealth"), false).setConfig(AlinLib.bariumConfig, "STREAMER.STEALTH"))
                .addWidget(new CategoryBox(Component.translatable("alinlib.config.stealth.coordinates"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("alinlib.config.streamer.stealth.coordinates"), true).setConfig(AlinLib.bariumConfig, "STREAMER.STEALTH.COORDINATES"))
                        .addValue(new ButtonBooleanBuilder(Component.translatable("alinlib.config.streamer.stealth.direction"), true).setConfig(AlinLib.bariumConfig, "STREAMER.STEALTH.DIRECTION"))
                        .addValue(new SelectorBuilder(Component.translatable("alinlib.config.streamer.stealth.type")).setValue(0).setList(new String[]{"ImGRUI Version", "AlinLib"}).setConfig(AlinLib.bariumConfig, "STREAMER.STEALTH.COORDINATES.TYPE"))
                )
                .addWidget(new CategoryBox(Component.translatable("alinlib.config.stealth.alinlib"))
                        .addValue(new SliderBuilder(Component.translatable("alinlib.config.streamer.alinlib.max_radius")).setMin(300).setDefaultValue(1000).setMax(100000).setConfig(AlinLib.bariumConfig, "STREAMER.STEALTH.ALINLIB.MAX_RADIUS").build())
                )
                .addWidget(new ButtonBooleanBuilder(Component.translatable("alinlib.config.streamer.stealth.name"), true).setConfig(AlinLib.bariumConfig, "STREAMER.STEALTH.NAME"));
        return builder.build();
    }
}
