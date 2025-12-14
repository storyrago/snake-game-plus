package FinalTerm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int CELL_SIZE = 25;
    
    private SnakeGameController controller;
    private GameState gameState;
    private GameRenderer renderer;
    private ThemeManager themeManager;
    private SoundManager soundManager;
    private ScoreManager scoreManager;
    private InputHandler inputHandler;
    
    private Timer gameTimer;
    private int delay;
    private int baseDelay;
    private boolean running;
    private boolean showResults;
    
    private JButton restartButton;
    private JButton menuButton;
    
    private String currentEffectMessage = "";
    private Timer effectMessageTimer;
    
    private int screenWidth;
    private int screenHeight;
    
    public SnakeGame(SnakeGameController controller, ThemeManager themeManager, 
                     SoundManager soundManager, ScoreManager scoreManager) {
        this.controller = controller;
        this.themeManager = themeManager;
        this.soundManager = soundManager;
        this.scoreManager = scoreManager;
        
        int gridSize = GameBoard.getGridSize();
        screenWidth = gridSize * CELL_SIZE;
        screenHeight = gridSize * CELL_SIZE;
        
        setPreferredSize(new Dimension(screenWidth, screenHeight + 180));
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);
        
        gameState = new GameState();
        renderer = new GameRenderer();
        inputHandler = new InputHandler(this);
        
        addKeyListener(inputHandler);
        
        gameTimer = new Timer(100, this);
        initGameOverButtons();
    }
    
    private void initGameOverButtons() {
        restartButton = createStyledButton("Try Again", new Color(0, 200, 0));
        restartButton.setBounds(screenWidth / 2 - 160, screenHeight - 80, 150, 45);
        restartButton.addActionListener(e -> { restart(); requestFocusInWindow(); });
        add(restartButton);
        
        menuButton = createStyledButton("Main Menu", new Color(200, 50, 50));
        menuButton.setBounds(screenWidth / 2 + 10, screenHeight - 80, 150, 45);
        menuButton.addActionListener(e -> { controller.showScreen(GameScreen.MAIN_MENU); hideButtons(); });
        add(menuButton);
    }
    
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setVisible(false);
        button.setFocusable(false);
        UIAnimationHelper.addHoverEffect(button, baseColor, baseColor.brighter(), Color.WHITE, Color.WHITE);
        return button;
    }
    
    private void showButtons() { restartButton.setVisible(true); menuButton.setVisible(true); UIAnimationHelper.fadeIn(restartButton, 500); UIAnimationHelper.fadeIn(menuButton, 500); }
    private void hideButtons() { restartButton.setVisible(false); menuButton.setVisible(false); }

    public void startNewGame() {
        hideButtons();
        gameState.reset();
        GameDifficulty difficulty = GameSettings.getDifficulty();
        baseDelay = difficulty.getInitialDelay();
        delay = baseDelay;
        running = true;
        showResults = false;
        gameTimer.setDelay(delay);
        gameTimer.start();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) { update(); repaint(); }
    }

    private void update() {
        if (GameSettings.getMode() == GameMode.TIME_ATTACK && gameState.isTimeUp(GameSettings.getTimeLimit())) {
            gameOver(); return;
        }
        UpdateResult result = gameState.update(null);
        updateGameSpeed();
        handleUpdateResult(result);
        
        if (gameState.getSnake().getLength() % 5 == 0) {
            GameDifficulty difficulty = GameSettings.getDifficulty();
            int lengthBoost = (gameState.getSnake().getLength() / 5) * 10;
            int newBaseDelay = Math.max(difficulty.getMinDelay(), difficulty.getInitialDelay() - lengthBoost);
            if (newBaseDelay != baseDelay) {
                baseDelay = newBaseDelay;
                if (gameState.getSpeedEffect() == 0) gameTimer.setDelay(baseDelay);
            }
        }
    }

    private void updateGameSpeed() {
        int speedEffect = gameState.getSpeedEffect();
        if (speedEffect == 1) gameTimer.setDelay((int)(baseDelay * 0.6));
        else if (speedEffect == -1) gameTimer.setDelay((int)(baseDelay * 1.5));
        else gameTimer.setDelay(baseDelay);
    }

    private void handleUpdateResult(UpdateResult result) {
        if (result.getType() == UpdateResult.Type.ITEM_COLLECTED) {
             // 룰렛 시작
             soundManager.playSound(600, 100);
        } else if (result.getType() == UpdateResult.Type.ROULETTE_FINISHED) {
             // [수정] 룰렛 종료: 긍정/부정에 따른 사운드 분기
             if (result.isPositive()) {
                 // 긍정: 높은음 (딩동댕 느낌)
                 soundManager.playSound(800, 100); 
                 Timer t = new Timer(100, e -> soundManager.playSound(1000, 200));
                 t.setRepeats(false); t.start();
             } else {
                 // 부정: 낮은 경고음 (삐-)
                 soundManager.playSound(300, 400); 
             }
             showEffectMessage(result.getMessage());
        }

        switch (result.getType()) {
            case FOOD_EATEN: soundManager.playSound(523 + (result.getCombo() * 50), 100); break;
            case OBSTACLE_DESTROYED: soundManager.playSound(600, 100); break;
            case PORTAL_USED: soundManager.playSound(800, 100); gameState.clearPortalCooldown(); break;
            case WALL_COLLISION: case SELF_COLLISION: case OBSTACLE_COLLISION: gameOver(); break;
        }
    }

    private void showEffectMessage(String msg) {
        if (msg == null) return;
        currentEffectMessage = msg;
        if (effectMessageTimer != null && effectMessageTimer.isRunning()) effectMessageTimer.stop();
        effectMessageTimer = new Timer(2000, e -> { currentEffectMessage = ""; repaint(); });
        effectMessageTimer.setRepeats(false);
        effectMessageTimer.start();
    }

    private void gameOver() {
        running = false;
        showResults = true;
        gameTimer.stop();
        soundManager.playSound(200, 500);
        scoreManager.addScore(PlayerData.getPlayerName(), GameSettings.getMode(), GameSettings.getDisplayDifficulty(), 
                              gameState.getScore(), gameState.getMaxCombo(), gameState.getPlayTime(), gameState.getFoodEaten(), gameState.getItemsCollected());
        repaint();
        Timer t = new Timer(500, e -> showButtons());
        t.setRepeats(false); t.start();
    }

    public void restart() { startNewGame(); }
    public void cycleTheme() { themeManager.cycleTheme(); repaint(); }
    public void toggleSound() { soundManager.toggle(); }
    public void handleEnter() { if (showResults) { controller.showScreen(GameScreen.MAIN_MENU); hideButtons(); } }

    public void handleDirection(Point direction) {
        if (running) {
            Point target = direction;
            if (gameState.isReverseInputActive()) target = new Point(-direction.x, -direction.y);
            gameState.getSnake().queueDirection(target);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render((Graphics2D) g, gameState, themeManager, screenWidth, screenHeight, showResults, scoreManager, currentEffectMessage);
    }
}