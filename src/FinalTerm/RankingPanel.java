package FinalTerm;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class RankingPanel extends JPanel {
    private SnakeGameController controller;
    private ScoreManager scoreManager;
    private JTabbedPane modeTabbedPane;
    
    // �� ���� ���̵��� �г��� ����
    private Map<String, JPanel> scorePanels;
    
    public RankingPanel(SnakeGameController controller, ScoreManager scoreManager) {
        this.controller = controller;
        this.scoreManager = scoreManager;
        this.scorePanels = new HashMap<>();
        
        setLayout(new BorderLayout());
        setBackground(new Color(26, 26, 26));
        
        initComponents();
    }
    
    private void initComponents() {
        // Ÿ��Ʋ
        JLabel titleLabel = new JLabel("랭킹", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // ��� ��
        modeTabbedPane = new JTabbedPane();
        modeTabbedPane.setFont(new Font("Arial", Font.BOLD, 16));
        modeTabbedPane.setBackground(new Color(26, 26, 26));
        modeTabbedPane.setForeground(Color.WHITE);
        
        // Classic Mode ��
        JTabbedPane classicDifficultyTabs = createDifficultyTabs(GameMode.CLASSIC);
        modeTabbedPane.addTab("클래식 모드", classicDifficultyTabs);
        
        // Time Attack ��
        JTabbedPane timeAttackDifficultyTabs = createDifficultyTabs(GameMode.TIME_ATTACK);
        modeTabbedPane.addTab("타임 어택", timeAttackDifficultyTabs);
        
        add(modeTabbedPane, BorderLayout.CENTER);
        
        // �ϴ� ��ư
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setBackground(new Color(26, 26, 26));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JButton clearButton = new JButton("랭킹 기록 지우기");
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setPreferredSize(new Dimension(180, 40));
        
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "모든 랭킹을 지우시겠습니까?\n다시 되돌릴 수 없습니다!", 
                "랭킹 지우기", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                scoreManager.clearScores();
                updateScores();
                JOptionPane.showMessageDialog(this, "모든 랭킹이 지워졌습니다!", "Success", 
                                            JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(new Color(200, 35, 51));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clearButton.setBackground(new Color(220, 53, 69));
            }
        });
        
        JButton mainMenuButton = new JButton("메인 메뉴");
        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 16));
        mainMenuButton.setBackground(new Color(51, 51, 51));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setBorderPainted(false);
        mainMenuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainMenuButton.setPreferredSize(new Dimension(130, 40));
        mainMenuButton.addActionListener(e -> controller.showScreen(GameScreen.MAIN_MENU));
        
        mainMenuButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mainMenuButton.setBackground(new Color(0, 255, 0));
                mainMenuButton.setForeground(Color.BLACK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mainMenuButton.setBackground(new Color(51, 51, 51));
                mainMenuButton.setForeground(Color.WHITE);
            }
        });
        
        bottomPanel.add(clearButton);
        bottomPanel.add(mainMenuButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        updateScores();
    }
    
    private JTabbedPane createDifficultyTabs(GameMode mode) {
        JTabbedPane difficultyTabs = new JTabbedPane();
        difficultyTabs.setFont(new Font("Arial", Font.PLAIN, 14));
        difficultyTabs.setBackground(new Color(26, 26, 26));
        
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            JPanel scorePanel = createScoresPanel();
            
            // �г��� �ʿ� ����
            String key = mode.name() + "_" + difficulty.name();
            scorePanels.put(key, scorePanel);
            
            JScrollPane scrollPane = new JScrollPane(scorePanel);
            scrollPane.setBackground(new Color(26, 26, 26));
            scrollPane.setBorder(null);
            
            difficultyTabs.addTab(difficulty.getDisplayName(), scrollPane);
        }
        
        return difficultyTabs;
    }
    
    private JPanel createScoresPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(26, 26, 26));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        return panel;
    }
    
    public void updateScores() {
        // Classic Mode�� ��� ���̵� ������Ʈ
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            String key = GameMode.CLASSIC.name() + "_" + difficulty.name();
            JPanel panel = scorePanels.get(key);
            if (panel != null) {
                updateScoresForDifficulty(panel, GameMode.CLASSIC, difficulty);
            }
        }
        
        // Time Attack Mode�� ��� ���̵� ������Ʈ
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            String key = GameMode.TIME_ATTACK.name() + "_" + difficulty.name();
            JPanel panel = scorePanels.get(key);
            if (panel != null) {
                updateScoresForDifficulty(panel, GameMode.TIME_ATTACK, difficulty);
            }
        }
    }
    
    private void updateScoresForDifficulty(JPanel panel, GameMode mode, GameDifficulty difficulty) {
        panel.removeAll();
        
        List<ScoreManager.ScoreRecord> scores = scoreManager.getHighScores(mode, difficulty);
        
        if (scores.isEmpty()) {
            JLabel noScoresLabel = new JLabel("No records yet!");
            noScoresLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            noScoresLabel.setForeground(Color.GRAY);
            noScoresLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createRigidArea(new Dimension(0, 50)));
            panel.add(noScoresLabel);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                ScoreManager.ScoreRecord record = scores.get(i);
                
                JPanel scorePanel = new JPanel(new BorderLayout());
                scorePanel.setBackground(new Color(40, 40, 40));
                scorePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                scorePanel.setMaximumSize(new Dimension(600, 65));
                
                // ����
                JLabel rankLabel = new JLabel((i + 1) + ".");
                rankLabel.setFont(new Font("Arial", Font.BOLD, 20));
                if (i == 0) {
                    rankLabel.setForeground(new Color(255, 215, 0));  // Gold
                } else if (i == 1) {
                    rankLabel.setForeground(new Color(192, 192, 192));  // Silver
                } else if (i == 2) {
                    rankLabel.setForeground(new Color(205, 127, 50));  // Bronze
                } else {
                    rankLabel.setForeground(Color.WHITE);
                }
                
                JLabel nameLabel = new JLabel(record.playerName);
                nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
                nameLabel.setForeground(Color.WHITE);
                
                JLabel scoreLabel = new JLabel(record.score + " pts");
                scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
                scoreLabel.setForeground(new Color(0, 255, 0));
                
                JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                leftPanel.setBackground(new Color(40, 40, 40));
                leftPanel.add(rankLabel);
                leftPanel.add(nameLabel);
                
                scorePanel.add(leftPanel, BorderLayout.WEST);
                scorePanel.add(scoreLabel, BorderLayout.EAST);
                
                panel.add(scorePanel);
                panel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }
        
        panel.revalidate();
        panel.repaint();
    }
}
