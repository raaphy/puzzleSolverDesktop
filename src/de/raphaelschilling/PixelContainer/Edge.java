package de.raphaelschilling.PixelContainer;

public class Edge {
    private final Object id;
    public int x;
    public int y;
    public float accuracy;

    public Edge(int x, int y, float accuracy, int id) {
        this.x = x;
        this.y = y;
        this.accuracy = accuracy;
        this.id = id;
    }
}
