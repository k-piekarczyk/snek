package snek;

import java.awt.*;
import java.util.Random;

public class Food {
    private final static Color[] COLORS = {
            new Color(0xC45050), // BITTERSWEET SHIMMER
            new Color(0xEDD89C), // TUSCAN
            new Color(0x85DB98), // LIGHT GREEN
    };

    private Color color;
    private Point point;

    public Food(Point point) {
        color = COLORS[new Random().nextInt(COLORS.length)];
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        color = COLORS[new Random().nextInt(COLORS.length)];
        this.point = point;
    }

    public Color getColor() {
        return color;
    }
}
