package ru.kelcuprum.alinlib.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.gui.Colors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigureScrolWidget extends AbstractScrollWidget {
    public final Consumer<ConfigureScrolWidget> onScroll;
    public int innerHeight;
    public List<AbstractWidget> widgets = new ArrayList<>();

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
    protected void setScrollAmount(double amount) {
        super.setScrollAmount(amount);
        this.onScroll.accept(this);
    }

    @Override
    protected void renderBackground(GuiGraphics guiGraphics) {
        if (this.scrollbarVisible()) guiGraphics.fill(getX()+this.width, getY(), getX()+this.width+4, getY()+getHeight(), 0x75000000);
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

    public void resetWidgets(){
        widgets.clear();
        setScrollAmount(0);
    }
    public void addWidget(AbstractWidget widget) {
        widgets.add(widget);
    }
    public void addWidgets(List<AbstractWidget> widgets) {
        for(AbstractWidget widget : widgets) addWidget(widget);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
    }

    // Original code: https://modrinth.com/mod/smooth-scrolling-refurbished
    private double animationTimer = 0;
    private double scrollStartVelocity = 0;

    public static double scrollSpeed = 0.5;
    public static double scrollbarDrag= 0.025;
    public static double animationDuration = 1.0;
    public static double pushBackStrength = 1.0;

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        if (!this.visible) {
            return false;
        } else {
            double amount = this.scrollAmount - g * this.scrollRate();
            if(AlinLib.bariumConfig.getBoolean("SCROLLER.SMOOTH", false) && scrollbarVisible()){
                double diff = amount - this.scrollAmount;
                diff = Math.signum(diff) * Math.min(Math.abs(diff), 10);
                diff *= scrollSpeed;
                if (Math.signum(diff) != Math.signum(scrollStartVelocity)) diff *= 2.5d;
                animationTimer *= 0.5;
                scrollStartVelocity = scrollbarVelocity(animationTimer, scrollStartVelocity) + diff;
                animationTimer = 0;
            } else this.setScrollAmount(amount);
            return true;
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if(AlinLib.bariumConfig.getBoolean("SCROLLER.SMOOTH", false)) {
            checkOutOfBounds(delta);
            if (Math.abs(scrollbarVelocity(animationTimer, scrollStartVelocity)) > 1.0) applyMotion(delta);
        }
        super.renderWidget(guiGraphics, mouseX, mouseY, delta);
    }
    private void applyMotion(float delta) {
        this.scrollAmount += scrollbarVelocity(animationTimer, scrollStartVelocity) * delta;
        animationTimer += delta * 10 / animationDuration;
        this.onScroll.accept(this);
    }

    private void checkOutOfBounds(float delta) {
        if (this.scrollAmount < 0) {
            this.scrollAmount += pushBackStrength(Math.abs(this.scrollAmount), delta);
            if (this.scrollAmount > -0.2) this.scrollAmount = 0;
        }
        if (this.scrollAmount > getMaxScrollAmount()) {
            this.scrollAmount -= pushBackStrength(this.scrollAmount - getMaxScrollAmount(), delta);
            if (this.scrollAmount < getMaxScrollAmount() + 0.2) this.scrollAmount = getMaxScrollAmount();
        }
    }
    // ScrollMath
    public static double scrollbarVelocity(double timer, double factor) {
        return Math.pow(1 - scrollbarDrag, timer) * factor;
    }

    public static int dampenSquish(double squish, int height) {
        double proportion = Math.min(1, squish / 100);
        return (int) (Math.min(0.85, proportion) * height);
    }

    public static double pushBackStrength(double distance, float delta) {
        return ((distance + 4d) * delta / 0.3d) / (3.2d/pushBackStrength);
    }
}