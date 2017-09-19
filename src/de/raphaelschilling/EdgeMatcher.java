package de.raphaelschilling;

import de.raphaelschilling.PixelContainer.Edge;

import java.util.ArrayList;

public class EdgeMatcher {

    private ArrayList<Edge> edges;

    public EdgeMatcher(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public String getBestMatch(int[] result) {
        float[][] matches;
        float bestValue = Float.MAX_VALUE;
        int bestI = 0;
        int bestJ = 0;
        matches = new float[edges.size()][edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.size(); j++) {
                matches[i][j] = edges.get(i).getMatch(edges.get(j));
                if (matches[i][j] < bestValue) {
                    bestI = i;
                    bestJ = j;
                    bestValue = matches[i][j];
                }
            }
        }
        result[0] = bestI;
        result[1] = bestJ;
        return "Best Value: " + bestValue + "i: " + bestI + " j: " + bestJ;
    }
}
