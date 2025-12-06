package FinalTerm;

import java.awt.Point;
import java.util.*;

public class GameState {
    private Snake snake;
    private GameBoard board;
    private int score;
    private int combo;
    private int maxCombo;
    private String activeItem;
    private long lastFoodTime;
    private long gameStartTime;
    private boolean portalCooldown;
    private int foodEaten;
    private int itemsCollected;
    
    public GameState() {
        reset();
    }
    
    public void reset() {
        snake = new Snake(new Point(10, 10));
        board = new GameBoard();
        score = 0;
        combo = 0;
        maxCombo = 0;
        activeItem = null;
        foodEaten = 0;
        itemsCollected = 0;
        portalCooldown = false;
        gameStartTime = System.currentTimeMillis();
        lastFoodTime = System.currentTimeMillis();
        
        board.spawnFood(snake);
    }
    
    public UpdateResult update(Point direction) {
        Point head = snake.getNextHeadPosition();
        
        if (!portalCooldown) {
            Point portalExit = board.checkPortal(head);
            if (portalExit != null) {
                head = portalExit;
                portalCooldown = true;
                snake.moveTo(head);
                return new UpdateResult(UpdateResult.Type.PORTAL_USED, head);
            }
        }
        
        if (board.isOutOfBounds(head)) {
            return new UpdateResult(UpdateResult.Type.WALL_COLLISION, head);
        }
        
        if (snake.checkSelfCollision(head)) {
            return new UpdateResult(UpdateResult.Type.SELF_COLLISION, head);
        }
        
        if (board.hasObstacle(head)) {
            if ("hammer".equals(activeItem)) {
                board.removeObstacle(head);
                activeItem = null;
                snake.moveTo(head);
                return new UpdateResult(UpdateResult.Type.OBSTACLE_DESTROYED, head);
            }
            return new UpdateResult(UpdateResult.Type.OBSTACLE_COLLISION, head);
        }
        
        Food food = board.getFood();
        boolean ate = false;
        if (head.equals(food.getPosition())) {
            ate = true;
            handleFoodEaten(food);
            
            board.spawnFood(snake);
            
            if (board.getObstacleCount() < 5 && Math.random() < 0.3) {
                board.createObstacle(snake);
            }
            if (!board.hasSpecialItem() && Math.random() < 0.2) {
                board.createSpecialItem(snake);
            }
            if (snake.getLength() % 10 == 0) {
                board.createPortals(snake);
            }
        }
        
        SpecialItem item = board.getSpecialItem();
        if (item != null && head.equals(item.getPosition())) {
            handleItemCollected(item);
            snake.moveTo(head);
            return new UpdateResult(UpdateResult.Type.ITEM_COLLECTED, head);
        }
        
        snake.moveTo(head);
        
        if (ate) {
            snake.grow();
            return new UpdateResult(UpdateResult.Type.FOOD_EATEN, head, combo);
        }
        
        return new UpdateResult(UpdateResult.Type.NORMAL_MOVE, head);
    }
    
    private void handleFoodEaten(Food food) {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - lastFoodTime;
        
        int points = food.getPoints();
        if (timeDiff < 5000) {  // 2000에서 5000으로 변경
            combo++;
            points += combo * 5;
            maxCombo = Math.max(maxCombo, combo);
        } else {
            combo = 0;
        }
        
        score += points;
        foodEaten++;
        lastFoodTime = currentTime;
    }

    
    private void handleItemCollected(SpecialItem item) {
        if ("scissors".equals(item.getType())) {
            int cutLength = (int)(snake.getLength() * 0.3);
            snake.cutTail(cutLength);
        } else {
            activeItem = item.getType();
        }
        board.removeSpecialItem();
        itemsCollected++;
    }
    
    public void clearPortalCooldown() {
        portalCooldown = false;
    }
    
    public int getPlayTime() {
        return (int)((System.currentTimeMillis() - gameStartTime) / 1000);
    }
    
    // 남은 시간 계산 (시간 제한 모드용)
    public int getRemainingTime(int timeLimit) {
        int elapsed = getPlayTime();
        return Math.max(0, timeLimit - elapsed);
    }
    
    // 시간 초과 체크
    public boolean isTimeUp(int timeLimit) {
        return getPlayTime() >= timeLimit;
    }
    
    // Getters
    public Snake getSnake() { return snake; }
    public GameBoard getBoard() { return board; }
    public int getScore() { return score; }
    public int getCombo() { return combo; }
    public int getMaxCombo() { return maxCombo; }
    public String getActiveItem() { return activeItem; }
    public int getFoodEaten() { return foodEaten; }
    public int getItemsCollected() { return itemsCollected; }
}
