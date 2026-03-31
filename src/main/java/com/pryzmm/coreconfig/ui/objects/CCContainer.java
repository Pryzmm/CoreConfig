package com.pryzmm.coreconfig.ui.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import java.util.List;
import java.util.function.Consumer;

public class CCContainer implements CCElement {

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Minecraft minecraft;

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        scrollableLayout.visitWidgets(consumer);
    }

    private ScrollableLayout scrollableLayout;
    private LinearLayout content;

    public CCContainer(Minecraft minecraft, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.minecraft = minecraft;
    }

    public void populate(List<AbstractWidget> widgets) {
        content = LinearLayout.vertical();
        content.defaultCellSetting().padding(2);

        widgets.forEach(widget -> {
            FrameLayout frame = new FrameLayout(this.width, widget.getHeight());
            frame.addChild(widget, frame.newChildLayoutSettings().alignHorizontallyCenter());
            content.addChild(frame);
        });

        this.scrollableLayout = new ScrollableLayout(minecraft, content, height);
        this.scrollableLayout.setX(x - 24);
        this.scrollableLayout.setY(y);
        this.scrollableLayout.setMinWidth(this.width);
        this.scrollableLayout.setMinHeight(height);
        this.scrollableLayout.setMaxHeight(height);
        this.scrollableLayout.arrangeElements();
    }

    public boolean scrollable() {
        return getLayout().container.maxScrollAmount() > 0;
    }

    public ScrollableLayout getLayout() {
        return scrollableLayout;
    }


}