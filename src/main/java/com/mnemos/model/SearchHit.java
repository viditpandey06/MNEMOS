package com.mnemos.model;

public class SearchHit implements Comparable<SearchHit> {
    private VectorItem item;
    private double distance;

    public SearchHit(VectorItem item, double distance) {
        this.item = item;
        this.distance = distance;
    }

    public VectorItem getItem() { return item; }
    public double getDistance() { return distance; }

    @Override
    public int compareTo(SearchHit o) {
        return Double.compare(this.distance, o.distance);
    }
}
