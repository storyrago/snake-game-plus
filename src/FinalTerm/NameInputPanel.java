package FinalTerm;

import javax.swing.*;
import java.awt.*;

public class NameInputPanel extends GradientPanel {
    private SnakeGameController controller;
    private JTextField nameField;
    
    public NameInputPanel(SnakeGameController controller) {
        // 배경색 그라데이션 설정
        super(new Color(15, 15, 35), new Color(35, 35, 55), true);
        this.controller = controller;
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(120, 80, 120, 80));
        
        JLabel iconLabel = new JLabel("SNAKE", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 48)); // 영문 타이틀
        iconLabel.setForeground(new Color(0, 255, 0));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel("이름을 입력하세요");
        instructionLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 32));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        nameField = new JTextField(PlayerData.getPlayerName());
        nameField.setFont(new Font("Malgun Gothic", Font.PLAIN, 24));
        nameField.setMaximumSize(new Dimension(380, 55));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setBackground(new Color(40, 40, 60));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(new Color(0, 255, 0));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 0), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JButton startButton = new JButton("게임 시작");
        startButton.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        startButton.setBackground(new Color(0, 180, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 0), 2),
            BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(250, 55));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 이벤트 리스너
        startButton.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            startGame();
        });
        nameField.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            startGame();
        });
        
        // 버튼 호버 효과
        UIAnimationHelper.addHoverEffect(startButton, new Color(0, 180, 0), new Color(0, 255, 0), Color.WHITE, Color.BLACK);
        
        centerPanel.add(iconLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(nameField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(startButton);
        
        add(centerPanel, BorderLayout.CENTER);
        
        JButton backButton = createBackButton();
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void startGame() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            shakeComponent(nameField); // 이름 없으면 흔들기
            return;
        }
        PlayerData.setPlayerName(name);
        controller.showScreen(GameScreen.SETTINGS);
    }
    
    private void shakeComponent(JComponent component) {
        Point originalLocation = component.getLocation();
        Timer timer = new Timer(50, null);
        final int[] count = {0};
        
        timer.addActionListener(e -> {
            if (count[0] < 6) {
                int offset = (count[0] % 2 == 0) ? 10 : -10;
                component.setLocation(originalLocation.x + offset, originalLocation.y);
                count[0]++;
            } else {
                component.setLocation(originalLocation);
                timer.stop();
            }
        });
        timer.start();
    }
    
    private JButton createBackButton() {
        JButton button = new JButton("< 뒤로");
        button.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
        button.setBackground(new Color(60, 60, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            controller.getSoundManager().playClip("click");
            controller.showScreen(GameScreen.MAIN_MENU);
        });
        
        UIAnimationHelper.addHoverEffect(button, new Color(60, 60, 80), new Color(100, 100, 120), Color.WHITE, new Color(0, 255, 0));
        return button;
    }
    
    public void focusNameField() {
        if (nameField != null) {
            nameField.requestFocusInWindow();
            nameField.selectAll();
        }
    }
}