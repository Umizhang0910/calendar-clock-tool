package com.umizhang.widget;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

/**
 * Status bar widget
 */
public final class TimeStatusBarWidget implements StatusBarWidget, StatusBarWidget.MultipleTextValuesPresentation, Disposable {

    private static final Logger log = Logger.getInstance(TimeStatusBarWidget.class);
    private final Project project;
    // Timer 更适合：需要每秒更新UI；高精度时间刷新（1秒间隔）；应用运行时持续工作
    // private final Alarm alarm;
    private final Timer timer;
    private final Icon icon;
    private StatusBar statusBar;
    private String currentTime;

    public TimeStatusBarWidget(Project project) {
        this.project = project;
        icon = IconLoader.getIcon("/icons/time.svg", TimeStatusBarWidget.class);
        // this.alarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD);
        // updateTime();
        // scheduleUpdate();

        // 创建每秒更新的计时器
        timer = new Timer(1000, e -> {
            updateTime();
            if (statusBar != null) {
                statusBar.updateWidget(ID());
            }
        });
        log.info("Create widget instance: {}" + project.getName());
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        currentTime = sdf.format(System.currentTimeMillis());
        /*if (statusBar != null) {
            statusBar.updateWidget(ID());
        }*/
    }

    /*private void scheduleUpdate() {
        alarm.addRequest(() -> {
            if (project.isDisposed()) {
                return;
            }
            updateTime();
            scheduleUpdate(); // Schedule the next update
        }, 1000); // Update every second
    }*/

    @NotNull
    @Override
    public String ID() {
        return "TimeStatusWidget";
    }

    @Override
    public @NotNull StatusBarWidget.WidgetPresentation getPresentation() {
        return this;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        this.statusBar = statusBar;
        timer.start();
    }

    @Override
    public void dispose() {
        log.info("Widget disposal: " + project.getName());
        /*if (alarm != null && !alarm.isDisposed()) {
            alarm.cancelAllRequests();
            Disposer.dispose(alarm);
        }*/
        timer.stop();
        statusBar = null;
    }

    @Nullable
    @Override
    public String getSelectedValue() {
        return currentTime;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public @NotNull String getTooltipText() {
        return "Current Time";
    }

    @Override
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        return null; // 无点击行为
    }
}