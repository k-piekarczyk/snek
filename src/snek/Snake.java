package snek;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Snake implements KeyListener {
    private enum Direction {UP, DOWN, LEFT, RIGHT}

    public final static Color COLOR = new Color(0x424242); // ARSENIC
    public final static Color COLLISION_COLOR = new Color(0xC45050); // ARSENIC
    public final static int INITIAL_LENGTH = 3;

    private List<Point> body;
    private Direction currentDirection = Direction.RIGHT;
    private Direction pendingDirection = Direction.RIGHT;
    private Grid grid;

    public Snake(Grid grid, Point point) {
        this.grid = grid;
        body = new ArrayList<>();

        for (int i = 0; i < INITIAL_LENGTH; i++) {
            body.add(this.grid.wrap(new Point((int) point.getX() - i, (int) point.getX())));
        }
    }

    public Point getHead() {
        return body.get(0);
    }

    public List<Point> getBody() {
        return body;
    }

    public void extend() {
        Direction growDirection;
        Point grownTail;

        Point almostLast = body.get(body.size() - 2);
        Point last = body.get(body.size() - 1);

        if (almostLast.getX() - last.getX() < 0) growDirection = Direction.RIGHT;
        else if (almostLast.getX() - last.getX() > 0) growDirection = Direction.LEFT;
        else if (almostLast.getY() - last.getY() < 0) growDirection = Direction.DOWN;
        else growDirection = Direction.UP;

        if (growDirection == Direction.UP) grownTail = new Point((int) last.getX(), (int) last.getY() - 1);
        else if (growDirection == Direction.DOWN) grownTail = new Point((int) last.getX(), (int) last.getY() + 1);
        else if (growDirection == Direction.LEFT) grownTail = new Point((int) last.getX() - 1, (int) last.getY());
        else grownTail = new Point((int) last.getX() + 1, (int) last.getY());

        body.add(grid.wrap(grownTail));
    }

    public void move() {
        Point prev = (Point) getHead().clone();

        currentDirection = pendingDirection;

        switch (currentDirection) {
            case UP:
                body.set(0, grid.wrap(new Point((int) prev.getX(), (int) prev.getY() - 1)));
                break;
            case DOWN:
                body.set(0, grid.wrap(new Point((int) prev.getX(), (int) prev.getY() + 1)));
                break;
            case LEFT:
                body.set(0, grid.wrap(new Point((int) prev.getX() - 1, (int) prev.getY())));
                break;
            case RIGHT:
                body.set(0, grid.wrap(new Point((int) prev.getX() + 1, (int) prev.getY())));
                break;
        }

        for (int i = 1; i < body.size(); i++) {
            Point temp = (Point) body.get(i).clone();
            body.set(i, prev);
            prev = temp;
        }

    }

    public int getSize() {
        return body.size();
    }

    public boolean didCollide() {
        for (int i = 1; i < body.size(); i++) {
            if (body.get(i).equals(getHead())) return true;
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        switch (c) {
            case KeyEvent.VK_UP:
                if (currentDirection != Direction.DOWN) pendingDirection = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                if (currentDirection != Direction.UP) pendingDirection = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                if (currentDirection != Direction.RIGHT) pendingDirection = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                if (currentDirection != Direction.LEFT) pendingDirection = Direction.RIGHT;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
