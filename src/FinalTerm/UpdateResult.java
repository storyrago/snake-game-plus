package FinalTerm;

//UpdateResult.java - 업데이트 결과 전달

import java.awt.Point;

public class UpdateResult {
 public enum Type {
     NORMAL_MOVE,
     FOOD_EATEN,
     ITEM_COLLECTED,
     OBSTACLE_DESTROYED,
     PORTAL_USED,
     WALL_COLLISION,
     SELF_COLLISION,
     OBSTACLE_COLLISION
 }
 
 private Type type;
 private Point position;
 private int combo;
 
 public UpdateResult(Type type, Point position) {
     this(type, position, 0);
 }
 
 public UpdateResult(Type type, Point position, int combo) {
     this.type = type;
     this.position = position;
     this.combo = combo;
 }
 
 public Type getType() { return type; }
 public Point getPosition() { return position; }
 public int getCombo() { return combo; }
 
 public boolean isCollision() {
     return type == Type.WALL_COLLISION || 
            type == Type.SELF_COLLISION || 
            type == Type.OBSTACLE_COLLISION;
 }
}

