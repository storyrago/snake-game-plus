package FinalTerm;

//Portal.java

import java.awt.Point;

public class Portal {
 private Point position;
 private String color;
 
 public Portal(int x, int y, String color) {
     this.position = new Point(x, y);
     this.color = color;
 }
 
 public Point getPosition() { return position; }
 public String getColor() { return color; }
}

