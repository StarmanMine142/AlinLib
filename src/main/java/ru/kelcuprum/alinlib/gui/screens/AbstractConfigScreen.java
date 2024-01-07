package ru.kelcuprum.alinlib.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;
import ru.kelcuprum.alinlib.gui.components.ConfigureScrolWidget;
import ru.kelcuprum.alinlib.gui.components.Resetable;
import ru.kelcuprum.alinlib.gui.components.buttons.base.Button;
import ru.kelcuprum.alinlib.gui.components.buttons.ButtonSprite;
import ru.kelcuprum.alinlib.gui.components.text.CategoryBox;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConfigScreen extends Screen {
    private static final ResourceLocation icon = new ResourceLocation("alinlib", "textures/gui/widget/buttons/reset.png");

    protected List<AbstractWidget> widgetList = new ArrayList<>();
    private ConfigureScrolWidget scroller;
    private final Screen parent;
    public AbstractConfigScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        initPanelButtons();
        initCategory();
    }

    protected void initPanelButtons(){
        // -=-=-=-=-=-=-=-
        addRenderableWidget(new TextBox(10, 15, 110, this.font.lineHeight, this.title, true));
        // -=-=-=-=-=-=-=-
        // Exit Buttons
        // 85 before reset button
        addRenderableWidget(new Button(10, height - 30, 85, 20, InterfaceUtils.DesignType.VANILLA, CommonComponents.GUI_BACK, (OnPress) -> {
            AlinLib.bariumConfig.save();
            assert this.minecraft != null;
            this.minecraft.setScreen(parent);
        }));
        addRenderableWidget(new ButtonSprite(100, height-30, 20, 20, InterfaceUtils.DesignType.VANILLA, icon, Localization.getText("alinlib.component.reset"), (OnPress) -> {
            for(AbstractWidget widget : widgetList){
                if(widget instanceof Resetable){
                    ((Resetable) widget).resetValue();
                }
            }
            assert this.minecraft != null;
            new ToastBuilder()
                    .setTitle(title)
                    .setMessage(Component.translatable("alinlib.component.reset.toast"))
                    .setIcon(icon)
                    .show(this.minecraft.getToasts());
            AlinLib.log(Component.translatable("alinlib.component.reset.toast"));
        }));
    }
    protected void initCategory(){
        this.widgetList = new ArrayList<>();
        this.scroller = addRenderableWidget(new ConfigureScrolWidget(width - 8, 0, 4, height, Component.empty(), scroller -> {
            scroller.innerHeight = 5;

            for(AbstractWidget widget : widgetList){
                if(widget.visible){
//                    if (widget instanceof CategoryBox && scroller.innerHeight > 5) {
//                        scroller.innerHeight += 10;
//                    }

                    widget.setY((int) (scroller.innerHeight - scroller.scrollAmount()));

                    scroller.innerHeight += (widget.getHeight()+5);
                } else widget.setY(-widget.getHeight());
            }
        }));
        addRenderableWidget(scroller);
    }

    // Добавление виджетов
    protected void addRenderableWidgets(@NotNull List<AbstractWidget> widgets){
        for(AbstractWidget widget : widgets){
            addRenderableWidget(widget);
        }
    }
    @Override
    protected <T extends GuiEventListener & Renderable & NarratableEntry> @NotNull T addRenderableWidget(T widget) {
        return super.addRenderableWidget(widget);
    }
    protected void addCategoryWidget(AbstractWidget widget){
        widgetList.add(widget);
        addRenderableWidget(widget);
    };
    protected void addCategory(CategoryBox category){
        addCategoryWidget(category);
        widgetList.addAll(category.getValues());
        addRenderableWidgets(category.getValues());
    }

    // Рендер, скролл, прослушивание кей-биндов
    @Override
    public void tick(){
        if(scroller != null) scroller.onScroll.accept(scroller);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        boolean scr = super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);

        if (!scr && scroller != null) {
            scr = scroller.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        return scr;
    }
    //

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f){
        assert this.minecraft != null;
        InterfaceUtils.renderBackground(guiGraphics, this.minecraft);
        InterfaceUtils.renderLeftPanel(guiGraphics, 130, this.height);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}
