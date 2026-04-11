package com.pryzmm.coreconfig.ui.objects;

import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import net.minecraft.resources.Identifier;
import com.pryzmm.coreconfig.network.Server;
import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.entry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;

public class CCTooltip {

    private static Long firstRenderTime = null;
    private static Integer frame = 0;

    public static void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, CCEntry entry, CCContainer container) {
        if (entry == null) {
            frame = 0;
            return;
        }
        if (firstRenderTime == null) firstRenderTime = System.currentTimeMillis();
        Minecraft minecraft = Minecraft.getInstance();
        MutableComponent descriptor = Component.empty();
        int imageWidth = 0;
        int frameHeight = 0;
        ImageComponent image = null;

        if (entry instanceof MainEntry mainEntry) {

            if (CoreConfigScreen.activePopup != null) return;
            if (mainEntry.descriptor() == null && mainEntry.image() == null) return;

            image = mainEntry.image();
            imageWidth = image != null ? image.imageWidth() : 0;
            frameHeight = image != null ? image.frameHeight() : 0;

            Component component = (Component) mainEntry.descriptor();
            descriptor = component != null ? component.copy() : Component.empty();

        }

        switch (entry) {
            case IntegerEntry integerEntry -> {
                try {
                    int value = Integer.parseInt(String.valueOf(integerEntry.getUnsavedValue()));
                    if (value < integerEntry.minimum())
                        descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.lower_than_min", value, integerEntry.minimum()));
                    if (value > integerEntry.maximum())
                        descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.higher_than_max", value, integerEntry.maximum()));
                } catch (Exception ignored) {
                    descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.not_int"));
                }
            }
            case FloatEntry floatEntry -> {
                try {
                    float value = Float.parseFloat(String.valueOf(floatEntry.getUnsavedValue()));
                    if (value < floatEntry.minimum())
                        descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.lower_than_min", value, floatEntry.minimum()));
                    if (value > floatEntry.maximum())
                        descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.higher_than_max", value, floatEntry.maximum()));
                } catch (Exception ignored) {
                    descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.not_float"));
                }
            }
            case DoubleEntry doubleEntry -> {
                try {
                    double value = Double.parseDouble(String.valueOf(doubleEntry.getUnsavedValue()));
                    if (value < doubleEntry.minimum())
                        descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.lower_than_min", value, doubleEntry.minimum()));
                    if (value > doubleEntry.maximum())
                        descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.higher_than_max", value, doubleEntry.maximum()));
                } catch (Exception ignored) {
                    descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.not_double"));
                }
            }
            case StringEntry stringEntry -> {
                try {
                    String value = stringEntry.getUnsavedValue();
                    if (value.length() < stringEntry.minimumLength())
                        descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.lower_than_min_string", value.length(), stringEntry.minimumLength()));
                    if (value.length() > stringEntry.maximumLength())
                        descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.higher_than_max_string", value.length(), stringEntry.maximumLength()));
                } catch (Exception ignored) {
                    descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.unknown"));
                }
            }
            default -> {}
        }

        if (entry instanceof MainEntry mainEntry) {
            if (mainEntry.type() == ConfigType.SERVER && !Server.isHostingServer()) {
                descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.server_side"));
            } else if (mainEntry.type() == ConfigType.COMMON && mainEntry.getServerValue() != null && !Server.isHostingServer()) {
                descriptor.append("\n\n").append(Component.translatable("error.coreconfig.error")).append("\n").append(Component.translatable("error.coreconfig.server_side_common"));
            }
        }

        final int[] maxTextWidth = {0};
        minecraft.font.split(descriptor, 170).forEach(sequence -> {
            int width = minecraft.font.width(sequence);
            if (width > maxTextWidth[0]) maxTextWidth[0] = width;
        });

        if (maxTextWidth[0] == 0 && image == null) return;

        int tooltipWidth = Math.max(imageWidth, maxTextWidth[0]) + 24;
        List<FormattedCharSequence> sequences = minecraft.font.split(descriptor, tooltipWidth - 24);
        int tooltipHeight = frameHeight + (sequences.size() * (minecraft.font.lineHeight + 2)) + 24;

        int tooltipX = mouseX;
        int tooltipY = mouseY;
        if (mouseX + tooltipWidth > (container.getX() + container.getWidth())) tooltipX = (container.getX() + container.getWidth()) - tooltipWidth;
        if (mouseY + tooltipHeight > (container.getY() + container.getHeight())) tooltipY = (container.getY() + container.getHeight()) - tooltipHeight;

        boolean hadScissor;
        try { graphics.disableScissor(); hadScissor = true; } catch (IllegalStateException ignored) { hadScissor = false; }
        graphics.nextStratum();

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TooltipRenderUtil.BACKGROUND_SPRITE, tooltipX, tooltipY, tooltipWidth, tooltipHeight, 0xFFFFFFFF);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TooltipRenderUtil.FRAME_SPRITE, tooltipX, tooltipY, tooltipWidth, tooltipHeight, 0xFFFFFFFF);

        if (image != null) {
            if (image.animationTime() >= 0) {
                int maxFrames = (image.imageHeight() / image.frameHeight());
                if (System.currentTimeMillis() - firstRenderTime >= 50L * image.animationTime()) {
                    frame++;
                    if (frame >= maxFrames) frame = 0;
                    firstRenderTime = System.currentTimeMillis();
                }
            }
            graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                Identifier.fromNamespaceAndPath(image.modID(), image.path()),
                tooltipX + 12, tooltipY + 12,
                0, frame * image.frameHeight(),
                image.imageWidth(), image.frameHeight(),
                image.imageWidth(), image.imageHeight()
            );
        }

        int textY = tooltipY + frameHeight + 14;
        for (FormattedCharSequence sequence : sequences) {
            graphics.text(
                minecraft.font, sequence,
                tooltipX + 12, textY,
                0xFFFFFFFF,
                true
            );
            textY += minecraft.font.lineHeight + 2;
        }

        if (hadScissor) graphics.enableScissor(
            container.getX(), container.getY(),
            container.getX() + container.getWidth(),
            container.getY() + container.getHeight()
        );

    }

}
