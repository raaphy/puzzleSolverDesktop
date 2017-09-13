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

    public int[][] normalize() {
        double xDifference = borderPixels.get(end).x - borderPixels.get(start).x;
        double yDifference = borderPixels.get(end).y - borderPixels.get(start).y;
        double originalAngular = Math.atan2(yDifference, xDifference);
        int pixelAmount;
        if(Math.abs(end -start) > borderPixels.size()/2) {
            pixelAmount = borderPixels.size() - Math.abs(end-start);
        } else {
            pixelAmount = Math.abs(end-start);
        }
        int[][] result = new int[pixelAmount][2];
        System.out.println("x: " + borderPixels.get(start).x + " y: " + borderPixels.get(start).y + " originalAngular: " + originalAngular);

        for (int i = 0; i < pixelAmount; i++) {
            float x = (borderPixels.get((i + start) %  borderPixels.size()).x);
            float y = (borderPixels.get((i + start) % borderPixels.size()).y);
            result [i][0] = (int) (x * Math.cos(originalAngular) - y * Math.sin(originalAngular));
            result [i][1] = (int) (y * Math.cos(originalAngular) - x * Math.sin(originalAngular));
        }
        return result;
    }
}
