package de.raphaelschilling.core;

import java.util.ArrayList;

import de.raphaelschilling.core.PixelContainer.Edge;

public class EdgeMatch {
    public int i;
    public int j;
    private float bestValue;
    private ArrayList<Edge> edgeList;

    public EdgeMatch(int i, int j, float bestValue, ArrayList<Edge> edgeList) {
        this.i = i;
        this.j = j;
        this.bestValue = bestValue;
        this.edgeList = edgeList;
        System.out.println(bestValue);
    }

    public void drawTo(int[][] drawArea, int color) {
        Edge edgeI = edgeList.get(i);
        Edge edgeJ = edgeList.get(j);
        float xDiff = edgeI.getReferenceX() - edgeJ.getReferenceX();
        float yDiff = edgeI.getReferenceY() - edgeJ.getReferenceY();
        for(int i = 0; i < 100; i++) {
            drawArea[(int) (edgeJ.getReferenceX() + xDiff *i/100)][(int) (edgeJ.getReferenceY() + yDiff *i/100)] =color;
        }
    }
}
