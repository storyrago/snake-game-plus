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
    
    private Timer gameTimer;   // 로직용
    private Timer renderTimer; // 렌더링용
    private Timer buttonShowTimer; // [추가] 버튼 딜레이 표시용 타이머
    
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
        renderTimer = new Timer(16, e -> repaint());
        
        initGameOverButtons();
    }
    
    private void initGameOverButtons() {
        restartButton = createStyledButton("Try Again (Enter)", new Color(0, 200, 0));
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
        button.setFont(new Font("Arial", Font.BOLD, 14));
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
    
    private void showButtons() { 
        restartButton.setVisible(true); 
        menuButton.setVisible(true); 
        UIAnimationHelper.fadeIn(restartButton, 500); 
        UIAnimationHelper.fadeIn(menuButton, 500); 
    }
    
    // [수정] 버튼 숨길 때, 혹시 돌고 있을지 모를 '버튼 표시 타이머'를 취소함
    private void hideButtons() { 
        if (buttonShowTimer != null && buttonShowTimer.isRunning()) {
            buttonShowTimer.stop();
        }
        restartButton.setVisible(false); 
        menuButton.setVisible(false); 
    }

    public void startNewGame() {
        hideButtons(); // 여기서 타이머도 같이 취소됨
        gameState.reset();
        
        GameDifficulty difficulty = GameSettings.getDifficulty();
        baseDelay = difficulty.getInitialDelay();
        delay = baseDelay;
        
        running = true;
        showResults = false;
        
        gameTimer.setDelay(delay);
        gameTimer.start();
        renderTimer.start();
        
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) { update(); }
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
             soundManager.playSound(600, 100);
        } else if (result.getType() == UpdateResult.Type.ROULETTE_FINISHED) {
             if (result.isPositive()) {
                 soundManager.playSound(800, 100); 
                 Timer t = new Timer(100, e -> soundManager.playSound(1000, 200));
                 t.setRepeats(false); t.start();
             } else {
                 soundManager.playSound(300, 400); 
             }
             showEffectMessage(result.getMessage());
        } else if (result.getType() == UpdateResult.Type.HAMMER_COLLECTED) {
             soundManager.playSound(750, 150);
             showEffectMessage(result.getMessage());
        } else if (result.getType() == UpdateResult.Type.OBSTACLE_DESTROYED) {
             soundManager.playSound(200, 200);
             showEffectMessage(result.getMessage());
        }

        switch (result.getType()) {
            case FOOD_EATEN: soundManager.playSound(523 + (result.getCombo() * 50), 100); break;
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
        renderTimer.stop();
        
        soundManager.playSound(200, 500);
        scoreManager.addScore(PlayerData.getPlayerName(), GameSettings.getMode(), GameSettings.getDisplayDifficulty(), 
                              gameState.getScore(), gameState.getMaxCombo(), gameState.getPlayTime(), gameState.getFoodEaten(), gameState.getItemsCollected());
        
        repaint();
        
        // [수정] 전역 변수 buttonShowTimer에 할당하여 제어 가능하게 함
        if (buttonShowTimer != null && buttonShowTimer.isRunning()) {
            buttonShowTimer.stop();
        }
        buttonShowTimer = new Timer(500, e -> showButtons());
        buttonShowTimer.setRepeats(false);
        buttonShowTimer.start();
    }

    public void restart() { startNewGame(); }
    public void cycleTheme() { themeManager.cycleTheme(); repaint(); }
    public void toggleSound() { soundManager.toggle(); }

    public void handleEnter() {
        if (showResults) {
            restart(); 
        }
    }

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