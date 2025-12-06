package FinalTerm;


public class GameSettings {
    private static GameDifficulty difficulty = GameDifficulty.NORMAL;
    private static GameDifficulty displayDifficulty = GameDifficulty.NORMAL; // ·©Å· Ç¥½Ã¿ë
    private static GameMode mode = GameMode.CLASSIC;
    private static int timeLimit = 60;
    
    public static void setDifficulty(GameDifficulty diff) {
        difficulty = diff;
        displayDifficulty = diff;
    }
    
    public static void setDifficultyForDisplay(GameDifficulty diff) {
        displayDifficulty = diff;
    }
    
    public static GameDifficulty getDifficulty() {
        return difficulty;
    }
    
    public static GameDifficulty getDisplayDifficulty() {
        return displayDifficulty;
    }
    
    public static void setMode(GameMode m) {
        mode = m;
    }
    
    public static GameMode getMode() {
        return mode;
    }
    
    public static void setTimeLimit(int seconds) {
        timeLimit = seconds;
    }
    
    public static int getTimeLimit() {
        return timeLimit;
    }
}
