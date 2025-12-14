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
    
    // 룰렛 관련
    private boolean isRouletteSpinning = false;
    private long rouletteEndTime = 0;
    private String currentRouletteText = "";
    private boolean currentRoulettePositive = true; // [추가] 룰렛 텍스트 색상 결정용
    
    // 옵션 목록 (인덱스 매핑: 0-빠름, 1-느림, 2-2배, 3-반전, 4-암흑, 5-가위)
    private final String[] rouletteOptions = {
        "SPEED UP! (BAD)",    // 0: 부정
        "SPEED DOWN (GOOD)",  // 1: 긍정
        "X2 SCORE (GOOD)",    // 2: 긍정
        "REVERSE (BAD)",      // 3: 부정
        "DARKNESS (BAD)",     // 4: 부정
        "SCISSORS (GOOD)"     // 5: 긍정
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
        if (board.isObstacle(newHead)) return new UpdateResult(UpdateResult.Type.OBSTACLE_COLLISION, newHead);
        
        snake.moveTo(newHead);
        UpdateResult result = new UpdateResult(UpdateResult.Type.NORMAL_MOVE, newHead);
        
        managePortals();
        
        if (isRouletteSpinning) {
            UpdateResult rouletteResult = updateRoulette();
            if (rouletteResult != null) {
                return rouletteResult;
            }
        }
        
        SpecialItem item = board.getSpecialItem();
        if (item != null && item.getPosition().equals(newHead)) {
            board.removeSpecialItem();
            itemsCollected++;
            startRoulette();
            result = new UpdateResult(UpdateResult.Type.ITEM_COLLECTED, newHead);
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
        isRouletteSpinning = true;
        rouletteEndTime = System.currentTimeMillis() + 3000;
    }
    
    private UpdateResult updateRoulette() {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime >= rouletteEndTime) {
            isRouletteSpinning = false;
            return applyRandomEffect();
        } else {
            // 룰렛 도는 중: 랜덤 텍스트 보여주기
            Random r = new Random();
            int randIdx = r.nextInt(rouletteOptions.length);
            currentRouletteText = rouletteOptions[randIdx];
            currentRoulettePositive = isPositiveEffect(randIdx); // 색상 결정을 위해 저장
            return null;
        }
    }
    
    // [추가] 긍정 효과인지 판별하는 헬퍼 메서드
    private boolean isPositiveEffect(int index) {
        // 1(Slow), 2(Score), 5(Scissors) -> Positive
        // 0(Fast), 3(Reverse), 4(Darkness) -> Negative
        return index == 1 || index == 2 || index == 5;
    }
    
    private UpdateResult applyRandomEffect() {
        int rand = new Random().nextInt(6);
        long currentTime = System.currentTimeMillis();
        String effectName = "";
        
        // 긍정/부정 여부 판단
        boolean isPositive = isPositiveEffect(rand);
        
        switch (rand) {
            case 0: // SPEED UP (Bad)
                isSpeedUp = true; isSpeedDown = false; speedEffectEndTime = currentTime + 5000; 
                effectName = "SPEED UP! (BAD)"; break;
            case 1: // SPEED DOWN (Good)
                isSpeedDown = true; isSpeedUp = false; speedEffectEndTime = currentTime + 5000; 
                effectName = "SPEED DOWN (GOOD)"; break;
            case 2: // X2 SCORE (Good)
                doubleScoreEndTime = currentTime + 10000; 
                effectName = "DOUBLE SCORE (10s)"; break;
            case 3: // REVERSE (Bad)
                reverseInputEndTime = currentTime + 5000; 
                effectName = "REVERSE CONTROL (5s)"; break;
            case 4: // DARKNESS (Bad)
                reducedVisionEndTime = currentTime + 5000; 
                effectName = "DARKNESS (5s)"; break;
            case 5: // SCISSORS (Good)
                snake.cutTail(3); 
                effectName = "SCISSORS (CUT TAIL)"; break;
        }
        
        // 최종 결정된 텍스트와 긍정 여부 저장
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
    public boolean isRouletteSpinning() { return isRouletteSpinning; }
    public String getCurrentRouletteText() { return currentRouletteText; }
    public boolean isCurrentRoulettePositive() { return currentRoulettePositive; } // [추가]

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