package snek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

public class Game extends JPanel implements KeyListener {

    private Grid grid;
    private GameInfo gameInfo;
    private boolean end = false;
    private boolean again = false;
    private boolean paused = false;

    private static int frameRate = 25;
    private static float interval = 1000.0f / frameRate;

    private Game(int width, int height) {

        if (width < 250 || height < 250) {
            System.exit(1);
        }

        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);

        grid = new Grid(width, height);
        gameInfo = new GameInfo(width);

        addKeyListener(this);
        addKeyListener(grid.getSnake());
    }

    private class GameInfo extends JPanel {
        private final Color bckgrnd = new Color(0xEBEBEB); // ISABELLINE
        private final Color foregrnd = new Color(0x424242); // ARSENIC

        private final int scoreMultiplier = 5;

        private int score;

        GameInfo(int width) {
            setSize(width, 28);
            setPreferredSize(new Dimension(width, 23));
        }

        public void paint(Graphics g) {
            super.paint(g);

            g.setColor(bckgrnd);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(foregrnd);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            g.drawString("Score: " + score, 10, 16);

            String rEscPrompt = "R to restart   |   ESC to exit";

            g.drawString(rEscPrompt, getWidth() - g.getFontMetrics().stringWidth(rEscPrompt) - 10, 16);
        }

        public void setScore(int snakeLength) {
            this.score = (snakeLength - Snake.INITIAL_LENGTH) * scoreMultiplier;
        }

        public int getScore() {
            return score;
        }
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
            g.setColor(Grid.COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw snake with grid color
            g.setColor(Snake.COLOR_MUTED);
            for (Point point : grid.getSnake().getBody()) {
                drawPoint(g, point);
            }

            // Draw snake head
            g.setColor(Snake.COLLISION_COLOR);
            drawPoint(g, grid.getSnake().getHead());

            Color msgBoxColor = new Color(0xEB0000); // ISABELLINE
            Color msgBoxShadowColor = new Color(0x340000); // ISABELLINE
            Color msgTextColor = new Color(0xFFFFFF); // ARSENIC

            int msgBoxWidth = 300;
            int msgBoxHeight = 25;
            int msgHeight = 20;
            String msg = String.format("Your score: %d", gameInfo.getScore());

            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, msgHeight));
            int msgWidth = g.getFontMetrics().stringWidth(msg);

            g.setColor(msgBoxShadowColor);
            g.fillRect((getWidth() - msgBoxWidth) / 2 + 8, (getHeight() - msgBoxHeight) / 2 + 8, msgBoxWidth, msgBoxHeight);

            g.setColor(msgBoxColor);
            g.fillRect((getWidth() - msgBoxWidth) / 2, (getHeight() - msgBoxHeight) / 2, msgBoxWidth, msgBoxHeight);

            g.setColor(msgTextColor);
            g.drawString(msg, (getWidth() - msgWidth) / 2, (getHeight() - msgHeight) / 2 + 4);

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
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

            Game game = new Game(700, 700);
            container.add(game);
            container.add(game.gameInfo);
            frame.add(container);
            frame.pack();
            frame.setVisible(true);

            while (!game.end) {
                float time = System.currentTimeMillis();

                if (game.grid.update()) {
                    game.gameInfo.setScore(game.grid.getSnake().getSize());
                    game.repaint();
                    game.gameInfo.repaint();

                    time = System.currentTimeMillis() - time;

                    if (time < interval) {
                        try {
                            Thread.sleep((long) (interval - time));
                        } catch (InterruptedException ignore) {
                        }
                    }
                } else {
                    game.end = true;
                    game.gameInfo.setScore(game.grid.getSnake().getSize() - Snake.INITIAL_LENGTH);
                    game.repaint();
                    game.gameInfo.repaint();
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
            frame.remove(container);
        } while (again);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
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
