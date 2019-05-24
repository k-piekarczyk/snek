package snek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements KeyListener {

    private Grid grid;
    private boolean end = false;

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
        // Clear screen
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
    }

    private static void run() {
        Game game = new Game(500, 500);

        JFrame frame = new JFrame();
        frame.setTitle("snek");
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        while (!game.end) {
            float time = System.currentTimeMillis();

            game.grid.update();
            game.repaint();

            time = System.currentTimeMillis() - time;

            if (time < interval) {
                try {
                    Thread.sleep((long) (interval - time));
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    public static void main(String[] args) {
        run();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            end = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
