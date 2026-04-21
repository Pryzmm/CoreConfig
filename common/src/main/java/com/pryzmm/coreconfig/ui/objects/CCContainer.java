package com.pryzmm.coreconfig.ui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
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

    private InnerScrollWidget scrollWidget;

    public CCContainer(int x, int y, int width, int height) {
        this(x, y, width, height, null);
    }

    public CCContainer(int x, int y, int width, int height, Integer color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void populate(List<AbstractWidget> widgets) {
        final int GAP = 2;
        int currentY = y;
        for (AbstractWidget widget : widgets) {
            widget.setX(x);
            widget.setY(currentY);
            widget.setWidth(width);
            currentY += widget.getHeight() + GAP;
        }
        int totalHeight = currentY - y;
        scrollWidget = new InnerScrollWidget(x, y, width, height, widgets, totalHeight);
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        consumer.accept(scrollWidget);
    }

    public boolean scrollable() {
        return scrollWidget.getMaxScrollAmount() > 0;
    }

    public InnerScrollWidget getLayout() {
        return scrollWidget;
    }

    public static class InnerScrollWidget extends AbstractScrollWidget {

        private static Field SCROLLING_FIELD = null;
        static {
            try {
                SCROLLING_FIELD = AbstractScrollWidget.class.getDeclaredField("scrolling");
                SCROLLING_FIELD.setAccessible(true);
            } catch (Exception ignored) {}
        }

        private final List<AbstractWidget> widgets;
        private final int totalHeight;

        public InnerScrollWidget(int x, int y, int width, int height, List<AbstractWidget> widgets, int totalHeight) {
            super(x, y, width, height, Component.empty());
            this.widgets = widgets;
            this.totalHeight = totalHeight;
        }

        public double getScrollAmount() {
            return scrollAmount();
        }

        @Override
        protected int getInnerHeight() {
            return totalHeight;
        }

        @Override
        protected double scrollRate() {
            return 9.0;
        }

        @Override
        protected void renderBackground(@NotNull GuiGraphics guiGraphics) {}

        private int getScrollbarHeight() {
            return Mth.clamp((int)((float)(this.height * this.height) / (float)(this.getInnerHeight() + 4)), 32, this.height);
        }

        private static final ResourceLocation SCROLLER_SPRITE = new ResourceLocation("minecraft", "widget/scroller");

        @Override
        protected void renderDecorations(@NotNull GuiGraphics guiGraphics) {
            if (this.scrollbarVisible()) {
                int i = getScrollbarHeight();
                int j = this.getX() + this.width - 6;
                int k = Math.max(this.getY(), (int) this.scrollAmount() * (this.height - i) / this.getMaxScrollAmount() + this.getY());
                RenderSystem.enableBlend();
                guiGraphics.blit(SCROLLER_SPRITE, j, k, 0, 0, 6, i);
                RenderSystem.disableBlend();
            }
        }

        @Override
        protected void renderContents(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            int offset = (int) scrollAmount();
            for (AbstractWidget widget : widgets) {
                // Only render widgets that are within the visible area
                int widgetY = widget.getY() - offset;
                if (widgetY + widget.getHeight() < this.getY()) continue;
                if (widgetY > this.getY() + this.height) continue;
                widget.render(guiGraphics, mouseX, mouseY + offset, partialTick);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean isScrolling = this.scrollbarVisible()
                    && mouseX >= (double)(this.getX() + this.width - 6)
                    && mouseX <= (double)(this.getX() + this.width)
                    && mouseY >= (double)this.getY()
                    && mouseY < (double)(this.getY() + this.height);
            if (isScrolling) try { SCROLLING_FIELD.setBoolean(this, true); } catch (Exception ignored) {}
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
            try {
                if (this.visible && this.isFocused() && SCROLLING_FIELD != null && (boolean) SCROLLING_FIELD.get(this)) {
                    if (pMouseY < (double)this.getY()) this.setScrollAmount(0.0);
                    else if (pMouseY > (double)(this.getY() + this.height)) this.setScrollAmount(this.getMaxScrollAmount());
                    else {
                        int i = getScrollbarHeight();
                        double d0 = Math.max(1, this.getMaxScrollAmount() / ((this.height - i) == 0 ? 1 : (this.height - i)));
                        this.setScrollAmount(this.scrollAmount() + pDragY * d0);
                    }
                    return true;
                }
            } catch (IllegalAccessException ignored) {}
            return false;
        }

        @Override
        public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
            if (pButton == 0) try { SCROLLING_FIELD.setBoolean(this, false); } catch (Exception ignored) {}
            return super.mouseReleased(pMouseX, pMouseY, pButton);
        }

        public int getMaxScrollAmount() {
            return super.getMaxScrollAmount();
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}
    }
}