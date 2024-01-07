package ru.kelcuprum.alinlib.gui.toast;

import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.kelcuprum.alinlib.gui.InterfaceUtils;

import java.util.Objects;

public class ToastBuilder {
    protected Component title;
    protected Component message;
    protected ItemStack itemIcon;
    protected ResourceLocation icon;
    protected PlayerSkin playerIcon;
    protected Type type;

    public ToastBuilder setTitle(Component component) {
        this.title = component;
        return this;
    }

    public ToastBuilder setMessage(Component component) {
        this.message = component;
        return this;
    }

    public ToastBuilder setIcon(Item item) {
        return setIcon(new ItemStack(item));
    }

    public ToastBuilder setIcon(ItemStack itemStack) {
        this.itemIcon = itemStack;
        return this;
    }

    public ToastBuilder setIcon(String namespace, String path) {
        return setIcon(new ResourceLocation(namespace, path));
    }

    public ToastBuilder setIcon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    public ToastBuilder setIcon(AbstractClientPlayer player) {
        return setIcon(player.getSkin());
    }

    public ToastBuilder setIcon(PlayerSkin playerIcon) {
        this.playerIcon = playerIcon;
        return this;
    }

    public boolean hasIcon() {
        return this.playerIcon != null || this.itemIcon != null || this.icon != null;
    }

    public ToastBuilder setType(Type type) {
        this.type = type;
        return this;
    }

    public AlinaToast build() {
        Objects.requireNonNull(this.title, "title == null");

        return new AlinaToast(this);
    }

    public AlinaToast buildAndShow(ToastComponent toasts) {
        AlinaToast toast = build();
        toasts.addToast(toast);

        return toast;
    }

    public void show(ToastComponent toasts) {
        buildAndShow(toasts);
    }

    public enum Type {
        FLAT(null),
        INFO(InterfaceUtils.Colors.SEADRIVE),
        WARN(InterfaceUtils.Colors.CLOWNFISH),
        DEBUG(InterfaceUtils.Colors.TETRA),
        ERROR(InterfaceUtils.Colors.GROUPIE);

        public final Integer color;

        Type(Integer color) {
            this.color = color;
        }
    }
}
