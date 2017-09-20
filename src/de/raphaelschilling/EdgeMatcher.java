package de.raphaelschilling;

import de.raphaelschilling.PixelContainer.Edge;

import java.awt.*;
import java.util.ArrayList;

public class EdgeMatcher {

    private static final float MAX_MATCH_ACCORDANCE = 200;
    private ArrayList<Edge> edges;
    private float[][] matches = null;
    private ArrayList<EdgeMatch> edgeMatches = null;

    public EdgeMatcher(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public String drawMatchesTo(int[][] drawArea) {
        if (edgeMatches == null) {
            calcBestMatches();
        }
        float hue = 0;
        for (int i = 0; i < edgeMatches.size(); i++) {

            int color = Color.HSBtoRGB(hue, (float) i / edgeMatches.size(), (float) i / edgeMatches.size());
            edges.get(edgeMatches.get(i).i).drawTo(drawArea, color);
            edges.get(edgeMatches.get(i).j).drawTo(drawArea, color);
            edgeMatches.get(i).drawTo(drawArea, color);
            hue = (float) ((hue + Math.PI / 10) % 1f);

        }
        return "asd";
    }

    private void calcBestMatches() {
        if (matches == null) {
            calcAccordance();
        }
        edgeMatches = new ArrayList<>();
        for (int round = 0; round < edges.size() / 2; round++) {
            float bestValue = MAX_MATCH_ACCORDANCE;
            int bestI = -1;
            int bestJ = -1;
            for (int i = 0; i < edges.size(); i++) {
                for (int j = 0; j < edges.size(); j++) {
                    if (matches[i][j] < bestValue) {
                        bestValue = matches[i][j];
                        bestI = i;
                        bestJ = j;
                    }
                }
            }
            if (bestI == -1) {
                break;
            }
            for (int iJ = 0; iJ < edges.size(); iJ++) {
                matches[iJ][bestJ] = Float.MAX_VALUE;
                matches[bestI][iJ] = Float.MAX_VALUE;
            }
            edgeMatches.add(new EdgeMatch(bestI, bestJ, bestValue, edges));

        }
    }

    private void calcAccordance() {
        matches = new float[edges.size()][edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < edges.size(); j++) {
                matches[i][j] = edges.get(i).getMatch(edges.get(j));
            }
        }
    }
}
