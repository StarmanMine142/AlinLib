package ru.kelcuprum.alinlib.gui.components.builder.text;

import net.minecraft.network.chat.Component;
import ru.kelcuprum.alinlib.gui.components.builder.AbstractBuilder;
import ru.kelcuprum.alinlib.gui.components.text.HorizontalRule;
import ru.kelcuprum.alinlib.gui.components.text.TextBox;

public class HorizontalRuleBuilder extends AbstractBuilder {
    protected int height = 1;
    public HorizontalRuleBuilder(){
        this(Component.empty());
    }
    public HorizontalRuleBuilder(Component title){
        this(title, null);
    }
    public HorizontalRuleBuilder(Component title, TextBox.OnPress onPress){
        super(title);
    }
    public HorizontalRule build(){
        return new HorizontalRule(this);
    }
}
