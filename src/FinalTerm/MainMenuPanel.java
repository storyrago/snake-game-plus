package FinalTerm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
                
                // 설정 변수
                int logoSize = 110;
                int gap = 20;
                int startY = 60;
                
                String title1 = "SNAKE";
                String title2 = "Game+";
                
                Font font1 = new Font("Arial", Font.BOLD, 60);
                Font font2 = new Font("Arial", Font.BOLD, 45);
                
                g2d.setFont(font1);
                FontMetrics fm1 = g2d.getFontMetrics();
                int w1 = fm1.stringWidth(title1);
                
                g2d.setFont(font2);
                FontMetrics fm2 = g2d.getFontMetrics();
                int w2 = fm2.stringWidth(title2);
                
                int maxTextWidth = Math.max(w1, w2);
                int totalWidth = logoSize + gap + maxTextWidth;
                
                Image logo = imageManager.getImage("logo");
                if (logo == null) totalWidth -= (logoSize + gap);
                
                int centerX = getWidth() / 2;
                int startX = centerX - (totalWidth / 2);
                
                // 1. 로고
                if (logo != null) {
                    g2d.drawImage(logo, startX, startY, logoSize, logoSize, null);
                }
                
                // 2. 텍스트
                int textX = (logo != null) ? startX + logoSize + gap : startX;
                
                g2d.setFont(font1);
                GradientPaint gp = new GradientPaint(textX, startY, new Color(0, 255, 0), textX, startY + 50, new Color(0, 255, 0));
                g2d.setPaint(gp);
                g2d.drawString(title1, textX, startY + 55);
                
                g2d.setFont(font2);
                g2d.setColor(new Color(0, 255, 0));
                g2d.drawString(title2, textX, startY + 105);
                
                // 3. 구분선
                int lineY = startY + logoSize + 15;
                int lineWidth = totalWidth + 40;
                g2d.setColor(new Color(0, 255, 0));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(centerX - lineWidth/2, lineY, centerX + lineWidth/2, lineY);
                
                // 4. 태그라인
                String tagline = "당신의 한계에 도전하세요!!";
                g2d.setFont(new Font("Malgun Gothic", Font.ITALIC, 14));
                FontMetrics fmTag = g2d.getFontMetrics();
                g2d.setColor(new Color(120, 200, 120));
                g2d.drawString(tagline, centerX - fmTag.stringWidth(tagline) / 2, lineY + 25);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(500, 240));
        add(titlePanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 120, 60, 120));
        
        //각 버튼의 색상을 지정하여 호출
        buttonPanel.add(createStyledButton("게임 시작", new Color(0, 200, 0), e -> controller.showScreen(GameScreen.NAME_INPUT)));
        buttonPanel.add(createStyledButton("랭킹", new Color(100, 100, 255), e -> controller.showScreen(GameScreen.RANKING))); // 랭킹: 파란색 계열
        buttonPanel.add(createStyledButton("테마 설정", new Color(200, 100, 200), e -> controller.showScreen(GameScreen.THEME_SELECT))); // 테마: 보라색 계열
        buttonPanel.add(createStyledButton("게임 설명", new Color(0, 200, 200), e -> controller.showScreen(GameScreen.RULES))); // 설명: 청록색 계열
        buttonPanel.add(createStyledButton("나가기", new Color(200, 50, 50), e -> System.exit(0))); // 나가기: 빨간색
        
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    //투명 배경 + 테두리 버튼 생성 메서드
    private JButton createStyledButton(String text, Color color, java.awt.event.ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                // 마우스 오버 시에만 배경색 채우기
                if (getModel().isRollover()) {
                    g.setColor(color);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                super.paintComponent(g);
            }
        };
        
        // 폰트 및 기본 텍스트 색상
        button.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        button.setForeground(color);
        
        // 투명 처리
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        
        // 테두리 설정
        button.setBorder(BorderFactory.createLineBorder(color, 2));
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            action.actionPerformed(e);
        });
        
        // 마우스 호버 이벤트: 글자색 흰색으로 변경
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(color);
            }
        });
        
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