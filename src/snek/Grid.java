package snek;

import java.awt.*;
import java.util.Random;

public class Grid {
    static final int SIZE = 10;

    public final static Color COLOR = new Color(0x424242); // ARSENIC

    private final int rows;
    private final int cols;

    private Snake snake;
    private Food food;

    public Grid(int width, int height) {
        rows = height / SIZE;
        cols = width / SIZE;

        snake = new Snake(this, new Point(rows / 2, cols / 2));
        food = new Food(getRandomPoint());
    }

    private Point getRandomPoint() {
        Random random = new Random();
        Point point;

        do {
            point = new Point(random.nextInt(rows), random.nextInt(cols));
        } while (point.equals(snake.getHead()));

        return point;
    }

    public Point wrap(Point point) {
        int x = (int) point.getX();
        int y = (int) point.getY();

        if (x >= rows) x = x - rows;
        if (y >= cols) y = y - cols;

        if (x < 0) x = rows - x - 1;
        if (y < 0) y = cols - y - 1;

        return new Point(x, y);
    }

    public boolean update() {
        if (food.getPoint().equals(snake.getHead())) {
            snake.extend();
            snake.move();
            food.setPoint(getRandomPoint());
        } else {
            snake.move();
        }

        return !snake.didCollide();
    }

    public Snake getSnake() {
        return snake;
    }


    public Food getFood() {
        return food;
    }


    public int getRealX(Point point) {
        return (int) point.getX() * SIZE;
    }

    public int getRealY(Point point) {
        return (int) point.getY() * SIZE;
    }
}
