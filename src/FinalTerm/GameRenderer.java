package FinalTerm;

//GameRenderer.java - 게임 렌더링 담당

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameRenderer {
    private static final int CELL_SIZE = 25;
    
    public void render(Graphics2D g2d, GameState gameState, ThemeManager themeManager, 
                      int screenWidth, int screenHeight, boolean showResults, 
                      ScoreManager scoreManager) {
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        ThemeManager.ColorTheme theme = themeManager.getCurrentTheme();
        
        drawBackground(g2d, theme, screenWidth, screenHeight);
        
        drawObstacles(g2d, gameState.getBoard().getObstacles());
        drawPortals(g2d, gameState.getBoard().getPortals());
        drawFood(g2d, gameState.getBoard().getFood(), theme);
        drawSpecialItem(g2d, gameState.getBoard().getSpecialItem());
        drawSnake(g2d, gameState.getSnake(), theme);
        
        drawInfoPanel(g2d, gameState, screenWidth, screenHeight);
        
        if (showResults) {
            drawResultsScreen(g2d, gameState, scoreManager, screenWidth, screenHeight);
        }
    }
    
    private void drawBackground(Graphics2D g2d, ThemeManager.ColorTheme theme, int width, int height) {
        int gameAreaHeight = GameBoard.getGridSize() * CELL_SIZE;
        g2d.setColor(theme.background);
        g2d.fillRect(0, 0, width, gameAreaHeight);
        
        g2d.setColor(theme.grid);
        int gridSize = GameBoard.getGridSize();
        for (int i = 0; i <= gridSize; i++) {
            g2d.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, gameAreaHeight);
            g2d.drawLine(0, i * CELL_SIZE, width, i * CELL_SIZE);
        }
    }
    
    private void drawObstacles(Graphics2D g2d, List<Point> obstacles) {
        g2d.setColor(new Color(102, 102, 102));
        for (Point obs : obstacles) {
            g2d.fillRect(obs.x * CELL_SIZE + 2, obs.y * CELL_SIZE + 2, 
                        CELL_SIZE - 4, CELL_SIZE - 4);
        }
    }
    
    private void drawPortals(Graphics2D g2d, Portal[] portals) {
        if (portals == null) return;
        
        for (int i = 0; i < portals.length; i++) {
            Color portalColor = i == 0 ? new Color(255, 0, 255) : new Color(0, 255, 255);
            g2d.setColor(new Color(portalColor.getRed(), portalColor.getGreen(), 
                                  portalColor.getBlue(), 150));
            Point p = portals[i].getPosition();
            g2d.fillOval(p.x * CELL_SIZE + 2, p.y * CELL_SIZE + 2, 
                       CELL_SIZE - 4, CELL_SIZE - 4);
        }
    }
    
    private void drawFood(Graphics2D g2d, Food food, ThemeManager.ColorTheme theme) {
        if (food == null) return;
        
        Color foodColor = "rare".equals(food.getType()) ? 
                        new Color(255, 215, 0) : theme.food;
        g2d.setColor(foodColor);
        Point pos = food.getPosition();
        g2d.fillOval(pos.x * CELL_SIZE + 3, pos.y * CELL_SIZE + 3, 
                    CELL_SIZE - 6, CELL_SIZE - 6);
    }
    
    private void drawSpecialItem(Graphics2D g2d, SpecialItem item) {
        if (item == null) return;
        
        Point pos = item.getPosition();
        g2d.setColor(new Color(255, 0, 255));
        g2d.fillRect(pos.x * CELL_SIZE + 2, pos.y * CELL_SIZE + 2, 
                    CELL_SIZE - 4, CELL_SIZE - 4);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String symbol = "hammer".equals(item.getType()) ? "H" : "S";
        FontMetrics fm = g2d.getFontMetrics();
        int textX = pos.x * CELL_SIZE + (CELL_SIZE - fm.stringWidth(symbol)) / 2;
        int textY = pos.y * CELL_SIZE + (CELL_SIZE + fm.getAscent()) / 2 - 2;
        g2d.drawString(symbol, textX, textY);
    }
    
    private void drawSnake(Graphics2D g2d, Snake snake, ThemeManager.ColorTheme theme) {
        List<Point> body = snake.getBody();
        
        for (int i = 0; i < body.size(); i++) {
            Point segment = body.get(i);
            if (i == 0) {
                g2d.setColor(theme.snake);
                g2d.fillRect(segment.x * CELL_SIZE + 1, segment.y * CELL_SIZE + 1, 
                           CELL_SIZE - 2, CELL_SIZE - 2);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(segment.x * CELL_SIZE + 5, segment.y * CELL_SIZE + 5, 4, 4);
                g2d.fillRect(segment.x * CELL_SIZE + 14, segment.y * CELL_SIZE + 5, 4, 4);
            } else {
                int alpha = 255 - (int)((i / (float)body.size()) * 100);
                Color segmentColor = new Color(theme.snake.getRed(), theme.snake.getGreen(), 
                                             theme.snake.getBlue(), alpha);
                g2d.setColor(segmentColor);
                g2d.fillRect(segment.x * CELL_SIZE + 2, segment.y * CELL_SIZE + 2, 
                           CELL_SIZE - 4, CELL_SIZE - 4);
            }
        }
    }
    
    private void drawInfoPanel(Graphics2D g2d, GameState gameState, int screenWidth, int screenHeight) {
        int gameAreaHeight = GameBoard.getGridSize() * CELL_SIZE;
        int panelY = gameAreaHeight;
        
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRect(0, panelY, screenWidth, screenHeight - gameAreaHeight);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        
        int y = panelY + 25;
        
        g2d.drawString("Score: " + gameState.getScore(), 10, y);
        g2d.drawString("Length: " + gameState.getSnake().getLength(), 150, y);
        
        if (GameSettings.getMode() == GameMode.TIME_ATTACK) {
            int remainingTime = gameState.getRemainingTime(GameSettings.getTimeLimit());
            g2d.setColor(remainingTime <= 10 ? Color.RED : Color.CYAN);
            g2d.drawString("Time: " + remainingTime + "s", 280, y);
            g2d.setColor(Color.WHITE);
        } else if (gameState.getCombo() > 0) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Combo: x" + gameState.getCombo(), 280, y);
            g2d.setColor(Color.WHITE);
        }
        
        if (gameState.getActiveItem() != null) {
            String itemText = "hammer".equals(gameState.getActiveItem()) ? 
                             "Item: Hammer" : "Item: Scissors";
            g2d.setColor(new Color(255, 0, 255));
            g2d.drawString(itemText, 10, y + 30);
            g2d.setColor(Color.WHITE);
        }
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        y += 60;
        g2d.drawString("R: Restart | T: Theme | M: Sound", 10, y);
        y += 20;
        g2d.drawString("Red/Gold: Food | Purple/Cyan: Portal | Gray: Obstacle", 10, y);
        y += 20;
        g2d.drawString("H: Hammer | S: Scissors | Combo: Eat within 5sec", 10, y);
    }
    
    private void drawResultsScreen(Graphics2D g2d, GameState gameState, 
                                   ScoreManager scoreManager, int screenWidth, int screenHeight) {
        int gameAreaHeight = GameBoard.getGridSize() * CELL_SIZE;
        
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, screenWidth, gameAreaHeight);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        String gameOverText = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (screenWidth - fm.stringWidth(gameOverText)) / 2;
        g2d.drawString(gameOverText, x, 80);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Play Record", 50, 130);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(Color.WHITE);
        int lineY = 155;
        g2d.drawString("Final Score: " + gameState.getScore(), 50, lineY);
        g2d.drawString("Max Combo: x" + gameState.getMaxCombo(), 50, lineY + 25);
        g2d.drawString("Play Time: " + gameState.getPlayTime() + "sec", 50, lineY + 50);
        g2d.drawString("Food Eaten: " + gameState.getFoodEaten(), 50, lineY + 75);
        g2d.drawString("Items Collected: " + gameState.getItemsCollected(), 50, lineY + 100);
        g2d.drawString("Final Length: " + gameState.getSnake().getLength(), 50, lineY + 125);
        
        // 현재 게임 모드와 난이도의 랭킹 가져오기
        List<ScoreManager.ScoreRecord> highScores = scoreManager.getHighScores(
            GameSettings.getMode(), 
            GameSettings.getDisplayDifficulty()
        );
        
        if (!highScores.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setColor(Color.YELLOW);
            String modeText = GameSettings.getMode() == GameMode.CLASSIC ? "Classic" : "Time Attack";
            String diffText = GameSettings.getDisplayDifficulty().getDisplayName();
            g2d.drawString(modeText + " - " + diffText + " Top 5", 50, lineY + 165);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.setColor(Color.WHITE);
            for (int i = 0; i < Math.min(5, highScores.size()); i++) {
                ScoreManager.ScoreRecord record = highScores.get(i);
                g2d.drawString((i + 1) + ". " + record.playerName + " - " + record.score + " pts", 
                              50, lineY + 195 + i * 25);
            }
        }
        
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.GREEN);
        String restartText = "Press ENTER to Main Menu";
        fm = g2d.getFontMetrics();
        x = (screenWidth - fm.stringWidth(restartText)) / 2;
        g2d.drawString(restartText, x, gameAreaHeight - 30);
    }
}
