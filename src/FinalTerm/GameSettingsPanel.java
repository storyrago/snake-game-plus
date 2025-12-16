package FinalTerm;

import javax.swing.*;
import java.awt.*;

public class GameSettingsPanel extends GradientPanel {
    private SnakeGameController controller;
    
    public GameSettingsPanel(SnakeGameController controller) {
        super(new Color(20, 20, 40), new Color(40, 40, 60), true);
        this.controller = controller;
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel title = new JLabel("게임 설정");
        title.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(title);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // 난이도 설정
        addSettingSection(centerPanel, "난이도", new String[]{"EASY", "NORMAL", "HARD"}, 1);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // 모드 설정
        addSettingSection(centerPanel, "게임 모드", new String[]{"CLASSIC", "TIME_ATTACK"}, 0);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        
        JButton startBtn = new JButton("게임 시작!");
        startBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        startBtn.setBackground(new Color(0, 200, 0));
        startBtn.setForeground(Color.WHITE);
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setMaximumSize(new Dimension(200, 60));
        startBtn.addActionListener(e -> controller.showScreen(GameScreen.PLAYING));
        
        centerPanel.add(startBtn);
        add(centerPanel, BorderLayout.CENTER);
        
        JButton backBtn = new JButton("취소");
        backBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        backBtn.addActionListener(e -> controller.showScreen(GameScreen.NAME_INPUT));
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);
    }
    
    private void addSettingSection(JPanel parent, String title, String[] options, int type) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        label.setForeground(Color.GREEN);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(label);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        ButtonGroup group = new ButtonGroup();
        
        for (String opt : options) {
            JRadioButton rb = new JRadioButton(opt);
            rb.setFont(new Font("Arial", Font.BOLD, 16));
            rb.setForeground(Color.WHITE);
            rb.setOpaque(false);
            if (opt.equals("NORMAL") || opt.equals("CLASSIC")) rb.setSelected(true);
            
            rb.addActionListener(e -> {
                if (type == 1) GameSettings.setDifficulty(GameDifficulty.valueOf(opt));
                else GameSettings.setMode(GameMode.valueOf(opt));
            });
            
            group.add(rb);
            btnPanel.add(rb);
        }
        parent.add(btnPanel);
    }
}