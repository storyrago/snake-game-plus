package FinalTerm;

//Snake.java - 뱀 엔티티 관리

import java.awt.Point;
import java.util.*;

public class Snake {
    private List<Point> body;
    private Point direction;
    private Queue<Point> directionQueue;
    
    public Snake(Point startPosition) {
        body = new ArrayList<>();
        body.add(new Point(startPosition));
        direction = new Point(1, 0);
        directionQueue = new LinkedList<>();
    }
    
    // 다음 머리 위치만 계산 (실제로 이동하지 않음)
    public Point getNextHeadPosition() {
        Point currentDirection = direction;
        if (!directionQueue.isEmpty()) {
            currentDirection = directionQueue.peek();
        }
        
        Point head = body.get(0);
        return new Point(
            head.x + currentDirection.x,
            head.y + currentDirection.y
        );
    }
    
    // 실제 이동 수행
    public void moveTo(Point newHead) {
        if (!directionQueue.isEmpty()) {
            direction = directionQueue.poll();
        }
        
        body.add(0, newHead);
        body.remove(body.size() - 1);
    }
    
    public void grow() {
        Point tail = body.get(body.size() - 1);
        body.add(new Point(tail));
    }
    
    public void cutTail(int cutLength) {
        int newSize = Math.max(3, body.size() - cutLength);
        body = new ArrayList<>(body.subList(0, newSize));
    }
    
    // 머리가 추가되기 전에 체크해야 함
    public boolean checkSelfCollision(Point head) {
        for (int i = 0; i < body.size(); i++) {
            if (body.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }
    
    public void setHead(Point head) {
        body.set(0, head);
    }
    
    public boolean queueDirection(Point newDir) {
        Point currentDir = directionQueue.isEmpty() ? direction : 
                          directionQueue.toArray(new Point[0])[directionQueue.size() - 1];
        
        // 반대 방향 체크
        if (newDir.x == -currentDir.x && newDir.y == -currentDir.y) return false;
        if (newDir.x == currentDir.x && newDir.y == currentDir.y) return false;
        
        if (directionQueue.size() < 2) {
            directionQueue.offer(newDir);
            return true;
        }
        return false;
    }
    
    public List<Point> getBody() { return new ArrayList<>(body); }
    public int getLength() { return body.size(); }
    public Point getHead() { return body.get(0); }
}
