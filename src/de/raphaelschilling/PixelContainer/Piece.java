package de.raphaelschilling.PixelContainer;

import java.awt.*;
import java.util.ArrayList;

public class Piece {
    private int startXRelative;
    private int startYRelative;
    private int[][] pixelMatrix = null;
    private ArrayList<int[]> borderList = null;
    private float[] accuracyBorder;
    private int topCorner;
    private int leftCorner;
    private final int[] STEP_X = {1,0,-1,0};
    private final int[] STEP_Y = {0,1,0,-1};


    public Piece(PixelQue pixelQue) {
        int color = Color.HSBtoRGB((float) Math.random(),0.5f,0.5f);
        pixelMatrix = new int[pixelQue.rightCorner - pixelQue.leftCorner + 1][pixelQue.bottomCorner - pixelQue.topCorner + 1];
        for(int i = 0; i < pixelQue.addQueY.size(); i++) {
            pixelMatrix[pixelQue.addQueX.get(i) - pixelQue.leftCorner][pixelQue.addQueY.get(i) - pixelQue.topCorner] = color;
        }
        startXRelative = pixelQue.startX - pixelQue.leftCorner;
        startYRelative = pixelQue.startY - pixelQue.topCorner;
        this.topCorner = pixelQue.topCorner;
        this.leftCorner = pixelQue.leftCorner;

    }
    public void drawItselfTo(int[][] result) {
        for(int y = topCorner; y < topCorner + pixelMatrix[0].length; y++) {
            for (int x = leftCorner; x < leftCorner + pixelMatrix.length; x++) {
                if(pixelMatrix[x-leftCorner][y-topCorner] != 0x00000000) {
                    result[x][y] = pixelMatrix[x-leftCorner][y-topCorner];
                }
            }
        }
        for(int i = 0; i< borderList.size(); i++) {
            if(true) {
                result[borderList.get(i)[0]+leftCorner][borderList.get(i)[1] + topCorner] = Color.HSBtoRGB((float) (1),1f, (float) (1- accuracyBorder[i]/5));
                System.out.println(accuracyBorder[i] + " " + (1  - accuracyBorder[i]/5));
            }
        }
    }
    public boolean createEdgeList() {
        borderList = new ArrayList<>();
        int currentX = startXRelative;
        int currentY = startYRelative;
        int currentDirection = 0;
        while (!(currentX == startXRelative && currentY == startYRelative && currentDirection == 3)) {
            borderList.add(new int[]{currentX,currentY});
            if(isPiecePixel(currentX + STEP_X[(currentDirection + 3)%4], currentY + STEP_Y[(currentDirection + 3)%4])) {
                currentDirection = (currentDirection + 3)%4;
                currentX = currentX + STEP_X[currentDirection];
                currentY = currentY + STEP_Y[currentDirection];
            } else if(isPiecePixel(currentX + STEP_X[(currentDirection + 0)%4], currentY + STEP_Y[(currentDirection + 0)%4])) {
                currentDirection = (currentDirection + 0)%4;
                currentX = currentX + STEP_X[currentDirection];
                currentY = currentY + STEP_Y[currentDirection];
            } else if(isPiecePixel(currentX + STEP_X[(currentDirection + 1)%4], currentY + STEP_Y[(currentDirection + 1)%4])) {
                currentDirection = (currentDirection + 1)%4;
                currentX = currentX + STEP_X[currentDirection];
                currentY = currentY + STEP_Y[currentDirection];
            } else if(isPiecePixel(currentX + STEP_X[(currentDirection + 2)%4], currentY + STEP_Y[(currentDirection + 2)%4])) {
                currentDirection = (currentDirection + 2)%4;
                currentX = currentX + STEP_X[currentDirection];
                currentY = currentY + STEP_Y[currentDirection];
            } else {
                return false;
            }
        }

        return true;
    }

    private boolean isPiecePixel(int x, int y) {
        if(x<0 || x >= pixelMatrix.length || y < 0 || y >= pixelMatrix[0].length) {
            return false;
        }
        if(pixelMatrix[x][y] == 0x00000000) {
            return false;
        }
        return true;

    }

    public void markEdges() {
        Edge[] bestEdges = new Edge[4];
        accuracyBorder = new float[borderList.size()];
        for (int i = 0; i < borderList.size(); i++) {
            accuracyBorder[i]= calcEdgeAccuracy(i);
            //System.out.println(accuracyBorder[i]);
        }
    }

    private float calcEdgeAccuracy(int borderIndex) {
        int edgeDepth = pixelMatrix.length/8;
        float error = 0;
        float angularBefore = angularBetweenBorderListEntrys(borderIndex, borderIndex - edgeDepth);
        for(int i = -1; i >  -edgeDepth; i--) {
            float angular = angularBetweenBorderListEntrys(borderIndex, borderIndex + i);
            error += Math.abs(angularDifference(angular, angularBefore));
        }
        float angularAfter = angularBetweenBorderListEntrys(borderIndex, borderIndex + edgeDepth);
        for(int i = 1; i < edgeDepth;i++) {
            float angular = angularBetweenBorderListEntrys(borderIndex, borderIndex + i);
            error += Math.abs(angularDifference(angular, angularAfter));
        }
        return (float) (angularDifference(angularBefore,angularAfter)*2 - Math.PI/2 + error/edgeDepth) ;
    }

    private float angularBetweenBorderListEntrys(int index1, int index2) {
        int x = borderList.get(Math.floorMod(index1,borderList.size()))[0] - borderList.get(Math.floorMod(index2,borderList.size()))[0];
        int y = borderList.get(Math.floorMod(index1,borderList.size()))[1] - borderList.get(Math.floorMod(index2,borderList.size()))[1];
        return calcAngularOfVector(x, y);
    }

    private float angularDifference(float angular, float angularBefore) {
        if(Math.abs(angular - angularBefore) > Math.PI) {
            return (float) Math.abs(2* Math.PI - Math.abs(angular - angularBefore));
        } else {
            return (float) Math.abs(angular - angularBefore);
        }
    }




    private float calcAngularOfVector(int x, int y) {
        return (float) Math.atan2(x,y);
    }
}
