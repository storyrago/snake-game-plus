package FinalTerm;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private SnakeGameController controller;
    private ImageManager imageManager;
    private Timer pulseTimer;
    private float pulseAlpha = 0.0f;
    
    public MainMenuPanel(SnakeGameController controller) {
        this.controller = controller;
        this.imageManager = new ImageManager();
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        initComponents();
        startPulseAnimation();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image bgImage = imageManager.getImage("background");
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(new Color(20, 20, 20));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    private void initComponents() {
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                String mainTitle = "SNAKE";
                Font titleFont = new Font("Arial", Font.BOLD, 72);
                g2d.setFont(titleFont);
                FontMetrics fm = g2d.getFontMetrics();
                
                Image logo = imageManager.getImage("logo");
                int logoSize = 80;
                int gap = 25;
                int textWidth = fm.stringWidth(mainTitle);
                int totalWidth = textWidth;
                if (logo != null) totalWidth += (logoSize + gap);
                int centerX = getWidth() / 2;
                int startX = centerX - (totalWidth / 2);
                int titleY = 90;
                
                if (logo != null) {
                    g2d.drawImage(logo, startX, titleY - logoSize + 10, logoSize, logoSize, null);
                }
                
                int titleX = startX;
                if (logo != null) titleX += (logoSize + gap);
                
                GradientPaint gp = new GradientPaint(titleX, titleY - 50, new Color(0, 255, 0), titleX, titleY + 20, new Color(0, 200, 0));
                g2d.setPaint(gp);
                g2d.drawString(mainTitle, titleX, titleY);
                
                // 태그라인 (한글) - Malgun Gothic 적용
                String tagline = "빠르고, 전략적이게!";
                g2d.setFont(new Font("Malgun Gothic", Font.ITALIC, 14));
                fm = g2d.getFontMetrics();
                g2d.setColor(new Color(120, 200, 120));
                g2d.drawString(tagline, centerX - fm.stringWidth(tagline) / 2, 175);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(500, 200));
        add(titlePanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 120, 80, 120));
        
        buttonPanel.add(createStyledButton("게임 시작", new Color(0, 200, 0), e -> controller.showScreen(GameScreen.NAME_INPUT)));
        buttonPanel.add(createStyledButton("랭킹", new Color(60, 60, 90), e -> controller.showScreen(GameScreen.RANKING)));
        buttonPanel.add(createStyledButton("테마 설정", new Color(90, 60, 90), e -> controller.showScreen(GameScreen.THEME_SELECT)));
        buttonPanel.add(createStyledButton("게임 설명", new Color(60, 90, 90), e -> controller.showScreen(GameScreen.RULES)));
        buttonPanel.add(createStyledButton("나가기", new Color(90, 30, 30), e -> System.exit(0)));
        
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    private JButton createStyledButton(String text, Color color, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        // [폰트 수정] Malgun Gothic
        button.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            action.actionPerformed(e);
        });
        UIAnimationHelper.addHoverEffect(button, color, color.brighter(), Color.WHITE, Color.WHITE);
        return button;
    }
    
    private void startPulseAnimation() {
        pulseTimer = new Timer(30, e -> {
            pulseAlpha += 0.02f;
            if (pulseAlpha > 1.0f) pulseAlpha = 0.0f;
            repaint();
        });
        pulseTimer.start();
    }
}