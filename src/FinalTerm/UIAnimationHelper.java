package FinalTerm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UIAnimationHelper {
    
    // 버튼 호버 애니메이션
    public static void addHoverEffect(JButton button, Color normalBg, Color hoverBg, Color normalFg, Color hoverFg) {
        button.addMouseListener(new MouseAdapter() {
            Timer timer;
            float alpha = 0.0f;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                timer = new Timer(20, evt -> {
                    alpha = Math.min(1.0f, alpha + 0.1f);
                    
                    int r = (int)(normalBg.getRed() + (hoverBg.getRed() - normalBg.getRed()) * alpha);
                    int g = (int)(normalBg.getGreen() + (hoverBg.getGreen() - normalBg.getGreen()) * alpha);
                    int b = (int)(normalBg.getBlue() + (hoverBg.getBlue() - normalBg.getBlue()) * alpha);
                    
                    button.setBackground(new Color(r, g, b));
                    button.setForeground(alpha > 0.5f ? hoverFg : normalFg);
                    
                    if (alpha >= 1.0f) {
                        ((Timer)evt.getSource()).stop();
                    }
                });
                timer.start();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                
                timer = new Timer(20, evt -> {
                    alpha = Math.max(0.0f, alpha - 0.1f);
                    
                    int r = (int)(normalBg.getRed() + (hoverBg.getRed() - normalBg.getRed()) * alpha);
                    int g = (int)(normalBg.getGreen() + (hoverBg.getGreen() - normalBg.getGreen()) * alpha);
                    int b = (int)(normalBg.getBlue() + (hoverBg.getBlue() - normalBg.getBlue()) * alpha);
                    
                    button.setBackground(new Color(r, g, b));
                    button.setForeground(alpha > 0.5f ? hoverFg : normalFg);
                    
                    if (alpha <= 0.0f) {
                        ((Timer)evt.getSource()).stop();
                    }
                });
                timer.start();
            }
        });
    }
    
    // 페이드 인 효과
    public static void fadeIn(JComponent component, int duration) {
        component.setOpaque(false);
        Timer timer = new Timer(20, null);
        final float[] alpha = {0.0f};
        
        timer.addActionListener(e -> {
            alpha[0] = Math.min(1.0f, alpha[0] + (20.0f / duration));
            component.repaint();
            
            if (alpha[0] >= 1.0f) {
                timer.stop();
                component.setOpaque(true);
            }
        });
        
        timer.start();
    }
    
    // 스케일 애니메이션
    public static void scaleAnimation(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            Timer timer;
            float scale = 1.0f;
            boolean growing = false;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                growing = true;
                if (timer != null && timer.isRunning()) return;
                
                timer = new Timer(15, evt -> {
                    if (growing) {
                        scale = Math.min(1.05f, scale + 0.01f);
                    } else {
                        scale = Math.max(1.0f, scale - 0.01f);
                    }
                    
                    Font font = button.getFont();
                    button.setFont(font.deriveFont(font.getSize2D() * scale / 1.0f));
                    
                    if ((growing && scale >= 1.05f) || (!growing && scale <= 1.0f)) {
                        ((Timer)evt.getSource()).stop();
                    }
                });
                timer.start();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                growing = false;
                if (timer != null && timer.isRunning()) return;
                
                timer = new Timer(15, evt -> {
                    scale = Math.max(1.0f, scale - 0.01f);
                    
                    Font font = button.getFont();
                    button.setFont(font.deriveFont(font.getSize2D() * scale / 1.0f));
                    
                    if (scale <= 1.0f) {
                        ((Timer)evt.getSource()).stop();
                    }
                });
                timer.start();
            }
        });
    }
}
