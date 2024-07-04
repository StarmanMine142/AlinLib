package ru.kelcuprum.alinlib.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import ru.kelcuprum.alinlib.gui.Colors;

import java.util.function.Consumer;

public class ConfigureScrolWidget extends AbstractScrollWidget {
    public final Consumer<ConfigureScrolWidget> onScroll;
    public int innerHeight;

    public ConfigureScrolWidget(int x, int y, int width, int height, Component message, Consumer<ConfigureScrolWidget> onScroll) {
        super(x, y, width, height, message);

        this.onScroll = onScroll;
    }

    @Override
    protected int getInnerHeight() {
        return innerHeight;
    }

    @Override
    protected double scrollRate() {
        return 9.0;
    }

    @Override
    public double scrollAmount() {
        return super.scrollAmount();
    }

    @Override
    protected void setScrollAmount(double scrollAmount) {
        super.setScrollAmount(scrollAmount);
        this.onScroll.accept(this);
    }

    @Override
    protected void renderBackground(GuiGraphics guiGraphics) {
        if (this.scrollbarVisible()) guiGraphics.fill(getX()+this.width, getY(), getX()+this.width+4, getHeight(), 0x75000000);
    }

    private int getContentHeight() {
        return this.getInnerHeight() + 4;
    }

    private int getScrollBarHeight() {
        return Mth.clamp((int)((float)(this.height * this.height) / (float)this.getContentHeight()), 16, this.height);
    }

    @Override
    protected void renderDecorations(GuiGraphics guiGraphics) {
        if (this.scrollbarVisible()) {
            int i = this.getScrollBarHeight();
            int j = this.getX() + this.width;
            int k = Math.max(this.getY(), (int)this.scrollAmount() * (this.height - i) / this.getMaxScrollAmount() + this.getY());
            RenderSystem.enableBlend();
            guiGraphics.fill(j, k, j+4, k+i, Colors.getScrollerColor());
            RenderSystem.disableBlend();
        }
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }

    @Override
    protected void renderBorder(GuiGraphics guiGraphics, int x, int y, int width, int height) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
    }
}