package FinalTerm;

import java.awt.Point;

public class UpdateResult {
    public enum Type {
        NORMAL_MOVE,
        FOOD_EATEN,
        ITEM_COLLECTED,     // 룰렛 시작
        ROULETTE_FINISHED,  // 룰렛 종료 (결과 적용)
        OBSTACLE_DESTROYED,
        PORTAL_USED,
        WALL_COLLISION,
        SELF_COLLISION,
        OBSTACLE_COLLISION
    }
    
    private Type type;
    private Point position;
    private int combo;
    private String message;
    private boolean isPositive; // [추가] 긍정 효과 여부
    
    public UpdateResult(Type type, Point position) {
        this(type, position, 0, null, true);
    }
    
    public UpdateResult(Type type, Point position, int combo) {
        this(type, position, combo, null, true);
    }
    
    public UpdateResult(Type type, Point position, int combo, String message) {
        this(type, position, combo, message, true);
    }
    
    // [추가] 긍정/부정 여부를 포함하는 생성자
    public UpdateResult(Type type, Point position, int combo, String message, boolean isPositive) {
        this.type = type;
        this.position = position;
        this.combo = combo;
        this.message = message;
        this.isPositive = isPositive;
    }
    
    public Type getType() { return type; }
    public Point getPosition() { return position; }
    public int getCombo() { return combo; }
    public String getMessage() { return message; }
    public boolean isPositive() { return isPositive; } // Getter
    
    public boolean isCollision() {
        return type == Type.WALL_COLLISION || 
               type == Type.SELF_COLLISION || 
               type == Type.OBSTACLE_COLLISION;
    }
}