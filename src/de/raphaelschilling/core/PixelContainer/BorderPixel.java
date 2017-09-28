package de.raphaelschilling.core.PixelContainer;

public class BorderPixel {
    private final int id;
    public int x;
    public int y;
    public float accuracy;

    public BorderPixel(int x, int y, float accuracy, int id) {
        this.x = x;
        this.y = y;
        this.accuracy = accuracy;
        this.id = id;
    }
}
