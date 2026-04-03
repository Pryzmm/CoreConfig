package com.pryzmm.coreconfig.ui.objects;

import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface CCElement {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    @Nullable Integer getColor();
    void visitWidgets(Consumer<AbstractWidget> consumer);
}