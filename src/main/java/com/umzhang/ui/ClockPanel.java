package com.umzhang.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ClockPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();

        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制时钟表盘（背景）
//        int width = getWidth() / 2;
//        int height = getHeight() / 2;
        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = size / 2 - 10;// 留出边距

        g2d.setColor(Color.WHITE);
        g2d.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // 绘制刻度
        for (int i = 0; i < 60; i++) {
            double angle = Math.toRadians(i * 6 - 90); // 每个刻度6度 // 调整角度，使12点在正上方
            int innerRadius = (i % 5 == 0) ? radius - 15 : radius - 12; // 每5刻度为长刻度
            int x1 = centerX + (int) ((radius - 10) * Math.cos(angle)); // 内部刻度
            int y1 = centerY + (int) ((radius - 10) * Math.sin(angle));
            int x2 = centerX + (int) (innerRadius * Math.cos(angle)); // 外部刻度
            int y2 = centerY + (int) (innerRadius * Math.sin(angle));
            g2d.setStroke(new BasicStroke((i % 5 == 0) ? 2 : 1)); // 长刻度更粗
            g2d.drawLine(x1, y1, x2, y2);

            // 绘制数字
            if (i % 5 == 0) { // 仅在长刻度处绘制数字
                int number = i / 5 == 0 ? 12 : i / 5; // 计算数字
                int textRadius = radius - 25; // 数字距离中心的半径
                int textX = centerX + (int) (textRadius * Math.cos(angle)) - 5; // 调整位置
                int textY = centerY + (int) (textRadius * Math.sin(angle)) + 5;

                // Set font size for numbers
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString(String.valueOf(number), textX, textY);
            }
        }

        // 绘制时针、分针和秒针
        drawHand(g2d, centerX, centerY, radius * 0.4, (hour % 12 + minute / 60.0) * 30, 3); // 时针
        drawHand(g2d, centerX, centerY, radius * 0.5, minute * 6, 2); // 分针
        drawHand(g2d, centerX, centerY, radius * 0.6, second * 6, 1, Color.RED); // 秒针
    }

    private void drawHand(Graphics2D g2d, int x, int y, double length, double angle, int width) {
        drawHand(g2d, x, y, length, angle, width, Color.BLACK);
    }

    private void drawHand(Graphics2D g2d, int x, int y, double length, double angle, int width, Color color) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(width));
        double radian = Math.toRadians(angle - 90);
        int xEnd = x + (int) (length * Math.cos(radian));
        int yEnd = y + (int) (length * Math.sin(radian));
        g2d.drawLine(x, y, xEnd, yEnd);
    }
}
