package FinalTerm;


import java.awt.Point;
import java.awt.event.*;

public class InputHandler extends KeyAdapter {
    private SnakeGame game;
    
    public InputHandler(SnakeGame game) {
        this.game = game;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_R) {
            game.restart();
            return;
        }
        if (key == KeyEvent.VK_T) {
            game.cycleTheme();
            return;
        }
        if (key == KeyEvent.VK_M) {
            game.toggleSound();
            return;
        }
        if (key == KeyEvent.VK_ENTER) {
            game.handleEnter();
            return;
        }
        
        Point newDir = null;
        switch (key) {
            case KeyEvent.VK_UP:
                newDir = new Point(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                newDir = new Point(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                newDir = new Point(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                newDir = new Point(1, 0);
                break;
        }
        
        if (newDir != null) {
            game.handleDirection(newDir);
        }
    }
}


