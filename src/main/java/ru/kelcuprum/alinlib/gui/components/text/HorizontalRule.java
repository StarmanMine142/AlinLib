package ru.kelcuprum.alinlib.gui.components.text;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.Colors;
import ru.kelcuprum.alinlib.gui.components.builder.text.HorizontalRuleBuilder;

public class HorizontalRule extends AbstractWidget {
    public HorizontalRule(HorizontalRuleBuilder builder) {
        super(builder.getX(), builder.getY(), builder.getWidth(), builder.getHeight(), Component.empty());
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.fill(getX(), getY(), getRight(), getBottom(), Colors.getHorizontalRuleColor());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
