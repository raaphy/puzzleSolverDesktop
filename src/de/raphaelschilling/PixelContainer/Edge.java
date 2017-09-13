package de.raphaelschilling.PixelContainer;

import java.util.ArrayList;

public class Edge {
    ArrayList<BorderPixel> borderPixels = null;
    private int start;
    private int end;

    public Edge(ArrayList<BorderPixel> borderPixels, int start, int end) {
        this.borderPixels = borderPixels;
        this.start = start;
        this.end = end;
    }

    public Edge normalize() {
        double xDifference = borderPixels.get(end).x - borderPixels.get(start).x;
        double yDifference = borderPixels.get(end).y - borderPixels.get(start).y;
        double originalAngular = Math.atan2(yDifference, xDifference);

        System.out.println("x: " + borderPixels.get(start).x + " y: " + borderPixels.get(start).y + " originalAngular: " + originalAngular);

        for (int i = start + borderPixels.size(); i <= end + borderPixels.size(); i++) {

        }
        return null;
    }
}
