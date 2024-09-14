package ru.kelcuprum.alinlib.gui.screens.types;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.*;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.Colors;
import ru.kelcuprum.alinlib.gui.components.*;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.text.*;
import ru.kelcuprum.alinlib.gui.screens.*;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;

import java.util.Objects;

import static ru.kelcuprum.alinlib.gui.Icons.*;

public class ConfigScreen$withoutPanel extends AbstractConfigScreen {
    public ConfigScreen$withoutPanel(ConfigScreenBuilder builder) {
        super(builder);
    }

    @Override
    protected void init() {
        initPanelButtons();
        initCategory();
        super.init();
    }

    public void initPanelButtons() {
        // -=-=-=-=-=-=-=-
        titleW = addRenderableWidget(new TextBox(35, 5, width - 70, 20, this.builder.title, true));
        // -=-=-=-=-=-=-=-
        // Exit Buttons
        // 85 before reset button

        back = addRenderableWidget(new ButtonBuilder(AlinLib.isAprilFool() ? CommonComponents.GUI_BACK : Component.literal("x")).setOnPress((OnPress) -> {
            assert this.minecraft != null;
            this.minecraft.setScreen(builder.parent);
        }).setSprite(AlinLib.isAprilFool() ? EXIT : null).setPosition(width - 30, 5).setSize(20, 20).build());

        reset = addRenderableWidget(new ButtonBuilder(Component.translatable("alinlib.component.reset")).setOnPress((OnPress) -> {
            for (AbstractWidget widget : builder.widgets)
                if (widget instanceof Resetable) ((Resetable) widget).resetValue();
            assert this.minecraft != null;
            new ToastBuilder()
                    .setTitle(title)
                    .setMessage(Component.translatable("alinlib.component.reset.toast"))
                    .setIcon(RESET)
                    .buildAndShow();
            AlinLib.log(Component.translatable("alinlib.component.reset.toast"));
        }).setSprite(RESET).setSize(20, 20).setPosition(10, 5).build());
        addRenderableWidgets(builder.panelWidgets);
    }

    Component description = Component.empty();

    public void initCategory() {
        int width = this.width - 20;
        for (AbstractWidget widget : builder.widgets) {
            widget.setWidth(width);
            widget.setX(10);
        }
        this.scroller = addRenderableWidget(new ConfigureScrolWidget(this.width - 8, 30, 4, this.height - 30, Component.empty(), scroller -> {
            scroller.innerHeight = 30;
            CategoryBox lastCategory = null;
            Component lastDescription = null;
            for (AbstractWidget widget : builder.widgets) {
                if (widget.visible) {
                    if (widget instanceof Description) {
                        if (widget.isHoveredOrFocused() && ((Description) widget).getDescription() != null)
                            lastDescription = ((Description) widget).getDescription();
                    }
                    if (widget instanceof CategoryBox) {
                        if (lastCategory != widget && ((CategoryBox) widget).getState())
                            lastCategory = (CategoryBox) widget;
                    }
                    if (lastCategory != null && !(widget instanceof CategoryBox)) {
                        if (!lastCategory.values.contains(widget)) {
                            scroller.innerHeight += 6;
                            lastCategory.setRenderLine(true);
                            lastCategory = null;
                        }
                    }
                    widget.setY((int) (scroller.innerHeight - scroller.scrollAmount()));
                    scroller.innerHeight += (widget.getHeight() + 5);
                } else widget.setY(-widget.getHeight());
            }
            description = lastDescription != null ? lastDescription : Component.empty();
        }));
        addRenderableWidgets(builder.widgets);
    }

    //#if MC >= 12002
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        boolean scr = super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (!scr && scroller != null) {
            scr = scroller.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }
        return scr;
    }
    //#elseif MC < 12002
    //$$ @Override
    //$$   public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
    //$$      boolean scr = super.mouseScrolled(mouseX, mouseY, scrollY);
    //$$      if(mouseX <= this.builder.panelSize){
    //$$          if(descriptionBox.visible && (mouseX >= 5 && mouseX <= builder.panelSize-5) && (mouseY >= 40 && mouseY <= height - 30)){
    //$$                scr = descriptionBox.mouseScrolled(mouseX, mouseY, scrollY);
    //$$            } else if (!scr && scroller_panel != null) {
    //$$              scr = scroller_panel.mouseScrolled(mouseX, mouseY, scrollY);
    //$$          }
    //$$      } else {
    //$$          if (!scr && scroller != null) {
    //$$              scr = scroller.mouseScrolled(mouseX, mouseY, scrollY);
    //$$          }
    //$$      }
    //$$      return scr;
    //$$  }
    //#endif

    //#if MC >= 12002
    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        assert this.minecraft != null;
        super.renderBackground(guiGraphics, i, j, f);
        guiGraphics.fill(35, 5, width - 35, 25, Colors.BLACK_ALPHA);
    }
    //#elseif MC < 12002
    //$$
    //$$  @Override
    //$$  public void renderBackground(GuiGraphics guiGraphics){
    //$$      assert this.minecraft != null;
    //$$      super.renderBackground(guiGraphics);
    //$$      guiGraphics.fill(0, 0, this.builder.panelSize, this.height, Colors.BLACK_ALPHA);
    //$$  }
    //#endif

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        //#if MC < 12002
        //$$ renderBackground(guiGraphics);
        //#endif
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (!Objects.equals(description, Component.empty()))
            guiGraphics.renderTooltip(AlinLib.MINECRAFT.font, description, mouseX, mouseY);
    }
}