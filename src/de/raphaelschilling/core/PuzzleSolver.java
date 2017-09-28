package de.raphaelschilling.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.raphaelschilling.core.PixelContainer.Edge;
import de.raphaelschilling.core.PixelContainer.Piece;
import de.raphaelschilling.core.PixelContainer.PixelQue;

public class PuzzleSolver {
    private static final int MIN_PIXEL_PIECE = 99;
    private int[][] imageArray = null;
    private boolean[][] handeledArray = null;
    private float[] hsbTemp = new float[3];
    private float[] puzzleColor = new float[]{0.51666665f,0.22222222f,0.88235295f};
    private ArrayList<Piece> pieces;
    private int pieceID;

    public void setImageArray(int[][] imageArray) {
        this.imageArray = imageArray;
    }



    public int[][] getMaskPicture() {
        if(imageArray == null) {
            return null;
        }
        pieceID = 0;
        pieces = new ArrayList<>();
        handeledArray = new boolean[imageArray.length][imageArray[0].length];
        for(int y = 0; y < imageArray[0].length; y++) {
            for (int x = 0; x < imageArray.length; x++) {
                if (!handeledArray[x][y] && isPuzzlePixel(x, y)) {
                    maskPiece(x, y, MIN_PIXEL_PIECE);
                }
            }
        }
        ArrayList<Edge> edges = new ArrayList<>();
        int[][] result = new int[imageArray.length][imageArray[0].length];
        for (Piece piece : pieces) {
            Collections.addAll(edges, piece.getEdges());

        }
        EdgeMatcher edgeMatcher = new EdgeMatcher(edges);
        for (Piece piece : pieces) {

            piece.drawItselfTo(result);
        }
        edgeMatcher.drawMatchesTo(result);



        return result;
    }


   private void maskPiece(int x, int y, int minPixelPiece) {
       if(x < 0 || y < 0 ||x >= imageArray.length || y >= imageArray[0].length) {
           return;
       }
       PixelQue pixelQue = new PixelQue(x,y);
       List<int[]> pixelList = new ArrayList<>(0);
       pixelList.add(new int[]{x,y});
       while(!pixelList.isEmpty()) {
            int[] x_y = pixelList.get(pixelList.size()-1);
            int currentX = x_y[0];
            int currentY = x_y[1];
            pixelList.remove(pixelList.size()-1);
           if(!(currentX < 0 || currentY < 0 ||currentX >= imageArray.length || currentY >= imageArray[0].length)) {
               if(!handeledArray[currentX][currentY] && isPuzzlePixel(currentX, currentY)) {
                   pixelQue.addPixel(currentX, currentY);
                   handeledArray[currentX][currentY] = true;
                   pixelList.add(new int[]{currentX - 1, currentY});
                   pixelList.add(new int[]{currentX+1,currentY});
                   pixelList.add(new int[]{currentX,currentY-1});
                   pixelList.add(new int[]{currentX,currentY+1});
               }
           }

       }
       if(pixelQue.getPixelAmount() > minPixelPiece) {
           pieceID++;
           pieces.add(new Piece(pixelQue, pieceID));
           System.out.println(pieces.size());
       }
   }

    public boolean isPuzzlePixel(int x, int y) {
        return colorDifference(Color.RGBtoHSB(getR(x, y), getG(x, y), getB(x, y), this.hsbTemp), puzzleColor) < 3;
    }
    private float colorDifference(float[] hsvCompare1, float[] hsvCompare2) {
        return (Math.abs(hsvCompare1[0] - hsvCompare2[0]))*7  +
                (Math.abs(hsvCompare1[1] - hsvCompare2[1]))*17f +
                (Math.abs(hsvCompare1[2] - hsvCompare2[2]))*5;
    }

    public int getB(int x, int y) {
        return (imageArray[x][y] & 0x000000FF) ;
    }

    public int getG(int x, int y) {
        return (imageArray[x][y] & 0x0000FF00) >> 8;
    }

    public int getR(int x, int y) {
        return (imageArray[x][y] & 0x00FF0000) >> 16;
    }

    public void setCoordiatesReferenceColour(int xPuzzleColor, int yPuzzleColor) {
        Color.RGBtoHSB(getR(xPuzzleColor,yPuzzleColor), getG(xPuzzleColor,yPuzzleColor), getB(xPuzzleColor,yPuzzleColor), puzzleColor);
        System.out.println("Using color H:" + puzzleColor[0] + " S: " + puzzleColor[1] + " B: " + puzzleColor[2]);
    }
}
