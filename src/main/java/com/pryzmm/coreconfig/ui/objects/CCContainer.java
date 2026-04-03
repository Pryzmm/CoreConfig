package com.pryzmm.coreconfig.ui.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import java.util.List;
import java.util.function.Consumer;

public class CCContainer implements CCElement {

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Integer getColor() { return color; }

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Integer color;
    private final Minecraft minecraft;

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        scrollableLayout.visitWidgets(consumer);
    }

    private ScrollableLayout scrollableLayout;
    private LinearLayout content;

    public CCContainer(Minecraft minecraft, int x, int y, int width, int height) {
        this(minecraft, x, y, width, height, null);
    }
    public CCContainer(Minecraft minecraft, int x, int y, int width, int height, Integer color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minecraft = minecraft;
        this.color = color;
    }

    public void populate(List<AbstractWidget> widgets) {
        content = LinearLayout.vertical();
        content.defaultCellSetting().padding(2).paddingBottom(0);
        widgets.forEach(widget -> content.addChild(widget));
        this.scrollableLayout = new ScrollableLayout(minecraft, content, height);
        this.scrollableLayout.setX(x - 20);
        this.scrollableLayout.setY(y);
        this.scrollableLayout.setMinWidth(50);
        this.scrollableLayout.setMinHeight(height);
        this.scrollableLayout.setMaxHeight(height);
        this.scrollableLayout.arrangeElements();
        content.setX(x); // realigning
    }

    public boolean scrollable() {
        return getLayout().container.maxScrollAmount() > 0;
    }

    public ScrollableLayout getLayout() {
        return scrollableLayout;
    }


}