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
        JLabel titleLabel = new JLabel("랭킹", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        modeTabbedPane = new JTabbedPane();
        modeTabbedPane.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        modeTabbedPane.setBackground(new Color(26, 26, 26));
        modeTabbedPane.setForeground(Color.WHITE);
        
        modeTabbedPane.addTab("클래식 모드", createDifficultyTabs(GameMode.CLASSIC));
        modeTabbedPane.addTab("타임 어택", createDifficultyTabs(GameMode.TIME_ATTACK));
        
        add(modeTabbedPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setBackground(new Color(26, 26, 26));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JButton clearButton = new JButton("랭킹 기록 지우기");
        clearButton.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        clearButton.setBackground(new Color(220, 53, 69));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "모든 랭킹을 지우시겠습니까?", "랭킹 지우기", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                scoreManager.clearScores();
                updateScores();
            }
        });
        
        JButton mainMenuButton = new JButton("메인 메뉴");
        mainMenuButton.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        mainMenuButton.setBackground(new Color(51, 51, 51));
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.addActionListener(e -> controller.showScreen(GameScreen.MAIN_MENU));
        
        bottomPanel.add(clearButton);
        bottomPanel.add(mainMenuButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        updateScores();
    }
    
    private JTabbedPane createDifficultyTabs(GameMode mode) {
        JTabbedPane difficultyTabs = new JTabbedPane();
        difficultyTabs.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        difficultyTabs.setBackground(new Color(26, 26, 26));
        
        for (GameDifficulty difficulty : GameDifficulty.values()) {
            JPanel scorePanel = new JPanel();
            scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
            scorePanel.setBackground(new Color(26, 26, 26));
            scorePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            
            scorePanels.put(mode.name() + "_" + difficulty.name(), scorePanel);
            
            JScrollPane scrollPane = new JScrollPane(scorePanel);
            scrollPane.setBackground(new Color(26, 26, 26));
            scrollPane.setBorder(null);
            
            difficultyTabs.addTab(difficulty.getDisplayName(), scrollPane);
        }
        return difficultyTabs;
    }
    
    public void updateScores() {
        for (GameMode mode : GameMode.values()) {
            for (GameDifficulty diff : GameDifficulty.values()) {
                JPanel panel = scorePanels.get(mode.name() + "_" + diff.name());
                if (panel != null) updatePanel(panel, mode, diff);
            }
        }
    }
    
    private void updatePanel(JPanel panel, GameMode mode, GameDifficulty diff) {
        panel.removeAll();
        List<ScoreManager.ScoreRecord> scores = scoreManager.getHighScores(mode, diff);
        
        if (scores.isEmpty()) {
            JLabel label = new JLabel("기록이 없습니다.");
            label.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
            label.setForeground(Color.GRAY);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                ScoreManager.ScoreRecord r = scores.get(i);
                JPanel p = new JPanel(new BorderLayout());
                p.setBackground(new Color(40, 40, 40));
                p.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                p.setMaximumSize(new Dimension(600, 60));
                
                JLabel rank = new JLabel((i + 1) + ". ");
                rank.setFont(new Font("Arial", Font.BOLD, 20));
                rank.setForeground(i == 0 ? Color.YELLOW : Color.WHITE);
                
                JLabel name = new JLabel(r.playerName);
                name.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
                name.setForeground(Color.WHITE);
                
                JLabel score = new JLabel(r.score + " pts");
                score.setFont(new Font("Arial", Font.BOLD, 18));
                score.setForeground(Color.GREEN);
                
                JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
                left.setOpaque(false);
                left.add(rank);
                left.add(name);
                
                p.add(left, BorderLayout.WEST);
                p.add(score, BorderLayout.EAST);
                panel.add(p);
                panel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        panel.revalidate();
        panel.repaint();
    }
}