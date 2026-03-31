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

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        layout.visitWidgets(consumer);
    }

    public CCHeader(Minecraft minecraft, int x, int y, int width, int height, Component text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.layout = LinearLayout.vertical();
        this.layout.defaultCellSetting().alignHorizontallyCenter();
        this.layout.setPosition(this.x + (this.width / 2) - (minecraft.font.width(text) / 2), this.y + (height / 2) - (minecraft.font.lineHeight / 2));
        this.layout.addChild(new StringWidget(text, minecraft.font));
        this.layout.arrangeElements();
    }

    public LinearLayout getLayout() {
        return layout;
    }
}
