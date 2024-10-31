package ru.kelcuprum.alinlib.gui.screens.types;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.*;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.Colors;
import ru.kelcuprum.alinlib.gui.components.*;
import ru.kelcuprum.alinlib.gui.components.builder.button.ButtonBuilder;
import ru.kelcuprum.alinlib.gui.components.text.*;
import ru.kelcuprum.alinlib.gui.screens.*;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
            AlinLib.LOG.log(Component.translatable("alinlib.component.reset.toast"));
        }).setSprite(RESET).setSize(20, 20).setPosition(10, 5).build());
        addRenderableWidgets(builder.panelWidgets);
    }

    Component description = Component.empty();

    public void initCategory() {
        int width = this.width - 20;
        int oy = 30;
        for (AbstractWidget widget : builder.widgets) {
            widget.setWidth(width);
            widget.setY(oy);
            widget.setX(10);
            oy+= (widget.getHeight()+5);
        }
        this.scroller = addRenderableWidget(new ConfigureScrolWidget(this.width - 8, 30, 4, this.height - 35, Component.empty(), scroller -> {
            scroller.innerHeight = 0;
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
                    widget.setY(30+((int) (scroller.innerHeight - scroller.scrollAmount())));
                    scroller.innerHeight += (widget.getHeight() + 5);
                } else widget.setY(-widget.getHeight());
            }
            scroller.innerHeight-=13;
            description = lastDescription != null ? lastDescription : Component.empty();
        }));
        addRenderableWidgets$scroller(builder.widgets);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        boolean st = true;
        GuiEventListener selected = null;
        for (GuiEventListener guiEventListener : this.children()) {
            if (scroller != null && scroller.widgets.contains(guiEventListener)) {
                if ((d >= 10 && d <= width - 10) && (e >= 30 && e <= height)) {
                    if (guiEventListener.mouseClicked(d, e, i)) {
                        st = false;
                        selected = guiEventListener;
                        break;
                    }
                }
            } else if (guiEventListener.mouseClicked(d, e, i)) {
                st = false;
                selected = guiEventListener;
                break;
            }
        }

        this.setFocused(selected);
        if (i == 0) {
            this.setDragging(true);
        }

        return st;
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
        guiGraphics.enableScissor(0, 30, width, this.height-5);
        if (scroller != null) for (AbstractWidget widget : scroller.widgets) widget.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.disableScissor();
        if (!Objects.equals(description, Component.empty()))
            guiGraphics.renderTooltip(AlinLib.MINECRAFT.font, description, mouseX, mouseY);
    }
}
