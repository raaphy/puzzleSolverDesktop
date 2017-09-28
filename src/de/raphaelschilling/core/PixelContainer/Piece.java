package de.raphaelschilling.core.PixelContainer;

import java.awt.*;
import java.util.ArrayList;

public class Piece {
    int startXRelative;
    int startYRelative;
    private int[][] pixelMatrix = null;
    private ArrayList<BorderPixel> borderList = null;
    private int topCorner;
    private int leftCorner;
    final int[] STEP_X = {1, 0, -1, 0};
    final int[] STEP_Y = {0, 1, 0, -1};
    private int[] cornerIds = null;
    public int pieceID;
    private Edge[] edges = null;


    public Piece(PixelQue pixelQue, int pieceID) {
        this.pieceID = pieceID;
        int color = Color.HSBtoRGB((float) 0.5f, 0.5f, 0.5f);
        pixelMatrix = new int[pixelQue.rightCorner - pixelQue.leftCorner + 1][pixelQue.bottomCorner - pixelQue.topCorner + 1];
        for (int i = 0; i < pixelQue.addQueY.size(); i++) {
            pixelMatrix[pixelQue.addQueX.get(i) - pixelQue.leftCorner][pixelQue.addQueY.get(i) - pixelQue.topCorner] = color;
        }
        startXRelative = pixelQue.startX - pixelQue.leftCorner;
        startYRelative = pixelQue.startY - pixelQue.topCorner;
        this.topCorner = pixelQue.topCorner;
        this.leftCorner = pixelQue.leftCorner;

    }

    public void drawItselfTo(int[][] result) {
        for (int y = topCorner; y < topCorner + pixelMatrix[0].length; y++) {
            for (int x = leftCorner; x < leftCorner + pixelMatrix.length; x++) {
                if (pixelMatrix[x - leftCorner][y - topCorner] != 0x00000000) {
                    result[x][y] = pixelMatrix[x - leftCorner][y - topCorner];
                }
            }
        }
        for (BorderPixel aBorderList : borderList) {
            result[aBorderList.x + leftCorner][aBorderList.y + topCorner] = Color.HSBtoRGB((float) (1), 1f, (float) (aBorderList.accuracy));
        }


    }


    boolean isPiecePixel(int x, int y) {
        if (x < 0 || x >= pixelMatrix.length || y < 0 || y >= pixelMatrix[0].length) {
            return false;
        }
        if (pixelMatrix[x][y] == 0x00000000) {
            return false;
        }
        return true;

    }

    public void drawAnalysePictureTo(int[][] result) {

        getEdges();
        this.drawItselfTo(result);

    }

    public Edge[] getEdges() {
        borderList = BorderCreator.createBorderList(this);
        cornerIds = CornerFinder.findCorners(borderList, pixelMatrix.length, pixelMatrix[0].length);
        if (edges != null) {
            return edges;
        }
        edges = new Edge[cornerIds.length];
        for (int i = 0; i < cornerIds.length; i++) {
            edges[i] = new Edge(borderList, cornerIds[i], cornerIds[(i + 1) % cornerIds.length], pieceID, leftCorner, topCorner);
        }
        return edges;
    }
}
