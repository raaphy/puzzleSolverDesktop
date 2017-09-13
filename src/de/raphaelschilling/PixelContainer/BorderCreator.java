package de.raphaelschilling.PixelContainer;

import java.util.ArrayList;

public class BorderCreator {
    public static ArrayList<BorderPixel> createBorderList(Piece piece) {
        ArrayList<BorderPixel> borderList = new ArrayList<>();
        int currentX = piece.startXRelative;
        int currentY = piece.startYRelative;
        int currentDirection = 0;
        while (!(currentX == piece.startXRelative && currentY == piece.startYRelative && currentDirection == 3)) {
            borderList.add(new BorderPixel(currentX, currentY, -1f, borderList.size()));
            if (piece.isPiecePixel(currentX + piece.STEP_X[(currentDirection + 3) % 4], currentY + piece.STEP_Y[(currentDirection + 3) % 4])) {
                currentDirection = (currentDirection + 3) % 4;
                currentX = currentX + piece.STEP_X[currentDirection];
                currentY = currentY + piece.STEP_Y[currentDirection];
            } else if (piece.isPiecePixel(currentX + piece.STEP_X[(currentDirection + 0) % 4], currentY + piece.STEP_Y[(currentDirection + 0) % 4])) {
                currentDirection = (currentDirection + 0) % 4;
                currentX = currentX + piece.STEP_X[currentDirection];
                currentY = currentY + piece.STEP_Y[currentDirection];
            } else if (piece.isPiecePixel(currentX + piece.STEP_X[(currentDirection + 1) % 4], currentY + piece.STEP_Y[(currentDirection + 1) % 4])) {
                currentDirection = (currentDirection + 1) % 4;
                currentX = currentX + piece.STEP_X[currentDirection];
                currentY = currentY + piece.STEP_Y[currentDirection];
            } else if (piece.isPiecePixel(currentX + piece.STEP_X[(currentDirection + 2) % 4], currentY + piece.STEP_Y[(currentDirection + 2) % 4])) {
                currentDirection = (currentDirection + 2) % 4;
                currentX = currentX + piece.STEP_X[currentDirection];
                currentY = currentY + piece.STEP_Y[currentDirection];
            } else {
                return null;
            }
        }

        return borderList;
    }
}
