package FinalTerm;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private Color color1;
    private Color color2;
    private boolean vertical;
    
    public GradientPanel(Color color1, Color color2, boolean vertical) {
        this.color1 = color1;
        this.color2 = color2;
        this.vertical = vertical;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int w = getWidth();
        int h = getHeight();
        
        GradientPaint gp;
        if (vertical) {
            gp = new GradientPaint(0, 0, color1, 0, h, color2);
        } else {
            gp = new GradientPaint(0, 0, color1, w, 0, color2);
        }
        
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
