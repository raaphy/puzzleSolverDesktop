package de.raphaelschilling.PixelContainer;

import java.util.ArrayList;

public class PixelQue {
    final int startX;
    final int startY;
    ArrayList<Integer> addQueX = new ArrayList<>();
    ArrayList<Integer> addQueY = new ArrayList<>();
    int leftCorner;
    int rightCorner;
    int topCorner;
    int bottomCorner;
    public void addPixel(int x, int y) {
        addQueX.add(x);
        addQueY.add(y);
        if(x < leftCorner) leftCorner = x;
        if(x > rightCorner) rightCorner = x;
        if(y < topCorner) topCorner = y;
        if(y > bottomCorner) bottomCorner = y;
    }

    public PixelQue(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
        leftCorner = startX;
        rightCorner = startX;
        topCorner = startY;
        bottomCorner = startY;
    }

    public int getPixelAmount() {
        return addQueX.size();
    }

}
