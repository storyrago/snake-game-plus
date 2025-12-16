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
        // [한글화]
        JLabel titleLabel = new JLabel("게임 설명", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0, 255, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setBackground(new Color(26, 26, 26));
        rulesPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // [한글화] 규칙 내용
        addSection(rulesPanel, "조작법", new String[]{
            "방향키 - 뱀 이동",
            "R - 재시작",
            "T - 테마 변경",
            "M - 소리 켜기/끄기",
            "Space - 일시 정지"
        });
        
        addSection(rulesPanel, "점수", new String[]{
            "빨간 원 - 일반 먹이 (10점)",
            "황금 원 - 레어 먹이 (30점)",
            "콤보 - 2초 내에 연속 획득 시 추가 점수"
        });
        
        addSection(rulesPanel, "특수 아이템", new String[]{
            "보라/청록 원 - 포탈 (서로 이동 가능)",
            "회색 사각형 - 장애물 (피하거나 망치로 파괴)",
            "H - 망치 (장애물 1회 파괴)",
            "S - 가위 (꼬리 길이 30% 자르기)",
            "? - 랜덤 박스 (룰렛 효과)"
        });
        
        addSection(rulesPanel, "목표", new String[]{
            "먹이를 먹어 뱀을 길게 만들고 점수를 얻으세요.",
            "벽, 장애물, 자기 자신과 부딪히면 안 됩니다.",
            "아이템을 활용해 최고 점수에 도전하세요!"
        });
        
        JScrollPane scrollPane = new JScrollPane(rulesPanel);
        scrollPane.setBackground(new Color(26, 26, 26));
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
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
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bottomPanel.add(mainMenuButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void addSection(JPanel parent, String title, String[] items) {
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        sectionTitle.setForeground(new Color(255, 215, 0));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(sectionTitle);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        for (String item : items) {
            JLabel itemLabel = new JLabel("  " + item);
            itemLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
            itemLabel.setForeground(Color.WHITE);
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            parent.add(itemLabel);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        parent.add(Box.createRigidArea(new Dimension(0, 20)));
    }
}