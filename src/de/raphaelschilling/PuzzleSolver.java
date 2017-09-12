package de.raphaelschilling;

import de.raphaelschilling.PixelContainer.Piece;
import de.raphaelschilling.PixelContainer.PixelQue;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PuzzleSolver {
    private static final int MIN_PIXEL_PIECE = 99;
    private int[][] imageArray = null;
    private boolean[][] handeledArray = null;
    private float[] hsbTemp = new float[3];
    private float[] puzzleColor = new float[]{0.51666665f,0.22222222f,0.88235295f};
    private ArrayList<Piece> pieces;

    public void setImageArray(int[][] imageArray) {
        this.imageArray = imageArray;
    }



    public int[][] getMaskPicture() {
        if(imageArray == null) {
            return null;
        }
        pieces = new ArrayList<Piece>();
        handeledArray = new boolean[imageArray.length][imageArray[0].length];
        for(int y = 0; y < imageArray[0].length; y++) {
            for (int x = 0; x < imageArray.length; x++) {
                if (handeledArray[x][y] == false && isPuzzlePixel(x, y)) {
                    maskPiece(x, y, MIN_PIXEL_PIECE);
                }
            }
        }


        int[][] result = new int[imageArray.length][imageArray[0].length];
        for(Piece piece : pieces) {
            piece.createEdgeList();
            piece.markEdges();

            piece.drawItselfTo(result);
        }
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
                    pixelList.add(new int[]{currentX-1,currentY});
                   pixelList.add(new int[]{currentX+1,currentY});
                   pixelList.add(new int[]{currentX,currentY-1});
                   pixelList.add(new int[]{currentX,currentY+1});
               }
           }

       }
       if(pixelQue.getPixelAmount() > minPixelPiece) {
           pieces.add(new Piece(pixelQue));
           System.out.println(pieces.size());
       }
   }

    public boolean isPuzzlePixel(int x, int y) {
        if(colorDifference(Color.RGBtoHSB(getR(x,y), getG(x,y), getB(x,y), this.hsbTemp),puzzleColor) < 3) {
            return true;
        } else {
            return false;
        }
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
