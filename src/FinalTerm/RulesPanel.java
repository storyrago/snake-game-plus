package FinalTerm;

import javax.swing.*;
import java.awt.*;

public class RulesPanel extends JPanel {
    private SnakeGameController controller;
    
    public RulesPanel(SnakeGameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(26, 26, 26));
        
        initComponents();
    }
    
    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("Game Rules", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0, 255, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Rules panel
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setBackground(new Color(26, 26, 26));
        rulesPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        addSection(rulesPanel, "Controls", new String[]{
            "Arrow Keys - Move the snake",
            "R - Restart game",
            "T - Change theme",
            "M - Mute/Unmute sound",
            "Space - Pause game"
        });
        
        addSection(rulesPanel, "Food", new String[]{
            "Red Circle - Normal Food (10 points)",
            "Gold Circle - Rare Food (30 points)",
            "Combo - Eat food within 2 seconds for bonus points"
        });
        
        addSection(rulesPanel, "Special Items", new String[]{
            "Purple/Cyan Circles - Portals (teleport between them)",
            "Gray Squares - Obstacles (avoid or destroy with hammer)",
            "H - Hammer (destroys one obstacle)",
            "S - Scissors (cuts 30% of tail length)"
        });
        
        addSection(rulesPanel, "Objective", new String[]{
            "Eat food to grow longer and score points",
            "Avoid hitting walls, obstacles, and yourself",
            "Collect special items for advantages",
            "Try to achieve the highest score!"
        });
        
        JScrollPane scrollPane = new JScrollPane(rulesPanel);
        scrollPane.setBackground(new Color(26, 26, 26));
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
        // Main menu button
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
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bottomPanel.add(mainMenuButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void addSection(JPanel parent, String title, String[] items) {
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 22));
        sectionTitle.setForeground(new Color(255, 215, 0));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(sectionTitle);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        for (String item : items) {
            JLabel itemLabel = new JLabel("  " + item);
            itemLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            itemLabel.setForeground(Color.WHITE);
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            parent.add(itemLabel);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        parent.add(Box.createRigidArea(new Dimension(0, 20)));
    }
}
