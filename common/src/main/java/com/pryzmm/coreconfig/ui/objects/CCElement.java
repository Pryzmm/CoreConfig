package com.pryzmm.coreconfig.ui.objects;

import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface CCElement {
    int ccGetX();
    int ccGetY();
    int ccGetWidth();
    int ccGetHeight();
    @Nullable Integer getColor();
    void visitWidgets(Consumer<AbstractWidget> consumer);
}