package FinalTerm;

public class PlayerData {
    private static String currentPlayerName = "Player";
    
    public static void setPlayerName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            currentPlayerName = name.trim();
        }
    }
    
    public static String getPlayerName() {
        return currentPlayerName;
    }
}

