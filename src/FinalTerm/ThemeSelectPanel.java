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
        // 타이틀
        JLabel titleLabel = new JLabel("Skin / Theme", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0, 255, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // 테마 버튼 패널
        JPanel themesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        themesPanel.setBackground(new Color(26, 26, 26));
        themesPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        
        JButton classicButton = createThemeButton("Classic", new Color(0, 255, 0), "classic");
        JButton oceanButton = createThemeButton("Ocean", new Color(0, 212, 255), "ocean");
        JButton neonButton = createThemeButton("Neon", new Color(255, 0, 255), "neon");
        JButton forestButton = createThemeButton("Forest", new Color(144, 238, 144), "forest");
        
        themesPanel.add(classicButton);
        themesPanel.add(oceanButton);
        themesPanel.add(neonButton);
        themesPanel.add(forestButton);
        
        add(themesPanel, BorderLayout.CENTER);
        
        // 메인 메뉴 버튼
        JButton mainMenuButton = new JButton("Main Menu");
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 20));
        mainMenuButton.setBackground(new Color(51, 51, 51));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setBorderPainted(false);
        mainMenuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainMenuButton.addActionListener(e -> controller.showScreen(GameScreen.MAIN_MENU));
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(26, 26, 26));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        bottomPanel.add(mainMenuButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JButton createThemeButton(String name, Color themeColor, String themeKey) {
        JButton button = new JButton(name);
        button.setFont(new Font("Arial", Font.BOLD, 28));
        button.setBackground(new Color(51, 51, 51));
        button.setForeground(themeColor);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(themeColor, 3));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            themeManager.setTheme(themeKey);
            JOptionPane.showMessageDialog(this, "Theme changed to " + name + "!", "Theme Applied", JOptionPane.INFORMATION_MESSAGE);
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
