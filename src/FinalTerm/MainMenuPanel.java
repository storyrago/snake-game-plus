package FinalTerm;

import javax.swing.*;
import java.awt.*;

// GradientPanel 대신 JPanel을 상속받아 직접 이미지를 그립니다.
public class MainMenuPanel extends JPanel {
    private SnakeGameController controller;
    private ImageManager imageManager;
    private Timer pulseTimer;
    private float pulseAlpha = 0.0f;
    
    public MainMenuPanel(SnakeGameController controller) {
        this.controller = controller;
        this.imageManager = new ImageManager();
        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // 기본 배경
        
        initComponents();
        startPulseAnimation();
    }
    
    // [핵심] 배경 이미지 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. 배경 이미지 그리기
        Image bgImage = imageManager.getImage("background");
        if (bgImage != null) {
            // 화면에 꽉 차게 그리기
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            // 이미지가 없으면 기본 어두운 배경
            g.setColor(new Color(20, 20, 20));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // 2. 어두운 오버레이 (글씨가 잘 보이도록 반투명 검은색 덮기)
        g.setColor(new Color(0, 0, 0, 150)); // 150: 투명도 (0~255)
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    
    private void initComponents() {
        // Title Panel with custom painting
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // 패널 자체는 투명하게 (부모의 배경 이미지가 보이도록)
                // super.paintComponent(g); 
                
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 1. 폰트 설정
                String mainTitle = "SNAKE";
                Font titleFont = new Font("Arial", Font.BOLD, 72);
                g2d.setFont(titleFont);
                FontMetrics fm = g2d.getFontMetrics();
                
                // 2. 로고 및 텍스트 위치 계산
                Image logo = imageManager.getImage("logo"); // snakegame.png
                int logoSize = 80;
                int gap = 25;
                int textWidth = fm.stringWidth(mainTitle);
                
                int totalWidth = textWidth;
                if (logo != null) totalWidth += (logoSize + gap);
                
                int centerX = getWidth() / 2;
                int startX = centerX - (totalWidth / 2);
                int titleY = 90;
                
                // 3. 로고 그리기 (왼쪽)
                if (logo != null) {
                    int logoY = titleY - logoSize + 10;
                    
                    // 로고 글로우 효과
                    int shadowAlpha = (int)(50 * pulseAlpha);
                    g2d.setColor(new Color(0, 255, 0, shadowAlpha));
                    g2d.fillOval(startX + 5, logoY + 5, logoSize - 10, logoSize - 10);
                    
                    g2d.drawImage(logo, startX, logoY, logoSize, logoSize, null);
                }
                
                // 4. 텍스트 그리기 (오른쪽)
                int titleX = startX;
                if (logo != null) titleX += (logoSize + gap);
                
                // Outer glow layers
                for (int i = 8; i > 0; i--) {
                    int alpha = (int)(30 * pulseAlpha * (8 - i) / 8.0);
                    g2d.setColor(new Color(0, 255, 0, alpha));
                    g2d.drawString(mainTitle, titleX - i, titleY - i);
                    g2d.drawString(mainTitle, titleX + i, titleY + i);
                    g2d.drawString(mainTitle, titleX - i, titleY + i);
                    g2d.drawString(mainTitle, titleX + i, titleY - i);
                }
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(mainTitle, titleX + 3, titleY + 3);
                
                // Main text with gradient
                GradientPaint gp = new GradientPaint(
                    titleX, titleY - 50, new Color(0, 255, 0),
                    titleX, titleY + 20, new Color(0, 200, 0)
                );
                g2d.setPaint(gp);
                g2d.drawString(mainTitle, titleX, titleY);
                
                // --- Subtitle Area ---
                String subtitle = "GAME+";
                Font subtitleFont = new Font("Arial", Font.BOLD, 28);
                g2d.setFont(subtitleFont);
                fm = g2d.getFontMetrics();
                
                int subtitleY = 130;
                int subtitleX = centerX - fm.stringWidth(subtitle) / 2;
                
                // Subtitle glow
                g2d.setColor(new Color(0, 200, 0, (int)(80 * pulseAlpha)));
                for (int i = 2; i > 0; i--) {
                    g2d.drawString(subtitle, subtitleX - i, subtitleY - i);
                    g2d.drawString(subtitle, subtitleX + i, subtitleY + i);
                }
                
                g2d.setColor(new Color(150, 255, 150));
                g2d.drawString(subtitle, subtitleX, subtitleY);
                
                // Decorative lines
                g2d.setColor(new Color(0, 255, 0, 80));
                g2d.setStroke(new BasicStroke(2));
                int lineY = 150;
                g2d.drawLine(centerX - 150, lineY, centerX - 50, lineY);
                g2d.drawLine(centerX + 50, lineY, centerX + 150, lineY);
                
                // Tagline
                String tagline = "Fast. Strategic. Addictive.";
                Font tagFont = new Font("Arial", Font.ITALIC, 14);
                g2d.setFont(tagFont);
                fm = g2d.getFontMetrics();
                int tagX = centerX - fm.stringWidth(tagline) / 2;
                g2d.setColor(new Color(120, 200, 120));
                g2d.drawString(tagline, tagX, 175);
            }
        };
        
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(500, 200));
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(5, 1, 0, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 120, 80, 120));
        
        JButton playButton = createStyledButton("PLAY", new Color(0, 200, 0), new Color(0, 255, 0));
        JButton rankingButton = createStyledButton("RANKING", new Color(60, 60, 90), new Color(100, 100, 150));
        JButton themeButton = createStyledButton("THEMES", new Color(90, 60, 90), new Color(150, 100, 150));
        JButton rulesButton = createStyledButton("RULES", new Color(60, 90, 90), new Color(100, 150, 150));
        JButton exitButton = createStyledButton("EXIT", new Color(90, 30, 30), new Color(150, 50, 50));
        
        playButton.addActionListener(e -> controller.showScreen(GameScreen.NAME_INPUT));
        rankingButton.addActionListener(e -> controller.showScreen(GameScreen.RANKING));
        themeButton.addActionListener(e -> controller.showScreen(GameScreen.THEME_SELECT));
        rulesButton.addActionListener(e -> controller.showScreen(GameScreen.RULES));
        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(playButton);
        buttonPanel.add(rankingButton);
        buttonPanel.add(themeButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(exitButton);
        
        add(buttonPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel versionLabel = new JLabel("Version 1.0  |  2025", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(200, 200, 200)); // 배경이 어두우므로 밝은색으로
        versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(versionLabel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text, Color normalColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(hoverColor.darker(), 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(true);
        
        UIAnimationHelper.addHoverEffect(button, normalColor, hoverColor, Color.WHITE, Color.BLACK);
        
        return button;
    }
    
    private void startPulseAnimation() {
        pulseTimer = new Timer(30, e -> {
            pulseAlpha += 0.02f;
            if (pulseAlpha > 1.0f) {
                pulseAlpha = 0.0f;
            }
            repaint();
        });
        pulseTimer.start();
    }
}