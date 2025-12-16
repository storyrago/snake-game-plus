package FinalTerm;

import javax.swing.*;
import java.awt.*;

public class GameSettingsPanel extends JPanel {
    private SnakeGameController controller;
    private JRadioButton easyButton, normalButton, hardButton;
    private JRadioButton classicButton, timeAttackButton;
    private JComboBox<String> timeLimitComboBox;
    private JLabel timeLimitLabel;
    private JLabel difficultyInfoLabel;
    
    public GameSettingsPanel(SnakeGameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(26, 26, 26));
        setPreferredSize(new Dimension(500, 680));
        
        initComponents();
    }
    
    private void initComponents() {
        // Ÿ��Ʋ
        JLabel titleLabel = new JLabel("게임 설정", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0, 255, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // ���� �г�
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(new Color(26, 26, 26));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        
        // ���̵� ����
        addSectionTitle(settingsPanel, "난이도");
        JPanel difficultyPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        difficultyPanel.setBackground(new Color(26, 26, 26));
        ButtonGroup difficultyGroup = new ButtonGroup();
        
        easyButton = createRadioButton("쉬움 (느림)");
        normalButton = createRadioButton("보통");
        hardButton = createRadioButton("어려움 (빠름)");
        
        difficultyGroup.add(easyButton);
        difficultyGroup.add(normalButton);
        difficultyGroup.add(hardButton);
        
        difficultyPanel.add(easyButton);
        difficultyPanel.add(normalButton);
        difficultyPanel.add(hardButton);
        
        normalButton.setSelected(true);
        
        settingsPanel.add(difficultyPanel);
        
        // ���̵� ���� �� (�ʱ⿡�� ����)
        difficultyInfoLabel = new JLabel("<html><i>쉬움: 90 초 | 보통: 60 초 | 어려움: 30 초</i></html>");
        difficultyInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        difficultyInfoLabel.setForeground(new Color(150, 150, 150));
        difficultyInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        difficultyInfoLabel.setVisible(false);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        settingsPanel.add(difficultyInfoLabel);
        
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // ���� ��� ����
        addSectionTitle(settingsPanel, "게임 모드");
        JPanel modePanel = new JPanel(new GridLayout(2, 1, 0, 10));
        modePanel.setBackground(new Color(26, 26, 26));
        ButtonGroup modeGroup = new ButtonGroup();
        
        classicButton = createRadioButton("클래식 모드");
        timeAttackButton = createRadioButton("타임 어택 모드");
        
        modeGroup.add(classicButton);
        modeGroup.add(timeAttackButton);
        
        modePanel.add(classicButton);
        modePanel.add(timeAttackButton);
        
        classicButton.setSelected(true);
        
        settingsPanel.add(modePanel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // �ð� ���� ����
        JPanel timeLimitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeLimitPanel.setBackground(new Color(26, 26, 26));
        
        timeLimitLabel = new JLabel("시간 제한: ");
        timeLimitLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timeLimitLabel.setForeground(Color.LIGHT_GRAY);
        timeLimitLabel.setVisible(false);
        
        String[] timeOptions = {"30 초", "60 초", "90 초"};
        timeLimitComboBox = new JComboBox<>(timeOptions);
        timeLimitComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        timeLimitComboBox.setPreferredSize(new Dimension(140, 30));
        timeLimitComboBox.setSelectedIndex(1); // �⺻��: 60��
        timeLimitComboBox.setVisible(false);
        
        timeLimitPanel.add(timeLimitLabel);
        timeLimitPanel.add(timeLimitComboBox);
        
        settingsPanel.add(timeLimitPanel);
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // ��庰 �ȳ� ��
        JLabel modeInfoLabel = new JLabel("<html><i>* 클래식: 난이도에 따라 이동속도가 변경됩니다! <br>" +
                                         "* 타임어택: 기본 속도를 가지는 대신, 제한 시간 내에 높은 점수에 도전하세요!</i></html>");
        modeInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        modeInfoLabel.setForeground(Color.GRAY);
        modeInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPanel.add(modeInfoLabel);
        
        // ��� ���� �̺�Ʈ
        timeAttackButton.addActionListener(e -> {
            // �ð� ���� ���
            easyButton.setEnabled(false);
            normalButton.setEnabled(false);
            hardButton.setEnabled(false);
            
            timeLimitLabel.setVisible(true);
            timeLimitComboBox.setVisible(true);
            difficultyInfoLabel.setVisible(true);
        });
        
        classicButton.addActionListener(e -> {
            // Ŭ���� ���
            easyButton.setEnabled(true);
            normalButton.setEnabled(true);
            hardButton.setEnabled(true);
            
            timeLimitLabel.setVisible(false);
            timeLimitComboBox.setVisible(false);
            difficultyInfoLabel.setVisible(false);
        });
        
        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBackground(new Color(26, 26, 26));
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
        // ��ư �г�
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(26, 26, 26));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        JButton startButton = new JButton("게임 시작");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBackground(new Color(0, 255, 0));
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.addActionListener(e -> {
            applySettings();
            controller.showScreen(GameScreen.PLAYING);
        });
        
        JButton backButton = new JButton("뒤로");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBackground(new Color(51, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> controller.showScreen(GameScreen.NAME_INPUT));
        
        buttonPanel.add(startButton);
        buttonPanel.add(backButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addSectionTitle(JPanel parent, String title) {
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        sectionLabel.setForeground(new Color(255, 215, 0));
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(sectionLabel);
        parent.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    private JRadioButton createRadioButton(String text) {
        JRadioButton button = new JRadioButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(26, 26, 26));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void applySettings() {
        // ��� ����
        if (classicButton.isSelected()) {
            GameSettings.setMode(GameMode.CLASSIC);
            
            // Ŭ���� ���: ���̵� ����
            if (easyButton.isSelected()) {
                GameSettings.setDifficulty(GameDifficulty.EASY);
            } else if (normalButton.isSelected()) {
                GameSettings.setDifficulty(GameDifficulty.NORMAL);
            } else if (hardButton.isSelected()) {
                GameSettings.setDifficulty(GameDifficulty.HARD);
            }
        } else if (timeAttackButton.isSelected()) {
            GameSettings.setMode(GameMode.TIME_ATTACK);
            
            // �ð� ���� ���: �ӵ��� �׻� Normal�� ����
            GameSettings.setDifficulty(GameDifficulty.NORMAL);
            
            // ���õ� �ð��� ���� ǥ�ÿ� ���̵� ����
            int selectedIndex = timeLimitComboBox.getSelectedIndex();
            int timeLimit;
            GameDifficulty displayDiff;
            
            switch (selectedIndex) {
                case 0: // 30��
                    timeLimit = 30;
                    displayDiff = GameDifficulty.HARD;
                    break;
                case 1: // 60��
                    timeLimit = 60;
                    displayDiff = GameDifficulty.NORMAL;
                    break;
                case 2: // 90��
                    timeLimit = 90;
                    displayDiff = GameDifficulty.EASY;
                    break;
                default:
                    timeLimit = 60;
                    displayDiff = GameDifficulty.NORMAL;
            }
            
            GameSettings.setTimeLimit(timeLimit);
            GameSettings.setDifficultyForDisplay(displayDiff);
            return;
        }
        
        // Ŭ���� ����� �⺻ �ð� ���� (������ ����)
        GameSettings.setTimeLimit(0);
    }
}
