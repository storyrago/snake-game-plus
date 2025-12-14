package FinalTerm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class GameRenderer {
    private static final int CELL_SIZE = 25;
    
    // 마지막으로 표시된 효과 메시지의 색상을 기억하기 위한 변수
    private boolean lastMessagePositive = true;

    public void render(Graphics2D g2d, GameState gameState, ThemeManager themeManager, 
                      int screenWidth, int screenHeight, boolean showResults, 
                      ScoreManager scoreManager, String effectMessage) {
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        ThemeManager.ColorTheme theme = themeManager.getCurrentTheme();
        
        drawBackground(g2d, theme, screenWidth, screenHeight);
        drawObstacles(g2d, gameState.getBoard().getObstacles());
        drawPortals(g2d, gameState.getBoard().getPortals());
        drawFood(g2d, gameState.getBoard().getFood(), theme);
        drawSpecialItem(g2d, gameState.getBoard().getSpecialItem());
        drawSnake(g2d, gameState.getSnake(), theme);
        
        if (gameState.isReducedVisionActive()) {
            drawDarkness(g2d, gameState.getSnake().getHead(), screenWidth, screenHeight);
        }
        
        // 룰렛 애니메이션 (상태에 따라 색상 전달)
        if (gameState.isRouletteSpinning()) {
            drawRouletteAnimation(g2d, gameState.getCurrentRouletteText(), screenWidth, screenHeight, gameState.isCurrentRoulettePositive());
        }
        
        drawInfoPanel(g2d, gameState, screenWidth, screenHeight);
        
        // 효과 메시지 (긍정/부정 색상 적용)
        if (effectMessage != null && !effectMessage.isEmpty()) {
            // 메시지가 갱신될 때마다 색상을 정해야 하지만, 
            // 여기선 GameState가 룰렛이 끝났을 때의 상태를 계속 가지고 있지 않을 수 있음.
            // SnakeGame에서 메시지 타이머를 돌리므로, 렌더러는 전달받은 메시지를 그릴 뿐임.
            // 따라서 메시지 내용이나 별도의 플래그가 필요하지만, 
            // 편의상 룰렛이 끝난 직후라면 gameState의 마지막 상태가 유효하다고 가정하거나,
            // 메시지 텍스트에 "(BAD)"가 포함되어 있는지로 판단하는 게 안전함.
            boolean isPositive = !effectMessage.contains("(BAD)");
            drawEffectMessage(g2d, effectMessage, screenWidth, screenHeight, isPositive);
        }

        if (showResults) {
            drawResultsScreen(g2d, gameState, scoreManager, screenWidth, screenHeight);
        }
    }
    
    private void drawRouletteAnimation(Graphics2D g2d, String text, int width, int height, boolean isPositive) {
        int boxWidth = 250;
        int boxHeight = 70;
        int x = (width - boxWidth) / 2;
        int y = 40;
        
        // 배경: 긍정이면 푸른빛, 부정이면 붉은빛
        Color bgColor = isPositive ? new Color(20, 40, 60, 220) : new Color(60, 20, 20, 220);
        
        g2d.setColor(bgColor);
        g2d.fillRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        
        // 테두리
        g2d.setColor(new Color(255, 215, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("Arial", Font.ITALIC, 12));
        g2d.drawString("Item Roulette...", x + 15, y + 20);
        
        // 아이템 텍스트 색상 구분
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (boxWidth - fm.stringWidth(text)) / 2;
        int textY = y + 50;
        
        Color textColor = isPositive ? new Color(0, 255, 255) : new Color(255, 80, 80);
        g2d.setColor(textColor);
        g2d.drawString(text, textX, textY);
    }
    
    private void drawEffectMessage(Graphics2D g2d, String msg, int width, int height, boolean isPositive) {
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(msg)) / 2;
        int y = height / 2;
        
        // 그림자
        g2d.setColor(Color.BLACK);
        g2d.drawString(msg, x + 2, y + 2);
        
        // 긍정: 라임색, 부정: 빨간색
        Color msgColor = isPositive ? new Color(50, 255, 50) : new Color(255, 50, 50);
        g2d.setColor(msgColor);
        g2d.drawString(msg, x, y);
    }
    
    // ... 나머지 메서드는 기존 유지 (drawBackground, drawSnake 등) ...
    // 아래 메서드들을 반드시 복사해서 유지해주세요.
    
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
            g2d.fillRect(obs.x * CELL_SIZE + 2, obs.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
        }
    }

    private void drawPortals(Graphics2D g2d, Portal[] portals) {
        if (portals == null) return;
        for (int i = 0; i < portals.length; i++) {
            Color portalColor = i == 0 ? new Color(255, 0, 255) : new Color(0, 255, 255);
            g2d.setColor(new Color(portalColor.getRed(), portalColor.getGreen(), portalColor.getBlue(), 150));
            Point p = portals[i].getPosition();
            g2d.fillOval(p.x * CELL_SIZE + 2, p.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
        }
    }

    private void drawFood(Graphics2D g2d, Food food, ThemeManager.ColorTheme theme) {
        if (food == null) return;
        Color foodColor = "rare".equals(food.getType()) ? new Color(255, 215, 0) : theme.food;
        g2d.setColor(foodColor);
        Point pos = food.getPosition();
        g2d.fillOval(pos.x * CELL_SIZE + 3, pos.y * CELL_SIZE + 3, CELL_SIZE - 6, CELL_SIZE - 6);
    }

    private void drawSpecialItem(Graphics2D g2d, SpecialItem item) {
        if (item == null) return;
        Point pos = item.getPosition();
        g2d.setColor(new Color(100, 149, 237));
        g2d.fillRect(pos.x * CELL_SIZE + 2, pos.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
        g2d.setColor(new Color(255, 215, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(pos.x * CELL_SIZE + 2, pos.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("?", pos.x * CELL_SIZE + 8, pos.y * CELL_SIZE + 18);
    }

    private void drawSnake(Graphics2D g2d, Snake snake, ThemeManager.ColorTheme theme) {
        List<Point> body = snake.getBody();
        for (int i = 0; i < body.size(); i++) {
            Point segment = body.get(i);
            if (i == 0) {
                g2d.setColor(theme.snake);
                g2d.fillRect(segment.x * CELL_SIZE + 1, segment.y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(segment.x * CELL_SIZE + 5, segment.y * CELL_SIZE + 5, 4, 4);
                g2d.fillRect(segment.x * CELL_SIZE + 14, segment.y * CELL_SIZE + 5, 4, 4);
            } else {
                int alpha = 255 - (int)((i / (float)body.size()) * 100);
                g2d.setColor(new Color(theme.snake.getRed(), theme.snake.getGreen(), theme.snake.getBlue(), Math.max(0, alpha)));
                g2d.fillRect(segment.x * CELL_SIZE + 2, segment.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            }
        }
    }
    
    private void drawDarkness(Graphics2D g2d, Point snakeHead, int width, int height) {
        Area darkness = new Area(new Rectangle2D.Double(0, 0, width, height));
        int visionRadius = CELL_SIZE * 4;
        int centerX = snakeHead.x * CELL_SIZE + CELL_SIZE / 2;
        int centerY = snakeHead.y * CELL_SIZE + CELL_SIZE / 2;
        Shape visionHole = new Ellipse2D.Double(centerX - visionRadius, centerY - visionRadius, visionRadius * 2, visionRadius * 2);
        darkness.subtract(new Area(visionHole));
        g2d.setColor(Color.BLACK);
        g2d.fill(darkness);
    }
    
    private void drawInfoPanel(Graphics2D g2d, GameState gameState, int screenWidth, int screenHeight) {
        int gameAreaHeight = GameBoard.getGridSize() * CELL_SIZE;
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRect(0, gameAreaHeight, screenWidth, screenHeight - gameAreaHeight);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        int y = gameAreaHeight + 25;
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
        
        String status = "";
        if (gameState.isDoubleScoreActive()) status += "[x2 Score] ";
        if (gameState.isReverseInputActive()) status += "[Reverse] ";
        int speed = gameState.getSpeedEffect();
        if (speed == 1) status += "[Fast] ";
        if (speed == -1) status += "[Slow] ";
        
        g2d.setColor(new Color(0, 255, 255));
        g2d.drawString(status, 10, y + 25);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        int guideY = y + 55;
        g2d.setColor(Color.WHITE);
        g2d.drawString("R: Restart | T: Theme | M: Sound", 10, guideY);
        g2d.drawString("?: Random Box | Eat Food -> Spawn Obstacle", 10, guideY + 20);
    }
    
    private void drawResultsScreen(Graphics2D g2d, GameState gameState, ScoreManager scoreManager, int width, int height) {
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, width, GameBoard.getGridSize() * CELL_SIZE);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        String text = "GAME OVER";
        int x = (width - g2d.getFontMetrics().stringWidth(text)) / 2;
        g2d.drawString(text, x, 80);
        
        // ... (나머지 랭킹 표시는 이전과 동일) ...
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Play Record", 50, 130);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(Color.WHITE);
        int lineY = 155;
        g2d.drawString("Final Score: " + gameState.getScore(), 50, lineY);
        g2d.drawString("Max Combo: x" + gameState.getMaxCombo(), 50, lineY + 25);
        
        List<ScoreManager.ScoreRecord> highScores = scoreManager.getHighScores(
            GameSettings.getMode(), 
            GameSettings.getDisplayDifficulty()
        );
        if (!highScores.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Top 5 Ranking", 50, lineY + 140);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.setColor(Color.WHITE);
            for (int i = 0; i < Math.min(5, highScores.size()); i++) {
                ScoreManager.ScoreRecord record = highScores.get(i);
                g2d.drawString((i + 1) + ". " + record.playerName + " - " + record.score + " pts", 
                              50, lineY + 170 + i * 25);
            }
        }
    }
}