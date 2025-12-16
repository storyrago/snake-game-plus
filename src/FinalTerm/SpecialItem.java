package FinalTerm;


import java.awt.Point;

public class SpecialItem {
 private Point position;
 private String type;
 
 public SpecialItem(int x, int y, String type) {
     this.position = new Point(x, y);
     this.type = type;
 }
 
 public Point getPosition() { return position; }
 public String getType() { return type; }
}

