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
        JLabel titleLabel = new JLabel("스킨 / 테마", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0, 255, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel themesPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        themesPanel.setBackground(new Color(26, 26, 26));
        themesPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        
        themesPanel.add(createThemeButton("클래식", new Color(0, 255, 0), "classic"));
        themesPanel.add(createThemeButton("오션", new Color(0, 212, 255), "ocean"));
        themesPanel.add(createThemeButton("네온", new Color(255, 0, 255), "neon"));
        themesPanel.add(createThemeButton("포레스트", new Color(144, 238, 144), "forest"));
        
        add(themesPanel, BorderLayout.CENTER);
        
        JButton mainMenuButton = new JButton("메인 메뉴");
        mainMenuButton.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        mainMenuButton.setBackground(new Color(51, 51, 51));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            controller.showScreen(GameScreen.MAIN_MENU);
        });
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(26, 26, 26));
        bottomPanel.add(mainMenuButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JButton createThemeButton(String name, Color color, String key) {
        JButton button = new JButton(name);
        button.setFont(new Font("Malgun Gothic", Font.BOLD, 28));
        button.setBackground(new Color(51, 51, 51));
        button.setForeground(color);
        button.setBorder(BorderFactory.createLineBorder(color, 3));
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            themeManager.setTheme(key);
            JOptionPane.showMessageDialog(this, name + " 테마가 적용되었습니다!");
        });
        return button;
    }
}