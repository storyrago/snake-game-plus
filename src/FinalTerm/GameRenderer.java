package FinalTerm;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameRenderer {
    private static final int CELL_SIZE = 25;
    private ImageManager imageManager;
    
    // [추가] 부유하는 텍스트 효과 관리 리스트
    private List<FloatingText> floatingTexts;

    public GameRenderer() {
        imageManager = new ImageManager(); 
        floatingTexts = new ArrayList<>();
    }
    
    // [추가] 외부에서 호출하여 텍스트 효과 생성
    public void addFloatingScore(Point position, int score) {
        int pixelX = position.x * CELL_SIZE;
        int pixelY = position.y * CELL_SIZE;
        floatingTexts.add(new FloatingText(pixelX, pixelY, "+" + score));
    }

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
        
        drawHammer(g2d, gameState.getBoard().getHammer());
        
        drawSnake(g2d, gameState.getSnake(), theme);
        
        // [추가] 플로팅 텍스트 렌더링
        drawFloatingTexts(g2d);
        
        if (gameState.isReducedVisionActive()) {
            drawDarkness(g2d, gameState.getSnake().getHead(), screenWidth, screenHeight);
        }
        
        if (gameState.isRouletteSpinning()) {
            drawRouletteAnimation(g2d, gameState.getCurrentRouletteText(), screenWidth, screenHeight, gameState.isCurrentRoulettePositive());
        }
        
        drawInfoPanel(g2d, gameState, screenWidth, screenHeight);
        
        if (effectMessage != null && !effectMessage.isEmpty()) {
            boolean isPositive = !effectMessage.contains("(BAD)");
            drawEffectMessage(g2d, effectMessage, screenWidth, screenHeight, isPositive);
        }

        if (showResults) {
            drawResultsScreen(g2d, gameState, scoreManager, screenWidth, screenHeight);
        }
    }
    
    // [추가] 텍스트 효과 그리기 및 상태 업데이트
    private void drawFloatingTexts(Graphics2D g2d) {
        if (floatingTexts.isEmpty()) return;

        Iterator<FloatingText> it = floatingTexts.iterator();
        while (it.hasNext()) {
            FloatingText ft = it.next();
            
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            
            // 그림자
            g2d.setColor(new Color(0, 0, 0, (int)(ft.alpha * 255)));
            g2d.drawString(ft.text, ft.x + 1, (int)ft.y + 1);
            
            // 본문 (금색)
            g2d.setColor(new Color(255, 215, 0, (int)(ft.alpha * 255)));
            g2d.drawString(ft.text, ft.x, (int)ft.y);
            
            ft.update();
            
            if (!ft.isAlive()) {
                it.remove();
            }
        }
    }

    private void drawHammer(Graphics2D g2d, SpecialItem item) {
        if (item == null) return;
        Point pos = item.getPosition();
        
        Image hammerImg = imageManager.getImage("hammer");
        
        if (hammerImg != null) {
            g2d.drawImage(hammerImg, pos.x * CELL_SIZE, pos.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
        } else {
            g2d.setColor(new Color(255, 140, 0));
            g2d.fillRect(pos.x * CELL_SIZE + 2, pos.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("H", pos.x * CELL_SIZE + 8, pos.y * CELL_SIZE + 18);
        }
    }

    private void drawPortals(Graphics2D g2d, Portal[] portals) {
        if (portals == null) return;
        for (int i = 0; i < portals.length; i++) {
            Point p = portals[i].getPosition();
            Image portalImg = null;
            if (i == 0) {
                portalImg = imageManager.getImage("portal_purple");
            } else {
                portalImg = imageManager.getImage("portal_blue");
            }
            if (portalImg != null) {
                g2d.drawImage(portalImg, p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
            } else {
                Color portalColor = i == 0 ? new Color(255, 0, 255) : Color.BLUE;
                g2d.setColor(new Color(portalColor.getRed(), portalColor.getGreen(), portalColor.getBlue(), 150));
                g2d.fillOval(p.x * CELL_SIZE + 2, p.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            }
        }
    }
    
    private void drawFood(Graphics2D g2d, Food food, ThemeManager.ColorTheme theme) {
        if (food == null) return;
        Point pos = food.getPosition();
        String type = food.getType();
        Image foodImg = null;
        if ("rare".equals(type)) {
            foodImg = imageManager.getImage("food_rare");
        } else {
            foodImg = imageManager.getImage("food_normal");
        }
        if (foodImg != null) {
            g2d.drawImage(foodImg, pos.x * CELL_SIZE, pos.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
        } else {
            Color foodColor = "rare".equals(type) ? new Color(255, 215, 0) : theme.food;
            g2d.setColor(foodColor);
            g2d.fillOval(pos.x * CELL_SIZE + 3, pos.y * CELL_SIZE + 3, CELL_SIZE - 6, CELL_SIZE - 6);
        }
    }

    private void drawSpecialItem(Graphics2D g2d, SpecialItem item) {
        if (item == null) return;
        Point pos = item.getPosition();
        Image itemImg = imageManager.getImage("item_random");
        if (itemImg != null) {
            g2d.drawImage(itemImg, pos.x * CELL_SIZE, pos.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
        } else {
            g2d.setColor(new Color(100, 149, 237));
            g2d.fillRect(pos.x * CELL_SIZE + 2, pos.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            g2d.setColor(new Color(255, 215, 0));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(pos.x * CELL_SIZE + 2, pos.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("?", pos.x * CELL_SIZE + 8, pos.y * CELL_SIZE + 18);
        }
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
                Color c = new Color(theme.snake.getRed(), theme.snake.getGreen(), theme.snake.getBlue(), Math.max(0, alpha));
                g2d.setColor(c);
                g2d.fillRect(segment.x * CELL_SIZE + 2, segment.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            }
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
            g2d.fillRect(obs.x * CELL_SIZE + 2, obs.y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
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
    
    private void drawRouletteAnimation(Graphics2D g2d, String text, int width, int height, boolean isPositive) {
        int boxWidth = 250;
        int boxHeight = 70;
        int x = (width - boxWidth) / 2;
        int y = 40;
        Color bgColor = isPositive ? new Color(20, 40, 60, 220) : new Color(60, 20, 20, 220);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        g2d.setColor(new Color(255, 215, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, boxWidth, boxHeight, 15, 15);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("Malgun Gothic", Font.ITALIC, 12));
        g2d.drawString("룰렛 돌리는중...", x + 15, y + 20);
        g2d.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (boxWidth - fm.stringWidth(text)) / 2;
        int textY = y + 50;
        Color textColor = isPositive ? new Color(0, 255, 255) : new Color(255, 80, 80);
        g2d.setColor(textColor);
        g2d.drawString(text, textX, textY);
    }
    
    private void drawEffectMessage(Graphics2D g2d, String msg, int width, int height, boolean isPositive) {
        g2d.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(msg)) / 2;
        int y = height / 2;
        g2d.setColor(Color.BLACK); g2d.drawString(msg, x + 2, y + 2);
        g2d.setColor(isPositive ? new Color(50, 255, 50) : new Color(255, 50, 50));
        g2d.drawString(msg, x, y);
    }

    private void drawInfoPanel(Graphics2D g2d, GameState gameState, int screenWidth, int screenHeight) {
        int gameAreaHeight = GameBoard.getGridSize() * CELL_SIZE;
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRect(0, gameAreaHeight, screenWidth, screenHeight - gameAreaHeight);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        int y = gameAreaHeight + 25;
        g2d.drawString("점수: " + gameState.getScore(), 10, y);
        g2d.drawString("길이: " + gameState.getSnake().getLength(), 150, y);
        
        if (gameState.hasHammer()) {
            g2d.setColor(new Color(255, 140, 0));
            g2d.drawString("[해머 장착..!!]", 280, y);
            g2d.setColor(Color.WHITE);
        } else if (GameSettings.getMode() == GameMode.TIME_ATTACK) {
            int remainingTime = gameState.getRemainingTime(GameSettings.getTimeLimit());
            g2d.setColor(remainingTime <= 10 ? Color.RED : Color.CYAN);
            g2d.drawString("시간: " + remainingTime + "초", 280, y);
            g2d.setColor(Color.WHITE);
        } else if (gameState.getCombo() > 0) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Combo: x" + gameState.getCombo(), 280, y);
            g2d.setColor(Color.WHITE);
        }
        
        String status = "";
        if (gameState.isDoubleScoreActive()) status += "[점수 2배!] ";
        if (gameState.isReverseInputActive()) status += "[리버스!] ";
        int speed = gameState.getSpeedEffect();
        if (speed == 1) status += "[빠름] ";
        if (speed == -1) status += "[느림] ";
        
        g2d.setColor(new Color(0, 255, 255));
        g2d.drawString(status, 10, y + 25);
        
        g2d.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        int guideY = y + 55;
        g2d.setColor(Color.WHITE);
        g2d.drawString("R: 재시작 | T: 테마 | M: 사운드", 10, guideY);
        g2d.drawString("?: 랜덤아이템 | H: 해머 (자동 사용)", 10, guideY + 20);
    }

    private void drawResultsScreen(Graphics2D g2d, GameState gameState, ScoreManager scoreManager, int width, int height) {
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, width, GameBoard.getGridSize() * CELL_SIZE);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Malgun Gothic", Font.BOLD, 36));
        String text = "게임 오버";
        int x = (width - g2d.getFontMetrics().stringWidth(text)) / 2;
        g2d.drawString(text, x, 80);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("플레이 기록", 50, 130);
        g2d.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        g2d.setColor(Color.WHITE);
        int lineY = 155;
        g2d.drawString("최종 점수: " + gameState.getScore(), 50, lineY);
        g2d.drawString("최대 콤보: x" + gameState.getMaxCombo(), 50, lineY + 25);
        List<ScoreManager.ScoreRecord> highScores = scoreManager.getHighScores(
            GameSettings.getMode(), GameSettings.getDisplayDifficulty());
        if (!highScores.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Top 5", 50, lineY + 140);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.setColor(Color.WHITE);
            for (int i = 0; i < Math.min(5, highScores.size()); i++) {
                ScoreManager.ScoreRecord record = highScores.get(i);
                g2d.drawString((i + 1) + ". " + record.playerName + " - " + record.score + " pts", 
                              50, lineY + 170 + i * 25);
            }
        }
    }
    
    // [추가] 내부 클래스: 부유 텍스트
    private class FloatingText {
        int x;
        float y;
        String text;
        float alpha;

        public FloatingText(int x, int y, String text) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.alpha = 1.0f;
        }

        public void update() {
            y -= 1.0f; // 위로 이동
            alpha -= 0.02f; // 투명도 감소
        }

        public boolean isAlive() {
            return alpha > 0;
        }
    }
}