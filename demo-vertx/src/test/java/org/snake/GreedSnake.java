/*
 * Created on 2005-4-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.snake;

/**
 * @author cheess
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GreedSnake {
    public static void main(String[] args) {
        SnakeModel model = new SnakeModel(20,30);
        SnakeControl control = new SnakeControl(model);
        SnakeView view = new SnakeView(model,control);
        model.addObserver(view);
        (new Thread(model)).start();
    }
}
