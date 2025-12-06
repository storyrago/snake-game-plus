package FinalTerm;

//ScoreManager.java - 점수 및 기록 관리

import java.util.*;
import java.io.*;

public class ScoreManager {
    // 모드와 난이도별로 랭킹 관리
    private Map<String, List<ScoreRecord>> rankingsByModeAndDifficulty;
    private static final String SAVE_FILE = "snake_scores.dat";
    
    public ScoreManager() {
        rankingsByModeAndDifficulty = new HashMap<>();
        loadScores();
    }
    
    private String getRankingKey(GameMode mode, GameDifficulty difficulty) {
        return mode.name() + "_" + difficulty.name();
    }
    
    public void addScore(String playerName, GameMode mode, GameDifficulty difficulty, 
                        int score, int maxCombo, int playTime, int foodEaten, int itemsCollected) {
        
        String key = getRankingKey(mode, difficulty);
        List<ScoreRecord> rankings = rankingsByModeAndDifficulty.computeIfAbsent(key, k -> new ArrayList<>());
        
        ScoreRecord newRecord = new ScoreRecord(playerName, mode, difficulty, score, maxCombo, playTime, foodEaten, itemsCollected);
        
        // 같은 이름의 기록 찾기
        ScoreRecord existingRecord = null;
        for (ScoreRecord record : rankings) {
            if (record.playerName.equals(playerName)) {
                existingRecord = record;
                break;
            }
        }
        
        // 같은 이름이 있으면
        if (existingRecord != null) {
            // 새 점수가 더 높을 때만 교체
            if (newRecord.score > existingRecord.score) {
                rankings.remove(existingRecord);
                rankings.add(newRecord);
            }
        } else {
            // 같은 이름이 없으면 새로 추가
            rankings.add(newRecord);
        }
        
        // 점수순으로 정렬
        rankings.sort((a, b) -> Integer.compare(b.score, a.score));
        
        // 상위 10개만 유지
        if (rankings.size() > 10) {
            rankingsByModeAndDifficulty.put(key, new ArrayList<>(rankings.subList(0, 10)));
        }
        
        saveScores();
    }
    
    // 특정 모드 + 난이도의 랭킹 가져오기
    public List<ScoreRecord> getHighScores(GameMode mode, GameDifficulty difficulty) {
        String key = getRankingKey(mode, difficulty);
        return new ArrayList<>(rankingsByModeAndDifficulty.getOrDefault(key, new ArrayList<>()));
    }
    
    // 특정 모드의 모든 난이도 랭킹 가져오기
    public Map<GameDifficulty, List<ScoreRecord>> getHighScoresByMode(GameMode mode) {
        Map<GameDifficulty, List<ScoreRecord>> result = new HashMap<>();
        for (GameDifficulty diff : GameDifficulty.values()) {
            result.put(diff, getHighScores(mode, diff));
        }
        return result;
    }
    
    public void clearScores() {
        rankingsByModeAndDifficulty.clear();
        try {
            java.io.File file = new java.io.File(SAVE_FILE);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.err.println("Failed to delete score file: " + e.getMessage());
        }
    }
    
    public void clearScores(GameMode mode) {
        // 해당 모드의 모든 난이도 랭킹 삭제
        for (GameDifficulty diff : GameDifficulty.values()) {
            String key = getRankingKey(mode, diff);
            rankingsByModeAndDifficulty.remove(key);
        }
        saveScores();
    }
    
    public void clearScores(GameMode mode, GameDifficulty difficulty) {
        String key = getRankingKey(mode, difficulty);
        rankingsByModeAndDifficulty.remove(key);
        saveScores();
    }
    
    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(rankingsByModeAndDifficulty);
        } catch (Exception e) {
            System.err.println("Failed to save scores: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            rankingsByModeAndDifficulty = (Map<String, List<ScoreRecord>>) ois.readObject();
        } catch (Exception e) {
            rankingsByModeAndDifficulty = new HashMap<>();
        }
    }
    
    public static class ScoreRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String playerName;
        public final GameMode mode;
        public final GameDifficulty difficulty;
        public final int score;
        public final int maxCombo;
        public final int playTime;
        public final int foodEaten;
        public final int itemsCollected;
        
        public ScoreRecord(String playerName, GameMode mode, GameDifficulty difficulty,
                          int score, int maxCombo, int playTime, int foodEaten, int itemsCollected) {
            this.playerName = playerName;
            this.mode = mode;
            this.difficulty = difficulty;
            this.score = score;
            this.maxCombo = maxCombo;
            this.playTime = playTime;
            this.foodEaten = foodEaten;
            this.itemsCollected = itemsCollected;
        }
    }
}
