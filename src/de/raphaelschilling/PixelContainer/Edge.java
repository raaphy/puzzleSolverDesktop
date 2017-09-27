package de.raphaelschilling.PixelContainer;

import java.util.ArrayList;

public class Edge {
    private ArrayList<BorderPixel> borderPixels = null;
    private int start;
    private int end;
    public int pieceID;
    private final double MAX_LENGTH_DIFF = 1.5;
    private float[][] normalized = null;
    private int startY;
    private int startX;
    private int amount;

    public Edge(ArrayList<BorderPixel> borderPixels, int start, int end, int pieceID, int startX, int startY) {
        this.borderPixels = borderPixels;
        this.start = start;
        this.end = end;
        this.pieceID = pieceID;
        this.startX = startX;
        this.startY = startY;
        if (end - start > 0) {
            amount = end - start;
        } else {
            amount = end + borderPixels.size() - start;
        }
    }

    public float[][] normalize() {
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
        float[][] result = new float[pixelAmount][2];
        for (int i = 0; i < pixelAmount; i++) {
            float x = (borderPixels.get((i + start) % borderPixels.size()).x);
            float y = (borderPixels.get((i + start) % borderPixels.size()).y);
            result[i][0] = (float) (x * Math.cos(-originalAngular) - y * Math.sin(-originalAngular));
            result[i][1] = (float) (y * Math.cos(-originalAngular) + x * Math.sin(-originalAngular));
        }
        normalized = result;
        return result;
    }

    public float getMatchFloat(Edge other) {
        if (other.pieceID == this.pieceID) {
            return Float.MAX_VALUE;
        }
        if(other == this) {
        	return Float.MAX_VALUE;
        }
        float[][] thisNormalized = normalize();
        float[][] otherNormalized = other.normalize();
        if (otherNormalized.length > thisNormalized.length * MAX_LENGTH_DIFF || thisNormalized.length > otherNormalized.length * MAX_LENGTH_DIFF) {
            return Float.MAX_VALUE;
        }

        float weightXDiff = (getWeightX() - (-other.getWeightX()));
        float weightYDiff = (getWeightY() - (-other.getWeightY()));
        float result = 0;
        for (int i = 0; i < normalized.length; i++) {
            float x = normalized[i][0];
            float y = normalized[i][1];
            float additionalError= (float) Math.pow(getDistanceOfNearest(-(x - weightXDiff ), -(y - weightYDiff), otherNormalized),3f);
            result += additionalError;
            if(additionalError > 4000) {
            	return Float.MAX_VALUE;
            }
            if(i > normalized.length/10 && result/ (i+1) > 500) {
            	return Float.MAX_VALUE;
            }
            
        }
        result = result / normalized.length;
        result += Math.abs(thisNormalized.length - otherNormalized.length) / (float)normalized.length* 10;
        return result;
    }

    private float getDistanceOfNearest(float x, float y, float[][] smallerNormalized) {
        float smallestDistance = Float.MAX_VALUE;
        for(float[] position : smallerNormalized) {
            float distance = (float) Math.sqrt((x-position[0])*(x-position[0]) + (y-position[1]) * (y-position[1]));
            if(distance < smallestDistance) {
                smallestDistance = distance;
            }
        }
        return smallestDistance;
    }

    private float getWeightY() {
        float ySum = 0;
        normalize();
        for(float[] position : normalized) {
            ySum += position[1];
        }
        return ySum / amount;


    }

    public float getMatch1(Edge other) {
        if (other.pieceID == this.pieceID) {
            return Float.MAX_VALUE;
        }
        float[][] thisNormalized = normalize();
        float[][] otherNormalized = other.normalize();
        if (Math.abs(otherNormalized.length - thisNormalized.length) > otherNormalized.length * MAX_LENGTH_DIFF) {
            return Float.MAX_VALUE;
        }
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
            int xDiff = (int) (-otherNormalized[checkPixelAmount - i - 1][0] - thisNormalized[i][0] + xSumDiff);
            int yDiff = (int) (-otherNormalized[checkPixelAmount - i - 1][1] - thisNormalized[i][1] + ySumDiff);
            result += Math.sqrt(((float) xDiff) * xDiff + (float) yDiff * yDiff);
        }
        result = result / checkPixelAmount;
        result += Math.abs(thisNormalized.length - otherNormalized.length) / checkPixelAmount * 1;
        return result;
    }

    public float getMatch(Edge other) {
        if (other.pieceID == this.pieceID) {
            return Float.MAX_VALUE;
        }
        float[][] thisNormalized = normalize();
        float[][] otherNormalized = other.normalize();
        if (Math.abs(otherNormalized.length - thisNormalized.length) > otherNormalized.length * MAX_LENGTH_DIFF) {
            return Float.MAX_VALUE;
        }
        int checkPixelAmount = thisNormalized.length;
        if (thisNormalized.length > otherNormalized.length) {
            checkPixelAmount = otherNormalized.length;
        }
        int lastDiffX = (int) (thisNormalized[0][0] + otherNormalized[checkPixelAmount - 1][0]);
        int lastDiffY = (int) (thisNormalized[0][1] + otherNormalized[checkPixelAmount - 1][1]);
        float result = 0;
        for (int i = 1; i < checkPixelAmount; i++) {
            int xDiff = (int) (otherNormalized[checkPixelAmount - i - 1][0] + thisNormalized[i][0]);
            int yDiff = (int) (otherNormalized[checkPixelAmount - i - 1][1] + thisNormalized[i][1]);
            int xWrongness = xDiff - lastDiffX;
            int yWrongness = yDiff - lastDiffY;
            lastDiffX = xDiff;
            lastDiffY = yDiff;
            result += Math.sqrt(((float) xWrongness) * xWrongness + (float) yWrongness * yWrongness);
        }
        result = result / checkPixelAmount;
        result += Math.abs(thisNormalized.length - otherNormalized.length) / checkPixelAmount * 1;
        return result;
    }

    private float getWeightX() {
        float xSum = 0;
        normalize();
        for(float[] position : normalized) {
            xSum += position[0];
        }
        return xSum / amount;
    }

    public void drawTo(int[][] result, int color) {
        for (int i = 0; i < amount; i++) {
            int currentElement = Math.floorMod(i + start, borderPixels.size());
            result[borderPixels.get(currentElement).x + startX][borderPixels.get(currentElement).y + startY] = color;
        }
    }
    public void drawNormalizedTo(int [][] result, int color) {
        for (int i = 0; i < amount; i++) {
            result[(int) (normalized[i][0] + startX)][(int) (normalized[i][1]+ startY)] = color;
        }
    }
    public float getReferenceX() {
        return borderPixels.get(start).x + startX;
    }

    public float getReferenceY() {
        return borderPixels.get(start).y + startY;
    }
}
