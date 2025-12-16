package FinalTerm;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String FILE_NAME = "scores.dat";
    private Map<String, List<ScoreRecord>> scores;
    
    public ScoreManager() {
        scores = new HashMap<>();
        loadScores();
    }
    
    public void addScore(String playerName, GameMode mode, GameDifficulty difficulty, 
                        int score, int maxCombo, long playTime, int foodEaten, int itemsCollected) {
        String key = mode.name() + "_" + difficulty.name();
        List<ScoreRecord> list = scores.computeIfAbsent(key, k -> new ArrayList<>());
        
        // [수정] 중복 이름 확인 및 최고 점수 갱신 로직
        ScoreRecord existingRecord = null;
        for (ScoreRecord record : list) {
            if (record.playerName.equals(playerName)) {
                existingRecord = record;
                break;
            }
        }

        if (existingRecord != null) {
            // 이미 존재하는 이름인 경우
            if (score > existingRecord.score) {
                // 신기록이 더 높으면 기존 기록 삭제 후 새 기록 추가
                list.remove(existingRecord);
                list.add(new ScoreRecord(playerName, score, maxCombo, playTime, foodEaten, itemsCollected));
            }
            // 신기록이 낮거나 같으면 무시 (기존 점수 유지)
        } else {
            // 새 이름이면 그냥 추가
            list.add(new ScoreRecord(playerName, score, maxCombo, playTime, foodEaten, itemsCollected));
        }
        
        // 점수 내림차순 정렬
        Collections.sort(list, (a, b) -> b.score - a.score);
        
        // 상위 10개만 유지
        if (list.size() > 10) {
            scores.put(key, new ArrayList<>(list.subList(0, 10)));
        }
        
        saveScores();
    }
    
    public List<ScoreRecord> getHighScores(GameMode mode, GameDifficulty difficulty) {
        String key = mode.name() + "_" + difficulty.name();
        return scores.getOrDefault(key, new ArrayList<>());
    }
    
    public int getTopScore(GameMode mode, GameDifficulty difficulty) {
        List<ScoreRecord> list = getHighScores(mode, difficulty);
        if (list.isEmpty()) return 0;
        return list.get(0).score;
    }
    
    public void clearScores() {
        scores.clear();
        saveScores();
    }
    
    @SuppressWarnings("unchecked")
    private void loadScores() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                scores = (Map<String, List<ScoreRecord>>) ois.readObject();
            } catch (Exception e) {
                scores = new HashMap<>();
            }
        }
    }
    
    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static class ScoreRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        public String playerName;
        public int score;
        public int maxCombo;
        public long playTime;
        public int foodEaten;
        public int itemsCollected;
        public long date;
        
        public ScoreRecord(String name, int score, int combo, long time, int food, int items) {
            this.playerName = name;
            this.score = score;
            this.maxCombo = combo;
            this.playTime = time;
            this.foodEaten = food;
            this.itemsCollected = items;
            this.date = System.currentTimeMillis();
        }
    }
}