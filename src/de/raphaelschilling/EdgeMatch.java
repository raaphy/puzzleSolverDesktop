package de.raphaelschilling;

public class EdgeMatch {
    public int bestI;
    public int bestJ;
    private float bestValue;

    public EdgeMatch(int bestI, int bestJ, float bestValue) {
        this.bestI = bestI;
        this.bestJ = bestJ;
        this.bestValue = bestValue;
    }
}
