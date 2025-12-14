package FinalTerm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private static final int GRID_SIZE = 20;
    
    private int width;
    private int height;
    private List<Point> obstacles;
    private Portal[] portals;
    private Food food;
    
    private SpecialItem specialItem;
    private SpecialItem hammer;
    
    private Random random;
    
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.obstacles = new ArrayList<>();
        this.random = new Random();
        initBoard();
    }
    
    private void initBoard() {
        generateObstacles();
    }
    
    private void generateObstacles() {
        int difficultyLevel = 0;
        try { difficultyLevel = GameSettings.getDifficulty().ordinal(); } catch(Exception e) {}
        
        int obstacleCount = 5 + difficultyLevel * 3;
        for (int i = 0; i < obstacleCount; i++) {
            spawnObstacleInternal(new ArrayList<>());
        }
    }
    
    public void spawnObstacle(List<Point> snakeBody) {
        spawnObstacleInternal(snakeBody);
    }
    
    private void spawnObstacleInternal(List<Point> snakeBody) {
        Point pos;
        Point startPoint = new Point(10, 10);
        int attempts = 0;
        
        do {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            pos = new Point(x, y);
            attempts++;
        } while ((isSnake(pos, snakeBody) || isWall(pos) || isObstacle(pos) || 
                 (food != null && pos.equals(food.getPosition())) || checkPortal(pos) != null ||
                 (specialItem != null && pos.equals(specialItem.getPosition())) ||
                 (hammer != null && pos.equals(hammer.getPosition())) ||
                 (!snakeBody.isEmpty() && pos.distance(snakeBody.get(0)) < 3) ||
                 pos.distance(startPoint) < 5) 
                 && attempts < 100);
        
        if (attempts < 100) {
            obstacles.add(pos);
        }
    }
    
    public void spawnHammer(List<Point> snakeBody) {
        if (hammer != null) return;
        
        Point pos;
        int attempts = 0;
        do {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            pos = new Point(x, y);
            attempts++;
        } while ((isSnake(pos, snakeBody) || isWall(pos) || isObstacle(pos) || 
                 (food != null && pos.equals(food.getPosition())) || 
                 (specialItem != null && pos.equals(specialItem.getPosition())) ||
                 checkPortal(pos) != null) && attempts < 100);
        
        if (attempts < 100) {
            hammer = new SpecialItem(pos.x, pos.y, "hammer");
        }
    }
    
    public void removeHammer() { this.hammer = null; }

    public void spawnPortals(List<Point> snakeBody) {
        portals = new Portal[2];
        for (int i = 0; i < 2; i++) {
            Point p;
            int attempts = 0;
            do {
                p = new Point(random.nextInt(width), random.nextInt(height));
                attempts++;
            } while ((isSnake(p, snakeBody) || isObstacle(p) || isWall(p) ||
                     (food != null && p.equals(food.getPosition())) ||
                     (specialItem != null && p.equals(specialItem.getPosition())) ||
                     (hammer != null && p.equals(hammer.getPosition())) ||
                     (i == 1 && p.distance(portals[0].getPosition()) < 5)) 
                     && attempts < 100);
            
            if (attempts < 100) {
                // [수정] "cyan" -> "blue"
                portals[i] = new Portal(p.x, p.y, i == 0 ? "purple" : "blue");
            } else {
                portals = null; return;
            }
        }
    }
    
    public void removePortals() { this.portals = null; }

    public void spawnFood(List<Point> snakeBody) {
        Point pos;
        do {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            pos = new Point(x, y);
        } while (isSnake(pos, snakeBody) || isWall(pos) || isObstacle(pos) || 
                 (hammer != null && pos.equals(hammer.getPosition())) ||
                 checkPortal(pos) != null);
        
        String type = random.nextInt(10) == 0 ? "rare" : "normal";
        food = new Food(pos.x, pos.y, type);
    }
    
    public void spawnSpecialItem(List<Point> snakeBody) {
        Point pos;
        do {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            pos = new Point(x, y);
        } while (isSnake(pos, snakeBody) || isWall(pos) || isObstacle(pos) || 
                 (food != null && pos.equals(food.getPosition())) || 
                 (hammer != null && pos.equals(hammer.getPosition())) ||
                 checkPortal(pos) != null);
        
        specialItem = new SpecialItem(pos.x, pos.y, "random_box");
    }
    
    private boolean isSnake(Point p, List<Point> snakeBody) {
        for (Point part : snakeBody) { if (part.equals(p)) return true; }
        return false;
    }
    
    public boolean isWall(Point p) { return p.x < 0 || p.x >= width || p.y < 0 || p.y >= height; }
    
    public boolean isObstacle(Point p) {
        for (Point obs : obstacles) { if (obs.equals(p)) return true; }
        return false;
    }
    
    public Portal checkPortal(Point p) {
        if (portals == null) return null;
        for (Portal portal : portals) { if (portal.getPosition().equals(p)) return portal; }
        return null;
    }
    
    public Point getPortalDestination(Portal entry) {
        if (portals == null) return null;
        return entry == portals[0] ? portals[1].getPosition() : portals[0].getPosition();
    }
    
    public void removeObstacle(Point p) { obstacles.remove(p); }
    public void removeSpecialItem() { specialItem = null; }
    
    public List<Point> getObstacles() { return obstacles; }
    public Portal[] getPortals() { return portals; }
    public Food getFood() { return food; }
    public SpecialItem getSpecialItem() { return specialItem; }
    public SpecialItem getHammer() { return hammer; }
    public void setSpecialItem(SpecialItem item) { this.specialItem = item; }
    
    public static int getGridSize() { return GRID_SIZE; }
}