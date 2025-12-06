package FinalTerm;

//GameBoard.java - 게임 보드 및 오브젝트 관리

import java.awt.Point;
import java.util.*;

public class GameBoard {
    private static final int GRID_SIZE = 20;
    private Food food;
    private SpecialItem specialItem;
    private List<Point> obstacles;
    private Portal[] portals;
    private Random random;
    
    public GameBoard() {
        obstacles = new ArrayList<>();
        random = new Random();
    }
    
    public void spawnFood(Snake snake) {
        Point pos = getRandomPosition(snake);
        String type = Math.random() < 0.3 ? "rare" : "normal";
        food = new Food(pos.x, pos.y, type);
    }
    
    public void createObstacle(Snake snake) {
        Point pos = getRandomPosition(snake);
        obstacles.add(pos);
    }
    
    public void createSpecialItem(Snake snake) {
        Point pos = getRandomPosition(snake);
        String type = Math.random() < 0.5 ? "hammer" : "scissors";
        specialItem = new SpecialItem(pos.x, pos.y, type);
    }
    
    public void createPortals(Snake snake) {
        Point pos1 = getRandomPosition(snake);
        Point pos2 = getRandomPosition(snake);
        portals = new Portal[] {
            new Portal(pos1.x, pos1.y, "magenta"),
            new Portal(pos2.x, pos2.y, "cyan")
        };
    }
    
    public Point checkPortal(Point position) {
        if (portals == null) return null;
        
        for (int i = 0; i < portals.length; i++) {
            if (portals[i].getPosition().equals(position)) {
                return portals[1 - i].getPosition();
            }
        }
        return null;
    }
    
    public boolean isOutOfBounds(Point position) {
        return position.x < 0 || position.x >= GRID_SIZE || 
               position.y < 0 || position.y >= GRID_SIZE;
    }
    
    public boolean hasObstacle(Point position) {
        return obstacles.contains(position);
    }
    
    public void removeObstacle(Point position) {
        obstacles.remove(position);
    }
    
    public void removeSpecialItem() {
        specialItem = null;
    }
    
    private Point getRandomPosition(Snake snake) {
        Point pos;
        int attempts = 0;
        do {
            pos = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
            attempts++;
        } while (attempts < 100 && isOccupied(pos, snake));
        return pos;
    }
    
    private boolean isOccupied(Point pos, Snake snake) {
        // 뱀 위치 체크
        if (snake != null) {
            for (Point p : snake.getBody()) {
                if (p.equals(pos)) return true;
            }
        }
        
        if (obstacles.contains(pos)) return true;
        if (food != null && food.getPosition().equals(pos)) return true;
        if (specialItem != null && specialItem.getPosition().equals(pos)) return true;
        if (portals != null) {
            for (Portal p : portals) {
                if (p.getPosition().equals(pos)) return true;
            }
        }
        return false;
    }
    
    // Getters
    public Food getFood() { return food; }
    public SpecialItem getSpecialItem() { return specialItem; }
    public List<Point> getObstacles() { return new ArrayList<>(obstacles); }
    public Portal[] getPortals() { return portals; }
    public boolean hasSpecialItem() { return specialItem != null; }
    public int getObstacleCount() { return obstacles.size(); }
    public static int getGridSize() { return GRID_SIZE; }
}
