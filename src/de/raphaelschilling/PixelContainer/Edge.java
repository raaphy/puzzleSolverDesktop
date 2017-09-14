package de.raphaelschilling.PixelContainer;

import java.util.ArrayList;

public class Edge {
    private ArrayList<BorderPixel> borderPixels = null;
    private int start;
    private int end;
    public int pieceID;
    public int complement = -1;
    private final double MAX_LENGTH_DIFF = 0.1;
    private int[][] normalized = null;

    public Edge(ArrayList<BorderPixel> borderPixels, int start, int end, int pieceID) {
        this.borderPixels = borderPixels;
        this.start = start;
        this.end = end;
        this.pieceID = pieceID;
    }

    public int[][] normalize() {
        if (normalized != null) {
            return normalized;
        }
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
            float x = (borderPixels.get((i + start) % borderPixels.size()).x);
            float y = (borderPixels.get((i + start) % borderPixels.size()).y);
            result[i][0] = (int) (x * Math.cos(-originalAngular) - y * Math.sin(-originalAngular));
            result[i][1] = (int) (y * Math.cos(-originalAngular) + x * Math.sin(-originalAngular));
        }
        normalized = result;
        return result;
    }

    public float getMatch(Edge other) {
        if (other.pieceID == this.pieceID || Math.abs(other.borderPixels.size() - borderPixels.size()) > MAX_LENGTH_DIFF * borderPixels.size()) {
            return Float.MAX_VALUE;
        }
        int[][] thisNormalized = normalize();
        int[][] otherNormalized = other.normalize();
        int checkPixelAmount = thisNormalized.length;
        if (thisNormalized.length > otherNormalized.length) {
            checkPixelAmount = otherNormalized.length;
        }
        int xSumDiff = 0;
        int ySumDiff = 0;
        for (int i = 0; i < checkPixelAmount; i++) {
            xSumDiff += thisNormalized[i][0] + otherNormalized[checkPixelAmount - i - 1][0];
            ySumDiff += thisNormalized[i][1] + otherNormalized[checkPixelAmount - i - 1][1];
        }
        float result = 0;
        xSumDiff = (int) ((float) xSumDiff / checkPixelAmount + 0.5f);
        ySumDiff = (int) ((float) ySumDiff / checkPixelAmount + 0.5f);
        for (int i = 0; i < checkPixelAmount; i++) {
            int xDiff = -otherNormalized[checkPixelAmount - i - 1][0] - thisNormalized[i][0] + xSumDiff;
            int yDiff = -otherNormalized[checkPixelAmount - i - 1][1] - thisNormalized[i][1] + ySumDiff;
            result += Math.sqrt(((float) xDiff) * xDiff + (float) yDiff * yDiff);
        }
        result += Math.abs(thisNormalized.length - otherNormalized.length) * thisNormalized.length * 0.2;
        return result;
    }
}
