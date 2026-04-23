package com.pryzmm.coreconfig.ui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pryzmm.coreconfigapi.entry.MainEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

public class CCContainer implements CCElement {

    public int ccGetX() { return x; }
    public int ccGetY() { return y; }
    public int ccGetWidth() { return width; }
    public int ccGetHeight() { return height; }
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
        LinearLayout content = LinearLayout.vertical();
        content.defaultCellSetting().padding(2).paddingBottom(0);
        content.setPosition(x, y);
        widgets.forEach(widget -> {
            if (!(widget instanceof MainEntry entry) || entry.divider() == null || !entry.divider().getFoldedState()) content.addChild(widget);
        });
        content.arrangeElements();

        scrollWidget = new InnerScrollWidget(x, y, width, height, content);
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

        private final LinearLayout content;

        public InnerScrollWidget(int x, int y, int width, int height, LinearLayout content) {
            super(x, y, width, height, Component.empty());
            this.content = content;
        }

        public double getScrollAmount() {
            return scrollAmount();
        }

        @Override
        protected int getInnerHeight() {
            return content.getHeight();
        }

        @Override
        protected double scrollRate() {
            return 9.0;
        }

        @Override
        protected void renderBackground(@NotNull GuiGraphics pGuiGraphics) {}

        private int getScrollbarHeight() {
            return Mth.clamp((int)((float)(this.height * this.height) / (float)(this.getInnerHeight() + 4)), 32, this.height);
        }

        private static final ResourceLocation SCROLLER_SPRITE = new ResourceLocation("minecraft", "widget/scroller");
        @Override
        protected void renderDecorations(@NotNull GuiGraphics pGuiGraphics) {
            if (this.scrollbarVisible()) {
                int i = getScrollbarHeight();
                int j = this.getX() + this.width - 6;
                int k = Math.max(this.getY(), (int) this.scrollAmount() * (this.height - i) / this.getMaxScrollAmount() + this.getY());
                RenderSystem.enableBlend();
                pGuiGraphics.blitSprite(SCROLLER_SPRITE, j, k, 6, i);
                RenderSystem.disableBlend();
            }

        }

        @Override
        public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
            try {
                if (this.visible && this.isFocused() && SCROLLING_FIELD != null && (boolean) SCROLLING_FIELD.get(this)) {
                    if (pMouseY < (double)this.getY()) this.setScrollAmount(0.0F);
                    else if (pMouseY > (double)(this.getY() + this.height)) this.setScrollAmount(this.getMaxScrollAmount());
                    else {
                        int i = getScrollbarHeight();
                        double d0 = Math.max(1, this.getMaxScrollAmount() / ((this.height - i) == 0 ? 1 : (this.height - i)));
                        this.setScrollAmount(this.scrollAmount() + pDragY * d0);
                    }
                    return true;
                } else return false;
            } catch (IllegalAccessException ignored) {}
            return false;
        }

        @Override
        protected void renderContents(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            content.visitWidgets(widget -> widget.render(guiGraphics, mouseX, (int) (mouseY + scrollAmount()), partialTick));
        }

        public int getMaxScrollAmount() {
            return super.getMaxScrollAmount();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean isScrolling = this.scrollbarVisible() && mouseX >= (double)(this.getX() + this.width - 6) && mouseX <= (double)(this.getX() + this.width) && mouseY >= (double)this.getY() && mouseY < (double)(this.getY() + this.height);
            if (isScrolling) try { SCROLLING_FIELD.setBoolean(this, true); } catch (Exception ignored) {}
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
            if (pButton == 0) try { SCROLLING_FIELD.setBoolean(this, true); } catch (Exception ignored) {}
            return super.mouseReleased(pMouseX, pMouseY, pButton);
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}
    }
}