package snek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener {

    private Grid grid;
    private boolean end = false;
    private boolean again = false;
    private boolean paused = false;

    private static int frameRate = 20;
    private static float interval = 1000.0f / frameRate;

    private Game(int width, int height) {

        if (width < 250 || height < 250) {
            System.exit(1);
        }

        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);

        grid = new Grid(width, height);

        addKeyListener(this);
        addKeyListener(grid.getSnake());
    }

    private void drawPoint(Graphics g, Point point) {
        g.fillRect(grid.getRealX(point), grid.getRealY(point), Grid.SIZE, Grid.SIZE);
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (!end) {
            // Fill screen with grid color
            g.setColor(Grid.COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw food
            g.setColor(grid.getFood().getColor());
            drawPoint(g, grid.getFood().getPoint());

            // Draw snake
            g.setColor(Snake.COLOR);
            for (Point point : grid.getSnake().getBody()) {
                drawPoint(g, point);
            }
        } else {
            // Fill screen with snake color
            g.setColor(Snake.COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw snake with grid color
            g.setColor(Grid.COLOR);
            for (Point point : grid.getSnake().getBody()) {
                drawPoint(g, point);
            }

            // Draw snake head
            g.setColor(Snake.COLLISION_COLOR);
            drawPoint(g, grid.getSnake().getHead());
        }
    }

    private static void run() {
        JFrame frame = new JFrame();
        frame.setTitle("snek");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        boolean again;
        do {
            Game game = new Game(500, 500);
            frame.add(game);
            frame.pack();
            frame.setVisible(true);

            while (!game.end) {
                float time = System.currentTimeMillis();

                if (game.grid.update()) {
                    game.repaint();

                    time = System.currentTimeMillis() - time;

                    if (time < interval) {
                        try {
                            Thread.sleep((long) (interval - time));
                        } catch (InterruptedException ignore) {
                        }
                    }
                } else {
                    game.end = true;
                    game.repaint();
                    game.paused = true;
                }
            }

            while (game.paused) {
                try {
                    Thread.sleep((long) interval);
                } catch (InterruptedException ignore) {
                }
            }

            again = game.again;
            frame.setVisible(false);
            frame.remove(game);
        } while (again);
    }

    public static void main(String[] args) {
        run();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        switch (c) {
            case KeyEvent.VK_ESCAPE:
                end = true;
                paused = false;
                break;
            case KeyEvent.VK_R:
                again = true;
                end = true;
                paused = false;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
