package FinalTerm;

import java.awt.Point;
import java.util.Random;

public class GameState {
    private Snake snake;
    private GameBoard board;
    private int score;
    private int playTime;
    private int foodEaten;
    private int itemsCollected;
    
    private long lastEatTime;
    private int currentCombo;
    private int maxCombo;
    
    private long doubleScoreEndTime = 0;
    private long reverseInputEndTime = 0;
    private long reducedVisionEndTime = 0;
    private long speedEffectEndTime = 0;
    private boolean isSpeedUp = false;
    private boolean isSpeedDown = false;
    
    private long portalEndTime = 0;
    
    // volatile: 스레드 간 가시성 보장
    private volatile boolean isRouletteSpinning = false;
    private long rouletteEndTime = 0;
    private volatile String currentRouletteText = "";
    private volatile boolean currentRoulettePositive = true;
    
    private boolean hasHammer = false;
    
    private final String[] rouletteOptions = {
        "SPEED UP! (BAD)", "SPEED DOWN (GOOD)", "X2 SCORE (GOOD)", 
        "REVERSE (BAD)", "DARKNESS (BAD)", "SCISSORS (GOOD)"
    };
    
    public GameState() {
        reset();
    }
    
    public void reset() {
        snake = new Snake(new Point(10, 10));
        board = new GameBoard(20, 20);
        score = 0;
        playTime = 0;
        foodEaten = 0;
        itemsCollected = 0;
        currentCombo = 0;
        maxCombo = 0;
        lastEatTime = 0;
        hasHammer = false;
        
        board.spawnFood(snake.getBody());
        board.removePortals();
        resetEffects();
    }
    
    public void resetEffects() {
        doubleScoreEndTime = 0;
        reverseInputEndTime = 0;
        reducedVisionEndTime = 0;
        speedEffectEndTime = 0;
        isSpeedUp = false;
        isSpeedDown = false;
        portalEndTime = 0;
        
        isRouletteSpinning = false;
        rouletteEndTime = 0;
        currentRouletteText = "";
    }
    
    public boolean isTimeUp(int limit) { return playTime >= limit; }
    public int getRemainingTime(int limit) { return Math.max(0, limit - playTime); }
    
    public UpdateResult update(Point inputDirection) {
        Point newHead = snake.getNextHeadPosition();
        
        if (board.isWall(newHead)) return new UpdateResult(UpdateResult.Type.WALL_COLLISION, newHead);
        if (snake.checkSelfCollision(newHead)) return new UpdateResult(UpdateResult.Type.SELF_COLLISION, newHead);
        
        if (board.isObstacle(newHead)) {
            if (hasHammer) {
                hasHammer = false;
                board.removeObstacle(newHead);
                snake.moveTo(newHead);
                return new UpdateResult(UpdateResult.Type.OBSTACLE_DESTROYED, newHead, 0, "HAMMER USED!", true);
            } else {
                return new UpdateResult(UpdateResult.Type.OBSTACLE_COLLISION, newHead);
            }
        }
        
        snake.moveTo(newHead);
        UpdateResult result = new UpdateResult(UpdateResult.Type.NORMAL_MOVE, newHead);
        
        managePortals();
        
        // 메인 로직에서는 시간 체크만 수행
        if (isRouletteSpinning) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= rouletteEndTime) {
                isRouletteSpinning = false; // 스레드 종료 신호
                return applyRandomEffect();
            }
        }
        
        SpecialItem item = board.getSpecialItem();
        if (item != null && item.getPosition().equals(newHead)) {
            board.removeSpecialItem();
            itemsCollected++;
            startRoulette();
            result = new UpdateResult(UpdateResult.Type.ITEM_COLLECTED, newHead);
        }
        
        SpecialItem hammer = board.getHammer();
        if (hammer != null && hammer.getPosition().equals(newHead)) {
            board.removeHammer();
            hasHammer = true;
            result = new UpdateResult(UpdateResult.Type.HAMMER_COLLECTED, newHead, 0, "HAMMER GET!", true);
        }
        
        Food food = board.getFood();
        if (food != null && food.getPosition().equals(newHead)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastEatTime < 2000) currentCombo++;
            else currentCombo = 0;
            if (currentCombo > maxCombo) maxCombo = currentCombo;
            lastEatTime = currentTime;
            
            int points = "rare".equals(food.getType()) ? 30 : 10;
            points += (currentCombo * 5);
            if (isDoubleScoreActive()) points *= 2;
            
            score += points;
            foodEaten++;
            snake.grow();
            board.spawnFood(snake.getBody());
            
            Random rnd = new Random();
            if (rnd.nextInt(100) < 20) board.spawnSpecialItem(snake.getBody());
            if (rnd.nextInt(100) < 15) board.spawnObstacle(snake.getBody());
            if (rnd.nextInt(100) < 10) board.spawnHammer(snake.getBody());
            
            result = new UpdateResult(UpdateResult.Type.FOOD_EATEN, newHead, currentCombo);
        }
        
        Portal enteredPortal = board.checkPortal(newHead);
        if (enteredPortal != null) {
             Point dest = board.getPortalDestination(enteredPortal);
             if (dest != null) {
                 snake.setHead(dest);
                 result = new UpdateResult(UpdateResult.Type.PORTAL_USED, dest);
             }
        }
        
        return result;
    }
    
    private void startRoulette() {
        if (isRouletteSpinning) return;
        
        isRouletteSpinning = true;
        rouletteEndTime = System.currentTimeMillis() + 3000;
        
        // [중요] 룰렛 텍스트 변경 스레드 (게임 속도와 무관하게 80ms마다 변경)
        new Thread(() -> {
            Random r = new Random();
            while (isRouletteSpinning && System.currentTimeMillis() < rouletteEndTime) {
                int randIdx = r.nextInt(rouletteOptions.length);
                currentRouletteText = rouletteOptions[randIdx];
                currentRoulettePositive = isPositiveEffect(randIdx);
                try {
                    Thread.sleep(80); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
    
    private boolean isPositiveEffect(int index) {
        return index == 1 || index == 2 || index == 5;
    }
    
    private UpdateResult applyRandomEffect() {
        int rand = new Random().nextInt(6);
        long currentTime = System.currentTimeMillis();
        String effectName = "";
        boolean isPositive = isPositiveEffect(rand);
        
        switch (rand) {
            case 0: isSpeedUp = true; isSpeedDown = false; speedEffectEndTime = currentTime + 5000; effectName = "SPEED UP! (BAD)"; break;
            case 1: isSpeedDown = true; isSpeedUp = false; speedEffectEndTime = currentTime + 5000; effectName = "SPEED DOWN (GOOD)"; break;
            case 2: doubleScoreEndTime = currentTime + 10000; effectName = "DOUBLE SCORE (10s)"; break;
            case 3: reverseInputEndTime = currentTime + 5000; effectName = "REVERSE CONTROL (5s)"; break;
            case 4: reducedVisionEndTime = currentTime + 5000; effectName = "DARKNESS (5s)"; break;
            case 5: snake.cutTail(3); effectName = "SCISSORS (CUT TAIL)"; break;
        }
        
        currentRouletteText = effectName;
        currentRoulettePositive = isPositive;
        
        return new UpdateResult(UpdateResult.Type.ROULETTE_FINISHED, snake.getHead(), 0, effectName, isPositive);
    }
    
    private void managePortals() {
        long currentTime = System.currentTimeMillis();
        if (board.getPortals() != null) {
            if (currentTime > portalEndTime) board.removePortals();
        } else {
            if (new Random().nextInt(200) == 0) { 
                board.spawnPortals(snake.getBody());
                if (board.getPortals() != null) portalEndTime = currentTime + 15000;
            }
        }
    }

    // Getters
    public boolean hasHammer() { return hasHammer; }
    
    public boolean isRouletteSpinning() { return isRouletteSpinning; }
    public String getCurrentRouletteText() { return currentRouletteText; }
    public boolean isCurrentRoulettePositive() { return currentRoulettePositive; }

    public boolean isDoubleScoreActive() { return System.currentTimeMillis() < doubleScoreEndTime; }
    public boolean isReverseInputActive() { return System.currentTimeMillis() < reverseInputEndTime; }
    public boolean isReducedVisionActive() { return System.currentTimeMillis() < reducedVisionEndTime; }
    
    public int getSpeedEffect() {
        if (System.currentTimeMillis() > speedEffectEndTime) {
            isSpeedUp = false; isSpeedDown = false; return 0;
        }
        if (isSpeedUp) return 1;
        if (isSpeedDown) return -1;
        return 0;
    }

    public void clearPortalCooldown() {}
    public Snake getSnake() { return snake; }
    public GameBoard getBoard() { return board; }
    public int getScore() { return score; }
    public int getMaxCombo() { return maxCombo; }
    public int getPlayTime() { return playTime; }
    public void incrementPlayTime() { playTime++; }
    public int getFoodEaten() { return foodEaten; }
    public int getItemsCollected() { return itemsCollected; }
    public int getCombo() {
         if (System.currentTimeMillis() - lastEatTime >= 2000) return 0;
         return currentCombo;
    }
}