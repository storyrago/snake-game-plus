package FinalTerm;

//SnakeGame.java - 메인 게임 컨트롤러

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
    private boolean running;
    private boolean showResults;
    
    private int screenWidth;
    private int screenHeight;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGameController controller = new SnakeGameController();
            controller.show();
        });
    }
    
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
        
        gameState = new GameState();
        renderer = new GameRenderer();
        inputHandler = new InputHandler(this);
        
        addKeyListener(inputHandler);
        
        running = false;
        showResults = false;
        
        gameTimer = new Timer(delay, this);
    }
    
    public void startNewGame() {
        gameState.reset();
        
        GameDifficulty difficulty = GameSettings.getDifficulty();
        delay = difficulty.getInitialDelay();
        
        running = true;
        showResults = false;
        gameTimer.setDelay(delay);
        gameTimer.start();
        repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {  // paused 조건 제거
            update();
            repaint();
        }
    }
    
    private void update() {
        // 시간 제한 모드 체크
        if (GameSettings.getMode() == GameMode.TIME_ATTACK) {
            if (gameState.isTimeUp(GameSettings.getTimeLimit())) {
                gameOver();
                return;
            }
        }
        
        UpdateResult result = gameState.update(null);
        
        handleUpdateResult(result);
        
        if (gameState.getSnake().getLength() % 5 == 0) {
            GameDifficulty difficulty = GameSettings.getDifficulty();
            int newDelay = Math.max(
                difficulty.getMinDelay(), 
                difficulty.getInitialDelay() - (gameState.getSnake().getLength() / 5) * 10
            );
            if (newDelay != delay) {
                delay = newDelay;
                gameTimer.setDelay(delay);
            }
        }
    }
    
    private void handleUpdateResult(UpdateResult result) {
        switch (result.getType()) {
            case FOOD_EATEN:
                soundManager.playSound(523 + (result.getCombo() * 50), 100);
                break;
            case ITEM_COLLECTED:
                soundManager.playSound(700, 150);
                break;
            case OBSTACLE_DESTROYED:
                soundManager.playSound(600, 100);
                break;
            case PORTAL_USED:
                soundManager.playSound(800, 100);
                Timer cooldownTimer = new Timer(500, evt -> gameState.clearPortalCooldown());
                cooldownTimer.setRepeats(false);
                cooldownTimer.start();
                break;
            case WALL_COLLISION:
            case SELF_COLLISION:
            case OBSTACLE_COLLISION:
                gameOver();
                break;
        }
    }
    
    private void gameOver() {
        running = false;
        showResults = true;
        gameTimer.stop();
        soundManager.playSound(200, 500);
        
        scoreManager.addScore(
            PlayerData.getPlayerName(),
            GameSettings.getMode(),
            GameSettings.getDisplayDifficulty(),
            gameState.getScore(),
            gameState.getMaxCombo(),
            gameState.getPlayTime(),
            gameState.getFoodEaten(),
            gameState.getItemsCollected()
        );
        
        repaint();
    }
    
    public void restart() {
        startNewGame();
    }
    
    public void cycleTheme() {
        themeManager.cycleTheme();
        repaint();
    }
    
    public void toggleSound() {
        soundManager.toggle();
    }
    
    public void handleEnter() {
        if (showResults) {
            controller.showScreen(GameScreen.MAIN_MENU);
        }
    }
    
    public void handleDirection(Point direction) {
        if (running) {  // paused 조건 제거
            gameState.getSnake().queueDirection(direction);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render((Graphics2D) g, gameState, themeManager, 
                       screenWidth, screenHeight, showResults, scoreManager);
    }
}
