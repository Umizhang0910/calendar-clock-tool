package com.umizhang;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.umizhang.ui.ClockPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarClockToolWindowFactory implements ToolWindowFactory {
    private static final SimpleDateFormat sdf2ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf2ms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建主面板和子组件
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        // 在rowPanel中添加弹性空间
        JPanel rowPanel = new JPanel(new BorderLayout());
        // 左边面板
        JPanel leftContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));
        // 右边面板
        JPanel rightContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));

        JPanel textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension(300, 50));
        textPanel.setMaximumSize(new Dimension(300, 50));
        JLabel nowLabel = new JLabel();

        // Timestamp to Date section
        JPanel timestampToDatePanel = new JPanel();
        timestampToDatePanel.setPreferredSize(new Dimension(300, 120));
        timestampToDatePanel.setMaximumSize(new Dimension(300, 120));
        JLabel timestampLabel = new JLabel("Timestamp:");
        JTextField timestampField = new JTextField(13);
        ComboBox<String> timeUnitComboBox = new ComboBox<>(new String[]{"Seconds", "Milliseconds"});
        JButton convertToDateButton = new JButton("Convert >>");
        JTextField dateResult = new JTextField(20);

        // Date to Timestamp section
        JPanel dateToTimestampPanel = new JPanel();
        dateToTimestampPanel.setPreferredSize(new Dimension(300, 120));
        dateToTimestampPanel.setMaximumSize(new Dimension(300, 120));
        JLabel dateTimestampLabel = new JLabel("Date Time:");
        JTextField dateField = new JTextField(13);
        ComboBox<String> timeUnitComboBox2 = new ComboBox<>(new String[]{"Seconds", "Milliseconds"});
        JButton convertToTimestampButton = new JButton("Convert >>");
        JTextField timestampResult = new JTextField(20);

        ClockPanel clockPanel = new ClockPanel();
        clockPanel.setPreferredSize(new Dimension(150, 150));
        clockPanel.setMaximumSize(new Dimension(150, 150));
        ClockPanel clockPanel2 = new ClockPanel();
        clockPanel2.setPreferredSize(new Dimension(150, 150));
        clockPanel2.setMaximumSize(new Dimension(150, 150));

        // 使用 Timer 每秒刷新时钟
        new Timer(1000, e -> {
            long currentTimeMillis = System.currentTimeMillis();
            nowLabel.setText(String.format("Now: %tF %tT", currentTimeMillis, currentTimeMillis));
            clockPanel.repaint();
            clockPanel2.repaint();
        }).start();

        // Add action listeners
        convertToDateButton.addActionListener(e -> {
            try {
                long timestamp = Long.parseLong(timestampField.getText());
                String selectedUnit = (String) timeUnitComboBox.getSelectedItem();
                String formattedDate;
                if ("Milliseconds".equals(selectedUnit)) {
                    formattedDate = sdf2ms.format(new Date(timestamp));
                } else {
                    formattedDate = sdf2ss.format(new Date(timestamp));
                }
                dateResult.setText(formattedDate);
            } catch (NumberFormatException ex) {
                dateResult.setText("Invalid timestamp!");
            }
        });

        convertToTimestampButton.addActionListener(e -> {
            try {
                String formattedDate = dateField.getText();
                String selectedUnit = (String) timeUnitComboBox2.getSelectedItem();
                Date date;
                if ("Milliseconds".equals(selectedUnit)) {
                    date = sdf2ms.parse(formattedDate);
                } else {
                    date = sdf2ss.parse(formattedDate);
                }
                long timestamp = date.getTime();
                if ("Seconds".equals(selectedUnit)) {
                    timestamp /= 1000;
                }
                timestampResult.setText(String.valueOf(timestamp));
            } catch (ParseException ex) {
                timestampResult.setText("Invalid date time!");
            }
        });

        // 设置布局和添加组件
        textPanel.add(nowLabel);


        timestampToDatePanel.add(timestampLabel);
        timestampToDatePanel.add(timestampField);
        timestampToDatePanel.add(timeUnitComboBox);
        timestampToDatePanel.add(convertToDateButton);
        timestampToDatePanel.add(dateResult);
        dateToTimestampPanel.add(dateTimestampLabel);
        dateToTimestampPanel.add(dateField);
        dateToTimestampPanel.add(timeUnitComboBox2);
        dateToTimestampPanel.add(convertToTimestampButton);
        dateToTimestampPanel.add(timestampResult);

        leftContainer.add(textPanel);
        leftContainer.add(timestampToDatePanel);
        leftContainer.add(dateToTimestampPanel);
        rightContainer.add(clockPanel);
        rightContainer.add(clockPanel2);

        rowPanel.add(leftContainer, BorderLayout.WEST);
        rowPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER); // 弹性空间
        rowPanel.add(rightContainer, BorderLayout.EAST);

        mainPanel.add(rowPanel, BorderLayout.CENTER);

        // 将面板添加到工具窗口
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(mainPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
