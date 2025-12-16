package FinalTerm;

import javax.swing.*;
import java.awt.*;

public class ThemeSelectPanel extends JPanel {
    private SnakeGameController controller;
    private ThemeManager themeManager;
    
    public ThemeSelectPanel(SnakeGameController controller, ThemeManager themeManager) {
        this.controller = controller;
        this.themeManager = themeManager;
        setLayout(new BorderLayout());
        setBackground(new Color(26, 26, 26));
        initComponents();
    }
    
    private void initComponents() {
        // [한글화]
        JLabel titleLabel = new JLabel("스킨 / 테마", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0, 255, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel themesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        themesPanel.setBackground(new Color(26, 26, 26));
        themesPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        
        JButton classicButton = createThemeButton("클래식", new Color(0, 255, 0), "classic");
        JButton oceanButton = createThemeButton("오션", new Color(0, 212, 255), "ocean");
        JButton neonButton = createThemeButton("네온", new Color(255, 0, 255), "neon");
        JButton forestButton = createThemeButton("포레스트", new Color(144, 238, 144), "forest");
        
        themesPanel.add(classicButton);
        themesPanel.add(oceanButton);
        themesPanel.add(neonButton);
        themesPanel.add(forestButton);
        
        add(themesPanel, BorderLayout.CENTER);
        
        JButton mainMenuButton = new JButton("메인 메뉴");
        mainMenuButton.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        mainMenuButton.setBackground(new Color(51, 51, 51));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setBorderPainted(false);
        mainMenuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        mainMenuButton.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            controller.showScreen(GameScreen.MAIN_MENU);
        });
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(26, 26, 26));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        bottomPanel.add(mainMenuButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JButton createThemeButton(String name, Color themeColor, String themeKey) {
        JButton button = new JButton(name);
        button.setFont(new Font("Malgun Gothic", Font.BOLD, 28));
        button.setBackground(new Color(51, 51, 51));
        button.setForeground(themeColor);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(themeColor, 3));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            themeManager.setTheme(themeKey);
            JOptionPane.showMessageDialog(this, name + " 테마가 적용되었습니다!", "테마 변경", JOptionPane.INFORMATION_MESSAGE);
        });
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(themeColor);
                button.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 51, 51));
                button.setForeground(themeColor);
            }
        });
        return button;
    }
}