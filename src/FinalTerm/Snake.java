package FinalTerm;

//Snake.java - �� ��ƼƼ ����

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
    
    // ���� �Ӹ� ��ġ�� ��� (������ �̵����� ����)
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
    
    // ���� �̵� ����
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
        // 1. 현재 길이가 3 이하라면 자를 수 없으므로 아무것도 하지 않고 종료
        if (body.size() <= 3) {
            return;
        }

        int targetSize = body.size() - cutLength;
        
        // 2. 최소 길이는 3으로 유지 (3보다 작아지면 3으로 설정)
        int newSize = Math.max(3, targetSize);
        
        // 3. (안전장치) 혹시라도 newSize가 현재 크기보다 크면 현재 크기로 맞춤
        newSize = Math.min(newSize, body.size());

        body = new ArrayList<>(body.subList(0, newSize));
    }
    
    // �Ӹ��� �߰��Ǳ� ���� üũ�ؾ� ��
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
        
        // �ݴ� ���� üũ
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
