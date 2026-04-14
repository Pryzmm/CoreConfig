package com.pryzmm.coreconfig.ui.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractTextAreaWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
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
        LinearLayout content = LinearLayout.vertical();
        content.defaultCellSetting().padding(2).paddingBottom(0);
        content.setPosition(x, y);
        widgets.forEach(content::addChild);
        content.arrangeElements();

        scrollWidget = new InnerScrollWidget(x, y, width, height, content);
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        consumer.accept(scrollWidget);
    }

    public boolean scrollable() {
        return scrollWidget.maxScrollAmount() > 0;
    }

    public InnerScrollWidget getLayout() {
        return scrollWidget;
    }

    public static class InnerScrollWidget extends AbstractTextAreaWidget {

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

        private static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace("widget/scroller");
        @Override
        protected void renderDecorations(@NotNull GuiGraphics pGuiGraphics) {
            if (this.scrollbarVisible()) {
                int i = getScrollbarHeight();
                int j = this.getX() + this.width - 6;
                int k = Math.max(this.getY(), (int) this.scrollAmount() * (this.height - i) / this.maxScrollAmount() + this.getY());
                RenderSystem.enableBlend();
                pGuiGraphics.blitSprite(RenderType::guiTextured, SCROLLER_SPRITE, j, k, 6, i);
                RenderSystem.disableBlend();
            }

        }

        @Override
        public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
            if (this.visible && this.isFocused()) {
                if (pMouseY < (double)this.getY()) this.setScrollAmount(0.0F);
                else if (pMouseY > (double)(this.getY() + this.height)) this.setScrollAmount(this.maxScrollAmount());
                else {
                    int i = getScrollbarHeight();
                    double d0 = Math.max(1, this.maxScrollAmount() / (this.height - i));
                    this.setScrollAmount(this.scrollAmount() + pDragY * d0);
                }
                return true;
            } else return false;
        }

        @Override
        protected void renderContents(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            content.visitWidgets(widget -> widget.render(guiGraphics, mouseX, (int) (mouseY + scrollAmount()), partialTick));
        }

        @Override
        protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}
    }
}