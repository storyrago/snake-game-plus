package FinalTerm;


public enum GameDifficulty {
    EASY(200, 100, "Easy"),       // �ʱ� �ӵ�, �ּ� �ӵ�, �̸�
    NORMAL(150, 50, "Normal"),
    HARD(100, 30, "Hard");
    
    private final int initialDelay;
    private final int minDelay;
    private final String displayName;
    
    GameDifficulty(int initialDelay, int minDelay, String displayName) {
        this.initialDelay = initialDelay;
        this.minDelay = minDelay;
        this.displayName = displayName;
    }
    
    public int getInitialDelay() { return initialDelay; }
    public int getMinDelay() { return minDelay; }
    public String getDisplayName() { return displayName; }
}
