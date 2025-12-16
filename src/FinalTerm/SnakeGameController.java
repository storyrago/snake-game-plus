// SnakeGameController.java
package FinalTerm;

import javax.swing.*;
import java.awt.*;

public class SnakeGameController {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private ThemeManager themeManager;
    private SoundManager soundManager;
    private ScoreManager scoreManager;
    
    private MainMenuPanel mainMenuPanel;
    private NameInputPanel nameInputPanel;
    private GameSettingsPanel settingsPanel;
    private SnakeGame snakeGame;
    private RankingPanel rankingPanel;
    private RulesPanel rulesPanel;
    private ThemeSelectPanel themeSelectPanel;
    
    public SnakeGameController() {
        themeManager = new ThemeManager();
        soundManager = new SoundManager();
        scoreManager = new ScoreManager();
        
        frame = new JFrame("Snake Game+");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        initPanels();
        
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        showScreen(GameScreen.MAIN_MENU);
    }
    
    private void initPanels() {
        Dimension panelSize = new Dimension(500, 680);
        
        mainMenuPanel = new MainMenuPanel(this);
        mainMenuPanel.setPreferredSize(panelSize);
        
        nameInputPanel = new NameInputPanel(this);
        nameInputPanel.setPreferredSize(panelSize);
        
        settingsPanel = new GameSettingsPanel(this);
        settingsPanel.setPreferredSize(panelSize);
        
        snakeGame = new SnakeGame(this, themeManager, soundManager, scoreManager);
        
        rankingPanel = new RankingPanel(this, scoreManager);
        rankingPanel.setPreferredSize(panelSize);
        
        rulesPanel = new RulesPanel(this);
        rulesPanel.setPreferredSize(panelSize);
        
        themeSelectPanel = new ThemeSelectPanel(this, themeManager);
        themeSelectPanel.setPreferredSize(panelSize);
        
        mainPanel.add(mainMenuPanel, GameScreen.MAIN_MENU.name());
        mainPanel.add(nameInputPanel, GameScreen.NAME_INPUT.name());
        mainPanel.add(settingsPanel, GameScreen.SETTINGS.name());
        mainPanel.add(snakeGame, GameScreen.PLAYING.name());
        mainPanel.add(rankingPanel, GameScreen.RANKING.name());
        mainPanel.add(rulesPanel, GameScreen.RULES.name());
        mainPanel.add(themeSelectPanel, GameScreen.THEME_SELECT.name());
    }
    
    public void showScreen(GameScreen screen) {
        cardLayout.show(mainPanel, screen.name());
        frame.pack();
        
        //화면에 따른 배경음악 교체 로직
        if (screen == GameScreen.PLAYING) {
            // 게임 화면으로 진입 시: 메인 BGM 끄고 게임 BGM 재생
            soundManager.stopClip("bgm");
            soundManager.loopClip("game_bgm");
        } else {
            // 그 외 화면(메뉴, 랭킹 등) 진입 시: 게임 BGM 끄고 메인 BGM 재생
            soundManager.stopClip("game_bgm");
            soundManager.loopClip("bgm");
        }
        
        // 화면별 로직
        switch (screen) {
            case PLAYING:
                snakeGame.startNewGame();
                snakeGame.requestFocusInWindow();
                break;
            case NAME_INPUT:
                nameInputPanel.focusNameField();
                break;
            case RANKING:
                rankingPanel.updateScores();
                break;
        }
    }
    
    public SoundManager getSoundManager() { return soundManager; }
    
    public void show() { frame.setVisible(true); }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SnakeGameController().show();
        });
    }
}