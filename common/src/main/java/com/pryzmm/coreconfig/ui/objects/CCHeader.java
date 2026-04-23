package com.pryzmm.coreconfig.ui.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

public class CCHeader implements CCElement {

    private final LinearLayout layout;
    private final int x, y, width, height;
    private final Integer color;

    public int ccGetX() { return x; }
    public int ccGetY() { return y; }
    public int ccGetWidth() { return width; }
    public int ccGetHeight() { return height; }
    public Integer getColor() { return color; }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        layout.visitWidgets(consumer);
    }

    public CCHeader(Minecraft minecraft, int x, int y, int width, int height, Component text) {
        this(minecraft, x, y, width, height, text, null);
    }

    public CCHeader(Minecraft minecraft, int x, int y, int width, int height, Component text, Integer color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;

        this.layout = new LinearLayout(width, height, LinearLayout.Orientation.VERTICAL);
        this.layout.defaultChildLayoutSetting().alignVerticallyMiddle();
        this.layout.setPosition(this.x + (this.width / 2) - (minecraft.font.width(text) / 2), this.y + (height / 2) - (minecraft.font.lineHeight / 2));
        this.layout.addChild(new StringWidget(text, minecraft.font));
        this.layout.arrangeElements();
    }

}
