package FinalTerm;

//ThemeManager.java - 테마 관리

import java.awt.Color;
import java.util.*;

public class ThemeManager {
    private Map<String, ColorTheme> themes;
    private String currentTheme;
    private String[] themeNames;
    
    public ThemeManager() {
        themes = new HashMap<>();
        initThemes();
        currentTheme = "classic";
        themeNames = new String[]{"classic", "ocean", "neon", "forest"};
    }
    
    private void initThemes() {
        themes.put("classic", new ColorTheme(
            new Color(26, 26, 26),
            new Color(0, 255, 0),
            new Color(255, 0, 0),
            new Color(51, 51, 51)
        ));
        themes.put("ocean", new ColorTheme(
            new Color(10, 37, 64),
            new Color(0, 212, 255),
            new Color(255, 107, 53),
            new Color(26, 58, 90)
        ));
        themes.put("neon", new ColorTheme(
            new Color(15, 15, 35),
            new Color(255, 0, 255),
            new Color(0, 255, 255),
            new Color(42, 42, 74)
        ));
        themes.put("forest", new ColorTheme(
            new Color(26, 47, 26),
            new Color(144, 238, 144),
            new Color(255, 68, 68),
            new Color(45, 74, 45)
        ));
    }
    
    public void cycleTheme() {
        int currentIndex = Arrays.asList(themeNames).indexOf(currentTheme);
        currentTheme = themeNames[(currentIndex + 1) % themeNames.length];
    }
    
    public void setTheme(String themeName) {
        if (themes.containsKey(themeName)) {
            currentTheme = themeName;
        }
    }
    
    public ColorTheme getCurrentTheme() {
        return themes.get(currentTheme);
    }
    
    public static class ColorTheme {
        public final Color background;
        public final Color snake;
        public final Color food;
        public final Color grid;
        
        public ColorTheme(Color bg, Color snake, Color food, Color grid) {
            this.background = bg;
            this.snake = snake;
            this.food = food;
            this.grid = grid;
        }
    }
}
