package com.pryzmm.coreconfig.ui.objects;

import com.pryzmm.coreconfig.entry.CCEntry;
import com.pryzmm.coreconfig.entry.IntegerEntry;
import com.pryzmm.coreconfig.entry.StringEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;

public class CCTooltip {

    public static void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, CCEntry entry, CCContainer container) {
        Minecraft minecraft = Minecraft.getInstance();

        if (entry.descriptor() == null && entry.image() == null) return;

        int imageWidth = entry.image() != null ? entry.image().width() : 0;
        int imageHeight = entry.image() != null ? entry.image().height() : 0;

        MutableComponent descriptor = entry.descriptor() != null ? entry.descriptor().copy() : Component.empty();

        if (entry instanceof IntegerEntry integerEntry) {
            try {
                int value = Integer.parseInt(String.valueOf(integerEntry.getUnsavedValue()));
                if (value < integerEntry.minimum()) descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.lower_than_min", value, integerEntry.minimum()));
                if (value > integerEntry.maximum()) descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.higher_than_max", value, integerEntry.maximum()));
            } catch (Exception ignored) {
                descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.not_int"));
            }
        } else if (entry instanceof StringEntry stringEntry) {
            try {
                String value = stringEntry.getUnsavedValue();
                if (value.length() < stringEntry.minimumLength()) descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.lower_than_min_string", value.length(), stringEntry.minimumLength()));
                if (value.length() > stringEntry.maximumLength()) descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.higher_than_max_string", value.length(), stringEntry.maximumLength()));
            } catch (Exception ignored) {
                descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.unknown"));
            }
        }

        final int[] maxTextWidth = {0};
        minecraft.font.split(descriptor, 170).forEach(sequence -> {
            int width = minecraft.font.width(sequence);
            if (width > maxTextWidth[0]) maxTextWidth[0] = width;
        });

        int tooltipWidth = Math.max(imageWidth, maxTextWidth[0]) + 24;
        List<FormattedCharSequence> sequences = minecraft.font.split(descriptor, tooltipWidth - 24);
        int tooltipHeight = imageHeight + (sequences.size() * (minecraft.font.lineHeight + 2)) + 24;

        int tooltipX = mouseX;
        int tooltipY = mouseY;
        if (mouseX + tooltipWidth > (container.getX() + container.getWidth())) tooltipX = (container.getX() + container.getWidth()) - tooltipWidth;
        if (mouseY + tooltipHeight > (container.getY() + container.getHeight())) tooltipY = (container.getY() + container.getHeight()) - tooltipHeight;

        ScreenRectangle previousScissor = graphics.scissorStack.peek();
        if (previousScissor != null) graphics.disableScissor();
        graphics.nextStratum();

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TooltipRenderUtil.BACKGROUND_SPRITE, tooltipX, tooltipY, tooltipWidth, tooltipHeight, 0xFFFFFFFF);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TooltipRenderUtil.FRAME_SPRITE, tooltipX, tooltipY, tooltipWidth, tooltipHeight, 0xFFFFFFFF);

        if (entry.image() != null) graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            entry.image().identifier(),
            tooltipX + 12, tooltipY + 12,
            0, 0,
            imageWidth, imageHeight,
            imageWidth, imageHeight
        );

        int textY = tooltipY + imageHeight + 14;
        for (FormattedCharSequence sequence : sequences) {
            graphics.text(
                minecraft.font, sequence,
                tooltipX + 12, textY,
                0xFFFFFFFF,
                true
            );
            textY += minecraft.font.lineHeight + 2;
        }

        if (previousScissor != null) graphics.enableScissor(
            container.getX(), container.getY(),
            container.getX() + container.getWidth(),
            container.getY() + container.getHeight()
        );

    }

}
