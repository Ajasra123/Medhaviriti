package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnimationUtils {
    private static final int ANIMATION_DURATION = 200; // milliseconds
    private static final int STEPS = 20;

    public static void addHoverAnimation(JComponent component, Color normalColor, Color hoverColor) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateColor(component, normalColor, hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateColor(component, hoverColor, normalColor);
            }
        });
    }

    public static void addClickAnimation(JButton button) {
        button.addActionListener(e -> {
            Color originalColor = button.getBackground();
            Color pressedColor = originalColor.darker();
            
            Timer timer = new Timer(ANIMATION_DURATION / STEPS, null);
            timer.addActionListener(new ActionListener() {
                private int step = 0;
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (step < STEPS) {
                        float ratio = (float) step / STEPS;
                        Color currentColor = blendColors(pressedColor, originalColor, ratio);
                        button.setBackground(currentColor);
                        step++;
                    } else {
                        button.setBackground(originalColor);
                        timer.stop();
                    }
                }
            });
            timer.start();
        });
    }

    public static void addSlideAnimation(JComponent component, int startX, int endX, int duration) {
        Timer timer = new Timer(duration / STEPS, null);
        timer.addActionListener(new ActionListener() {
            private int step = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step < STEPS) {
                    float ratio = (float) step / STEPS;
                    int currentX = (int) (startX + (endX - startX) * ratio);
                    component.setLocation(currentX, component.getY());
                    step++;
                } else {
                    component.setLocation(endX, component.getY());
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private static void animateColor(JComponent component, Color startColor, Color endColor) {
        Timer timer = new Timer(ANIMATION_DURATION / STEPS, null);
        timer.addActionListener(new ActionListener() {
            private int step = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (step < STEPS) {
                    float ratio = (float) step / STEPS;
                    Color currentColor = blendColors(startColor, endColor, ratio);
                    component.setBackground(currentColor);
                    step++;
                } else {
                    component.setBackground(endColor);
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private static Color blendColors(Color color1, Color color2, float ratio) {
        float inverseRatio = 1.0f - ratio;
        float red = color1.getRed() * inverseRatio + color2.getRed() * ratio;
        float green = color1.getGreen() * inverseRatio + color2.getGreen() * ratio;
        float blue = color1.getBlue() * inverseRatio + color2.getBlue() * ratio;
        return new Color((int) red, (int) green, (int) blue);
    }
} 