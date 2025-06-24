package com.umizhang.widget;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.NotNull;

public class TimeWidgetFactory implements StatusBarWidgetFactory {

    @Override
    public @NotNull String getId() {
        return "RealTimeClockWidget";
    }

    @Override
    public @NotNull String getDisplayName() {
        return "Real-Time Clock";
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true; // 始终可用
    }

    @Override
    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {
        return new TimeStatusBarWidget(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget widget) {
        if (widget instanceof TimeStatusBarWidget) {
            widget.dispose();
        }
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }
}
