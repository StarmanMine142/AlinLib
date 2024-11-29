package ru.kelcuprum.alinlib.gui.screens;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.alinlib.gui.components.Resetable;
import ru.kelcuprum.alinlib.gui.components.builder.AbstractBuilder;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.screens.types.*;
import ru.kelcuprum.alinlib.gui.styles.AbstractStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigScreenBuilder {
    public Component title;
    public AbstractStyle style;
    public List<AbstractWidget> panelWidgets = new ArrayList<>();
    public List<AbstractWidget> widgets = new ArrayList<>();
    public OnTick onTick;
    public OnTickScreen onTickScreen;
    public Screen parent;
    public boolean isResetable = false;
    public int panelSize = AlinLib.bariumConfig.getBoolean("CONFIG_SCREEN.SMALL_PANEL_SIZE", false) ?  130 : 190;
    public int yL = AlinLib.bariumConfig.getBoolean("MODERN", true) ? 35 : 40;
    public int yC = 5;

    public ConfigScreenBuilder(Screen parent) {
        this(parent, Component.literal("Change me please"));
    }
    public ConfigScreenBuilder(Screen parent, Component title) {
        this(parent, title, GuiUtils.getSelected());
    }
    public ConfigScreenBuilder(Screen parent, Component title, AbstractStyle style){
        this.parent = parent;
        this.title = title;
        this.style = style;
    }
    //
    public ConfigScreenBuilder setTitle(String string){
        setTitle(Component.literal(string));
        return this;
    }
    public ConfigScreenBuilder setTitle(Component component) {
        this.title = component;
        return this;
    }
    public Component getTitle(){
        return this.title;
    }
    //
    public ConfigScreenBuilder setType(AbstractStyle style) {
        this.style = style;
        return this;
    }
    public AbstractStyle getType() {
        return this.style;
    }
    //
    public ConfigScreenBuilder setPanelSize(int panelSize) {
        this.panelSize = panelSize;
        return this;
    }
    public int getPanelSize() {
        return this.panelSize;
    }
    //
    public ConfigScreenBuilder addPanelWidget(AbstractBuilder builder){
        return addPanelWidget(builder.build());
    }
    public ConfigScreenBuilder addPanelWidget(AbstractWidget widget){
        widget.setWidth(this.panelSize-(AlinLib.bariumConfig.getBoolean("MODERN", true) ? 20 : 10));
        widget.setX(AlinLib.bariumConfig.getBoolean("MODERN", true) ? 10 : 5);
        widget.setY(yL);
        yL+=widget.getHeight()+5;
        this.panelWidgets.add(widget);
        return this;
    }

    public ConfigScreenBuilder addWidget(AbstractBuilder builder){
        return addWidget(builder.build());
    }
    CategoryBox lastCategory = null;
    public ConfigScreenBuilder addWidget(AbstractWidget widget){
        if(widget instanceof CategoryBox){
            if(lastCategory != widget) lastCategory = (CategoryBox) widget;
            this.widgets.add(widget);
            widget.setX(140);
            widget.setY(yC);
            yC+=widget.getHeight()+5;
            for(AbstractWidget cW : ((CategoryBox) widget).getValues()){
                this.widgets.add(cW);
                if(!isResetable && (cW instanceof Resetable)) isResetable = true;
                cW.setX(140);
                cW.setY(yC);
                yC+=cW.getHeight()+5;
            }
        } else {
            if(lastCategory != null){
                if(!lastCategory.values.contains(widget)) {
                    yC+=6;
                    lastCategory.setRenderLine(true);
                    lastCategory = null;
                }
            }
            this.widgets.add(widget);
            widget.setY(yC);
            widget.setX(140);
            yC+=widget.getHeight()+5;
        }
        if(!isResetable && (widget instanceof Resetable)) isResetable = true;
        return this;
    }
    //
    public ConfigScreenBuilder setResetable(boolean isResetable){
        this.isResetable = isResetable;
        return this;
    }
    public boolean getResetable(){
        return this.isResetable;
    }
    //
    public ConfigScreenBuilder setOnTick(OnTick onTick){
        this.onTick = onTick;
        return this;
    }
    public ConfigScreenBuilder setOnTickScreen(OnTickScreen onTickScreen){
        this.onTickScreen = onTickScreen;
        return this;
    }
    public OnTick getOnTick(){
        return this.onTick;
    }
    //
    public ConfigScreenBuilder setParent(Screen parent){
        this.parent = parent;
        return this;
    }

    public AbstractConfigScreen build() {
        Objects.requireNonNull(this.title, "title == null");
        return panelWidgets.isEmpty() ? new ConfigScreen$withoutPanel(this) : (AlinLib.bariumConfig.getBoolean("MODERN", true) ? new ConfigScreen$modern(this) : new ConfigScreen(this));
    }

    public interface OnTick {
        void onTick(ConfigScreenBuilder builder);
    }
    public interface OnTickScreen {
        void onTick(ConfigScreenBuilder builder, AbstractConfigScreen screen);
    }
}
