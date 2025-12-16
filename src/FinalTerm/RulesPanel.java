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
        JLabel titleLabel = new JLabel("게임 설명", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0, 255, 0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new BoxLayout(rulesPanel, BoxLayout.Y_AXIS));
        rulesPanel.setBackground(new Color(26, 26, 26));
        rulesPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        addSection(rulesPanel, "조작법", new String[]{"방향키 - 뱀 이동", "R - 재시작", "T - 테마 변경", "M - 소리 켜기/끄기"});
        addSection(rulesPanel, "점수", new String[]{"사과 - 일반 먹이 (10점)", "황금사과 - 레어 먹이 (30점)"});
        addSection(rulesPanel, "특수 아이템", new String[]{"포탈 (반대 포탈로 이동)", "회색 사각형 - 장애물 (망치로 파괴 가능)", "H - 망치", 
        		"? - 랜덤박스", "     긍정효과: 이동속도 감소, 점수 두배, 꼬리 자르기", "     부정효과: 이동속도 증가, 시야 축소, 방향키 뒤바꿈"});
        
        JScrollPane scrollPane = new JScrollPane(rulesPanel);
        scrollPane.setBackground(new Color(26, 26, 26));
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
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
    
    private void addSection(JPanel parent, String title, String[] items) {
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        sectionTitle.setForeground(new Color(255, 215, 0));
        parent.add(sectionTitle);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
        
        for (String item : items) {
            JLabel itemLabel = new JLabel("  " + item);
            itemLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
            itemLabel.setForeground(Color.WHITE);
            parent.add(itemLabel);
            parent.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        parent.add(Box.createRigidArea(new Dimension(0, 20)));
    }
}