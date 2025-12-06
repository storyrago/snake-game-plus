package FinalTerm;

//Food.java

import java.awt.Point;

public class Food {
 private Point position;
 private String type;
 private int points;
 
 public Food(int x, int y, String type) {
     this.position = new Point(x, y);
     this.type = type;
     this.points = "rare".equals(type) ? 30 : 10;
 }
 
 public Point getPosition() { return position; }
 public String getType() { return type; }
 public int getPoints() { return points; }
}

